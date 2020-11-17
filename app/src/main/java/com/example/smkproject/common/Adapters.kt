package com.example.smkproject.common

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.smkproject.models.Recipe
import com.example.smkproject.models.Tag

class DBHelper internal constructor(context: Context?) :
    SQLiteOpenHelper(context,
        DATABASE_NAME, null,
        DATABASE_VERSION
    ) {


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_RECIPES)
        db.execSQL(CREATE_TABLE_TAGS)
        db.execSQL(CREATE_TABLE_RECIPESTAGS)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("drop table if exists ${TABLE_RECIPES}")
        db.execSQL("drop table if exists ${TABLE_TAGS}")
        db.execSQL("drop table if exists ${TABLE_RECIPESTAGS}")
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "RecipesDB"
        const val TABLE_RECIPES = "recipes"
        const val TABLE_TAGS = "tags"
        const val TABLE_RECIPESTAGS = "recipes_tags"
        const val  CREATE_TABLE_RECIPES = "create table " + TABLE_RECIPES + "(" +
                RecipesAdapterDB.KEY_ID + " integer primary key," +
                RecipesAdapterDB.KEY_TITLE + " text," +
                RecipesAdapterDB.KEY_DESCRIB + " text," +
                RecipesAdapterDB.KEY_DATETIME + " text, " +
                RecipesAdapterDB.KEY_TAGS +" text"+ ")"
        const val  CREATE_TABLE_TAGS = "create table " + TABLE_TAGS + "(" +
                TagsAdapterDB.KEY_ID + " integer primary key," +
                TagsAdapterDB.KEY_TAG + " text, " +
                TagsAdapterDB.KEY_COUNT + " int" +
                ")"
        const val CREATE_TABLE_RECIPESTAGS = "create table " + TABLE_RECIPESTAGS +
                "(" +
                RTAdapterDB.KEY_ID + " integer primary key, " +
                RTAdapterDB.KEY_IDRECIPE + " int, " +
                RTAdapterDB.KEY_IDTAG +" int, "+
                " FOREIGN KEY (" + RTAdapterDB.KEY_IDRECIPE + ") REFERENCES " + TABLE_RECIPES +"("+ RecipesAdapterDB.KEY_ID +"), "+
                "FOREIGN KEY (" + RTAdapterDB.KEY_IDTAG + ") REFERENCES " + TABLE_TAGS +"("+ TagsAdapterDB.KEY_ID +")"+
                ")"
    }
}
class RecipesAdapterDB(_dbHelper: DBHelper?)
{
    companion object {
        const val KEY_ID = "_id"
        const val KEY_TITLE = "title"
        const val KEY_DESCRIB = "describ"
        const val KEY_DATETIME = "dateTime"
        const val KEY_TAGS = "tags"

    }

    private var dbHelper: DBHelper? = _dbHelper

    fun insert(recipe: Recipe): Long{
        val db = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, recipe.title);
        contentValues.put(KEY_DESCRIB, recipe.describ);
        contentValues.put(KEY_DATETIME, recipe.dateTime);
        contentValues.put(KEY_TAGS, recipe.tags)
        //Вставляем запись в БД и возвращаем ее ID. Если запись не вставиллась, возвращает -1
        return db.insert(DBHelper.TABLE_RECIPES, null, contentValues);
    }
    fun saveRecipe(recipe: Recipe) {

            val rtDB = RTAdapterDB(dbHelper)
            val tagsDB = TagsAdapterDB(dbHelper)

            var idRecipe=insert(recipe)
            var idTag: Long = -1
            for (_tag in recipe.tags.split(",").toTypedArray()){
                var tag = _tag.replace(" ", "")
                idTag = tagsDB.getIdTag(tag)
                tagsDB.updateCount(idTag, true)
                rtDB.insert(idRecipe, idTag)
            }
    }
    //Возвращает все рецепты из БД
    fun loadRecipes(): ArrayList<Recipe>{
        var recipes: ArrayList<Recipe> = ArrayList()

        val database = dbHelper!!.writableDatabase
        val cursor: Cursor =
            database.query(DBHelper.TABLE_RECIPES, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(KEY_ID)
            val titleIndex = cursor.getColumnIndex(KEY_TITLE)
            val describIndex = cursor.getColumnIndex(KEY_DESCRIB)
            val dateTimeIndex = cursor.getColumnIndex(KEY_DATETIME)
            val tagsIndex = cursor.getColumnIndex(KEY_TAGS)
            do {

                recipes.add(
                    Recipe(
                    cursor.getString(titleIndex).toString(),
                    cursor.getString(describIndex).toString(),
                    cursor.getString(dateTimeIndex).toString(),
                    cursor.getString(tagsIndex).toString())
                )

            } while (cursor.moveToNext())
        } else Log.d("mLog", "нечего выгружать из бд")

        cursor.close()

        return recipes
    }
    //Возвращает все рецепты из БД в лог
    fun loadRecipesInLog(){
        val database = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        val cursor: Cursor =
            database.query(DBHelper.TABLE_RECIPES, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(KEY_ID)
            val titleIndex = cursor.getColumnIndex(KEY_TITLE)
            val describIndex = cursor.getColumnIndex(KEY_DESCRIB)
            val dateTimeIndex = cursor.getColumnIndex(KEY_DATETIME)
            val tagsIndex = cursor.getColumnIndex(KEY_TAGS)
            do {
                Log.d(
                    "mLog", "ID = " + cursor.getInt(idIndex) +
                            ", title = " + cursor.getString(titleIndex) +
                            ", descrip = " + cursor.getString(describIndex) +
                            ", dateTime = " + cursor.getString(dateTimeIndex) +
                            ", tags = " + cursor.getString(tagsIndex)
                )
            } while (cursor.moveToNext())
        } else Log.d("mLog", "0 rows")

        cursor.close()
    }

}

