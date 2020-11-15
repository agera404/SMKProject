package com.example.smkproject.common

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.smkproject.models.Recipe

class RecipesAdapterDB(context: Context?)
{
    private class DBHelper internal constructor(context: Context?) :
        SQLiteOpenHelper(context, DBHelper.DATABASE_NAME, null, DBHelper.DATABASE_VERSION) {
        companion object {
            const val DATABASE_VERSION = 1
            const val DATABASE_NAME = "RecipesDB"
            const val TABLE_RECIPES = "recipes"
            const val KEY_ID = "_id"
            const val KEY_TITLE = "title"
            const val KEY_DESCRIB = "describ"
            const val KEY_DATETIME = "dateTime"
            const val KEY_TAGS = "tags"

        }
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                "create table " + DBHelper.TABLE_RECIPES + "(" + DBHelper.KEY_ID
                        + " integer primary key," + DBHelper.KEY_TITLE + " text," + DBHelper.KEY_DESCRIB + " text," + DBHelper.KEY_DATETIME + " text " + DBHelper.KEY_TAGS+" int"+ ")"
            )
        }

        override fun onUpgrade(
            db: SQLiteDatabase,
            oldVersion: Int,
            newVersion: Int
        ) {
            db.execSQL("drop table if exists ${DBHelper.TABLE_RECIPES}")
            onCreate(db)
        }
    }
    private var dbHelper: DBHelper? = null


    fun saveRecipe(context: Context, recipe: Recipe) {
        dbHelper = DBHelper(context)
        val database = dbHelper!!.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(DBHelper.KEY_TITLE, recipe.title);
        contentValues.put(DBHelper.KEY_DESCRIB, recipe.describ);
        contentValues.put(DBHelper.KEY_DATETIME, recipe.dateTime);
        /*Сделать запрос к БД и получить id последней записи*/
        val recipeTagDB = RecipesTagsAdapterDB(context)
        val tagsDB = TagsAdapterDB(context)
        for (tag in recipe.tags){
            if(tagsDB.isTagInDB(tag)){
                recipeTagDB.setDepend()
                //tagsDB.getTagID(id рецепта и тега)
            }
        }

        database.insert(DBHelper.TABLE_RECIPES, null, contentValues);


    }
    fun loadRecipes(context: Context): ArrayList<Recipe>{
        var recipes: ArrayList<Recipe> = ArrayList()
        dbHelper = DBHelper(context)
        val database = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        val cursor: Cursor =
            database.query(DBHelper.TABLE_RECIPES, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(DBHelper.KEY_ID)
            val titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE)
            val describIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIB)
            val dateTimeIndex = cursor.getColumnIndex(DBHelper.KEY_DATETIME)

            do {
                recipes.add(
                    Recipe(
                    cursor.getString(titleIndex).toString(),
                    cursor.getString(describIndex).toString(),
                    cursor.getString(dateTimeIndex).toString())
                )

            } while (cursor.moveToNext())
        } else Log.d("mLog", "0 rows")

        cursor.close()

        return recipes
    }
    fun loadRecipesInLog(context: Context){
        dbHelper = DBHelper(context)
        val database = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        val cursor: Cursor =
            database.query(DBHelper.TABLE_RECIPES, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(DBHelper.KEY_ID)
            val titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE)
            val describIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIB)
            val dateTimeIndex = cursor.getColumnIndex(DBHelper.KEY_DATETIME)

            do {
                Log.d(
                    "mLog", "ID = " + cursor.getInt(idIndex) +
                            ", title = " + cursor.getString(titleIndex) +
                            ", descrip = " + cursor.getString(describIndex) +
                            ", dateTime = " + cursor.getString(dateTimeIndex)
                )
            } while (cursor.moveToNext())
        } else Log.d("mLog", "0 rows")

        cursor.close()
    }

}

class TagsAdapterDB(context: Context?){
    private class DBHelper internal constructor(context: Context?) :
        SQLiteOpenHelper(context, DBHelper.DATABASE_NAME, null, DBHelper.DATABASE_VERSION) {
        companion object {
            const val DATABASE_VERSION = 1
            const val DATABASE_NAME = "TagsDB"
            const val TABLE_TAGS = "tags"
            const val KEY_ID = "_id"
            const val KEY_TAG = "tag"
        }
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                "create table " + DBHelper.TABLE_TAGS + "(" + DBHelper.KEY_ID
                        + " integer primary key," + DBHelper.KEY_TAG + " text" + ")"
            )
        }

        override fun onUpgrade(
            db: SQLiteDatabase,
            oldVersion: Int,
            newVersion: Int
        ) {
            db.execSQL("drop table if exists ${DBHelper.TABLE_TAGS}")
            onCreate(db)
        }
    }
    private var dbHelper: DBHelper? = null
    fun isTagInDB(tag: String): Boolean{
        return false
    }
    fun getTagID(tag: String): Int{
        return 1
    }
}

class RecipesTagsAdapterDB(context: Context?){
    private class DBHelper internal constructor(context: Context?) :
        SQLiteOpenHelper(context, DBHelper.DATABASE_NAME, null, DBHelper.DATABASE_VERSION) {
        companion object {
            const val DATABASE_VERSION = 1
            const val DATABASE_NAME = "RecipesTagsDB"
            const val TABLE_RECIPESTAGS = "recipes_tags"
            const val KEY_ID = "_id"
            const val KEY_TAG = "tags"
            const val KEY_RECIPE = "recipe"

        }
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                "create table " + DBHelper.TABLE_RECIPESTAGS + "(" + DBHelper.KEY_ID
                        + " integer primary key," + DBHelper.KEY_TAG + " int" + KEY_RECIPE+" int"+")"
            )
        }

        override fun onUpgrade(
            db: SQLiteDatabase,
            oldVersion: Int,
            newVersion: Int
        ) {
            db.execSQL("drop table if exists ${DBHelper.TABLE_RECIPESTAGS}")
            onCreate(db)
        }
    }
    private var dbHelper: DBHelper? = null
    fun setDepend(idRecipe: Int, idTag: Int){

    }
}