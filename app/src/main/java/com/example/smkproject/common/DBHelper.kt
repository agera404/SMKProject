package com.example.smkproject.common

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context?) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table " + TABLE_CONTACTS + "(" + KEY_ID
                    + " integer primary key," + KEY_TITLE + " text," + KEY_DESCRIB + " text," + KEY_DATETIME + " text " + ")"
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("drop table if exists $TABLE_CONTACTS")
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "RecipesDB"
        const val TABLE_CONTACTS = "recipes"
        const val KEY_ID = "_id"
        const val KEY_TITLE = "title"
        const val KEY_DESCRIB = "describ"
        const val KEY_DATETIME = "dateTime"
    }
}