class TagsAdapterDB(_dbHelper: DBHelper?){
    companion object {
        const val KEY_ID = "_id"
        const val KEY_TAG = "tag"
        const val KEY_COUNT = "count"
    }

    private var dbHelper: DBHelper? = _dbHelper

    fun updateCount(idTag: Long, boolean: Boolean){
        var db = dbHelper!!.writableDatabase
        var cursor = db.query(DBHelper.TABLE_TAGS, null,"$KEY_ID = ?", arrayOf(idTag.toString()), null,null,null,null)
        cursor.moveToFirst()
        var count = cursor.getInt(cursor.getColumnIndex(KEY_COUNT))
        if (true)count+=1
        else { if (count<0) count = 0
                else count-=1
        }
        var contentValues = ContentValues()
        contentValues.put(KEY_COUNT, count)
        db.update(DBHelper.TABLE_TAGS, contentValues,"$KEY_ID = ?", arrayOf(idTag.toString()))
    }

    fun loadTags(): ArrayList<Tag>{
        var tags: ArrayList<Tag> = ArrayList()

        val database = dbHelper!!.writableDatabase
        val cursor: Cursor =
            database.query(DBHelper.TABLE_TAGS, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(KEY_ID)
            val tagIndex = cursor.getColumnIndex(KEY_TAG)
            val countIndex = cursor.getColumnIndex(KEY_COUNT)

            do {
                tags.add(
                    Tag(
                        cursor.getLong(idIndex),
                        cursor.getString(tagIndex),
                        cursor.getInt(countIndex)
                    )
                )

            } while (cursor.moveToNext())
        } else Log.d("mLog", "нечего выгружать из бд")
        cursor.close()
        return tags
    }

    fun getIdTag(tag: String): Long{
        //тут нужна проверка, если id не вернется
        var id = findTag(tag)
        if(id>=0){
            return id
        }
        else {
            id = insertTag(tag)
            return id
        }
    }

    fun findTag(tag: String): Long{
        var idTag: Long = -1
        val database = dbHelper!!.writableDatabase
        var cursor = database.query(DBHelper.TABLE_TAGS, null,
            "$KEY_TAG = ?"
            , arrayOf(tag), null, null, null);


        if(cursor.moveToNext())
            idTag = cursor.getLong(cursor.getColumnIndex(KEY_ID))
        return idTag
    }

    fun insertTag(tag: String): Long{
        val database = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TAG, tag)
        contentValues.put(KEY_COUNT, 1)
        return database.insert(DBHelper.TABLE_TAGS, null, contentValues)
    }

}

class RTAdapterDB(_dbHelper: DBHelper?){
    companion object {
        const val KEY_ID = "_id"
        const val KEY_IDTAG = "id_tags"
        const val KEY_IDRECIPE = "id_recipe"
    }
    private var dbHelper: DBHelper? = _dbHelper
    fun insert(idRecipe: Long, idTag: Long): Long{
        val database = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_IDRECIPE, idRecipe)
        contentValues.put(KEY_IDTAG, idTag)
        return database.insert(DBHelper.TABLE_RECIPESTAGS, null, contentValues);
    }
}