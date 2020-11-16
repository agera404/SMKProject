package com.example.smkproject.common

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.smkproject.models.Recipe
import com.google.android.material.tabs.TabLayout


class RecipesAdapterDB(context: Context?)
{
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "RecipesDB"
        const val TABLE_RECIPES = "recipes"
        const val KEY_ID = "_id"
        const val KEY_TITLE = "title"
        const val KEY_DESCRIB = "describ"
        const val KEY_DATETIME = "dateTime"
        const val KEY_TAGS = "tags"
        const val  CREATE_TABLE_RECIPES = "create table " + TABLE_RECIPES + "(" +
                KEY_ID + " integer primary key," +
                KEY_TITLE + " text," +
                KEY_DESCRIB + " text," +
                KEY_DATETIME + " text, " +
                KEY_TAGS +" text"+ ")"
        const val  CREATE_TABLE_TAGS = "create table " + TagsAdapterDB.TABLE_TAGS + "(" +
                TagsAdapterDB.KEY_ID + " integer primary key," +
                TagsAdapterDB.KEY_TAG + " text, " +
                TagsAdapterDB.KEY_COUNT + " int" +
                ")"
        const val CREATE_TABLE_RECIPESTAGS = "create table " + RTAdapterDB.TABLE_RECIPESTAGS +
                "(" +
                RTAdapterDB.KEY_ID + " integer primary key, " +
                RTAdapterDB.KEY_IDRECIPE + " int, " +
                RTAdapterDB.KEY_IDTAG +" int, "+
                " FOREIGN KEY (" + RTAdapterDB.KEY_IDRECIPE + ") REFERENCES " + RecipesAdapterDB.TABLE_RECIPES +"("+ RecipesAdapterDB.KEY_ID +"), "+
                "FOREIGN KEY (" + RTAdapterDB.KEY_IDTAG + ") REFERENCES " + TagsAdapterDB.TABLE_TAGS +"("+ TagsAdapterDB.KEY_ID +")"+
                ")"
    }
    private class DBHelper internal constructor(context: Context?) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE_TABLE_RECIPES)
            db.execSQL(CREATE_TABLE_TAGS)
            db.execSQL(CREATE_TABLE_RECIPESTAGS)
            Log.d("mLog", "Создали таблицу: " + TABLE_RECIPES)

        }

        override fun onUpgrade(
            db: SQLiteDatabase,
            oldVersion: Int,
            newVersion: Int
        ) {
            db.execSQL("drop table if exists ${TABLE_RECIPES}")
            onCreate(db)
        }
    }
    private var dbHelper: DBHelper? = null



    fun saveRecipe(context: Context, recipe: Recipe) {
        Log.d("mLog", "Начали вставлять новую запись в таблицу: " + TABLE_RECIPES)
        dbHelper = DBHelper(context)
        val database = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, recipe.title);
        contentValues.put(KEY_DESCRIB, recipe.describ);
        contentValues.put(KEY_DATETIME, recipe.dateTime);

        val recipeTagDB = RTAdapterDB(context)
        val tagsDB = TagsAdapterDB(context)


        contentValues.put(KEY_TAGS, recipe.tags)
        database.insert(TABLE_RECIPES, null, contentValues);
        Log.d("mLog", "Вставии новую запись таблицу: " + TABLE_RECIPES)
        Log.d("mLog", "Начинаем создавать зависимость: " + TABLE_RECIPES)

        var cursor = database.query(TABLE_RECIPES, null, KEY_TITLE + "= \""+ recipe.title+"\"", null, null, null, null);
        var idRecipe=0
        if(cursor.moveToFirst()){
            var idindex= cursor.getColumnIndex("_id");
            do {
                idRecipe = cursor.getInt(idindex);

            } while (cursor.moveToNext());
            }

        Log.d("mLog", "idRecipe = " + idRecipe)
        var idTag = 0
        for (_tag in recipe.tags.split(",").toTypedArray()){
            var tag = _tag.replace(" ", "")
            if(tagsDB.findTag(tag, context)>=0){
                idTag = tagsDB.findTag(tag, context)
            }else{
                idTag = tagsDB.addNewTag(tag, context)
            }
            recipeTagDB.setDepend(idRecipe, idTag, context)
        }

    }
    fun loadRecipes(context: Context): ArrayList<Recipe>{
        var recipes: ArrayList<Recipe> = ArrayList()
        dbHelper = DBHelper(context)
        val database = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        val cursor: Cursor =
            database.query(TABLE_RECIPES, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(KEY_ID)
            val titleIndex = cursor.getColumnIndex(KEY_TITLE)
            val describIndex = cursor.getColumnIndex(KEY_DESCRIB)
            val dateTimeIndex = cursor.getColumnIndex(KEY_DATETIME)
            val tagsIndex = cursor.getColumnIndex(KEY_TAGS)

            do {
                val tags = cursor.getString(tagsIndex).toString()
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
    fun loadRecipesInLog(context: Context){
        dbHelper = DBHelper(context)
        val database = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        val cursor: Cursor =
            database.query(TABLE_RECIPES, null, null, null, null, null, null)
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

class TagsAdapterDB(context: Context?){
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "RecipesDB"
        const val TABLE_TAGS = "tags"
        const val KEY_ID = "_id"
        const val KEY_TAG = "tag"
        const val KEY_COUNT = "count"

    }
    class DBHelper internal constructor(context: Context?) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                "create table " + TABLE_TAGS + "(" +
                        KEY_ID + " integer primary key," +
                        KEY_TAG + " text, " +
                        KEY_COUNT + " int" +
                        ")"
            )
            Log.d("mLog", "Создали таблицу: " + TABLE_TAGS)
        }

        override fun onUpgrade(
            db: SQLiteDatabase,
            oldVersion: Int,
            newVersion: Int
        ) {
            db.execSQL("drop table if exists ${TABLE_TAGS}")
            onCreate(db)
        }
    }
    private var dbHelper: DBHelper? = null

    fun findTag(tag: String,context: Context?): Int{
        dbHelper = DBHelper(context)
        val database = dbHelper!!.writableDatabase
        var cursor = database.query(TABLE_TAGS, null,
            KEY_TAG +  "= \""+ tag +"\""
            , null, null, null, null);

        var idTag = -1

        if (cursor.moveToFirst()) {
            var indexTag = cursor.getColumnIndex(KEY_ID)
            do {
                idTag = cursor.getInt(indexTag)

            }while (cursor.moveToNext())
        }

        Log.d("mLog", "Ищем тег: "+ tag)
        Log.d("mLog", "idTag = "+idTag)

        return idTag
    }

    fun addNewTag(tag: String, context: Context?): Int{
        dbHelper = DBHelper(context)
        val database = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TAG, tag)
        contentValues.put(KEY_COUNT, 1)
        database.insert(TABLE_TAGS, null, contentValues)
        Log.d("mLog", "Добавили новый тег: " + tag)
        val idTag = findTag(tag,context)
        return idTag
    }

}

