package com.example.smkproject.common

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.smkproject.models.Recipe


class MainRepository {
    var dbHelper: DBHelper? = null

    fun saveRecipeInDb(context: Context, recipe: Recipe) {
        dbHelper = DBHelper(context)
        val database = dbHelper!!.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(DBHelper.KEY_TITLE, recipe.title);
        contentValues.put(DBHelper.KEY_DESCRIB, recipe.describ);
        contentValues.put(DBHelper.KEY_DATETIME, recipe.dateTime);


        database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);


    }
    fun loadRecipes(context: Context): ArrayList<Recipe>{
        var recipes: ArrayList<Recipe> = ArrayList()
        dbHelper = DBHelper(context)
        val database = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        val cursor: Cursor =
            database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(DBHelper.KEY_ID)
            val titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE)
            val describIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIB)
            val dateTimeIndex = cursor.getColumnIndex(DBHelper.KEY_DATETIME)

            do {
                recipes.add(Recipe(
                     cursor.getInt(titleIndex).toString(),
                     cursor.getInt(describIndex).toString(),
                     cursor.getInt(dateTimeIndex).toString()))

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
            database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null)
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