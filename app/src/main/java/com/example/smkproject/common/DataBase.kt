package com.example.smkproject.common

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smkproject.models.*

@Database(entities = arrayOf(
    Recipe::class,
    Tag::class,
    RecipeTag::class,
    Ingredient::class,
    RecipeIngredient::class), version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun tagDao(): TagDao
    abstract fun recipeTagDao(): RecipeTagDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun recipeIngredientDao(): RecipeIngredientDao

    companion object {
        @Volatile
        private var INSTANCE: DataBase? = null

        fun getDatabase(context: Context): DataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBase::class.java,
                    "database.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