class RTAdapterDB(context: Context?){
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "RecipesDB"
        const val TABLE_RECIPESTAGS = "recipes_tags"
        const val KEY_ID = "_id"
        const val KEY_IDTAG = "idtags"
        const val KEY_IDRECIPE = "idrecipe"

    }
    class DBHelper internal constructor(context: Context?) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                "(" +
                        KEY_ID + " integer primary key, " +
                        KEY_IDRECIPE + " int, " +
                        KEY_IDTAG +" int, "+
                        " FOREIGN KEY (" + KEY_IDRECIPE + ") REFERENCES " + RecipesAdapterDB.TABLE_RECIPES +"("+ RecipesAdapterDB.KEY_ID +"), "+
                        "FOREIGN KEY (" + KEY_IDTAG + ") REFERENCES " + TagsAdapterDB.TABLE_TAGS +"("+ TagsAdapterDB.KEY_ID +")"+
                        ")"
            )

            Log.d("mLog", "Создали новую таблицу: " + TABLE_RECIPESTAGS)
        }

        override fun onUpgrade(
            db: SQLiteDatabase,
            oldVersion: Int,
            newVersion: Int
        ) {
            db.execSQL("drop table if exists ${TABLE_RECIPESTAGS}")
            onCreate(db)
        }
    }
    private var dbHelper: DBHelper? = null
    fun setDepend(idRecipe: Int, idTag: Int, context: Context?): Boolean{
        dbHelper = DBHelper(context)
        val database = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_IDRECIPE,idRecipe)
        contentValues.put(KEY_IDTAG,idTag)
        database.insert(TABLE_RECIPESTAGS, null, contentValues);

        return true
    }
}