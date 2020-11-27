package com.example.smkproject.common

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import androidx.room.Room
import com.example.smkproject.models.Recipe
import com.example.smkproject.models.Tag


object MainRepository{

    var db: DataBase? = null
    var context:Context? = null
        set(value) {
        field = value
        db = Room.databaseBuilder(
            field!!,
            DataBase::class.java, "database"
        ).build()
    }
    init {


    }

    val ID_TAG_ALLRECIPE: Long = -1

    var selectedRecipe: Recipe? = null
        set(value) {field = value}
    var currentIdTag: Long = ID_TAG_ALLRECIPE // когда id < 0 показывает все рецепты

    var updateMenu: (()->Unit)? = null


    private fun updateArrays() {
        updateMenu?.invoke()
    }

    fun deleteRecipe(idRecipe: Long){

        val recipeDao = db?.recipeDao()
        val recipe = recipeDao?.getById(idRecipe)
        recipeDao?.delete(recipe)
        updateArrays()
    }

    fun saveRecipe(recipe: Recipe) {
        val recipeDao = db?.recipeDao()
        recipeDao?.insert(recipe)
        updateArrays()

    }

    fun getRecipesByTag(idTag: Long): List<Recipe> {

        val recipeTagDao = db?.recipeTagDao()
        return recipeTagDao?.getRecipesByTag(idTag = idTag) ?: arrayListOf<Recipe>()
    }

    fun loadRecipes(): LiveData<List<Recipe>>? {
        val recipeDao = db?.recipeDao()
        val recipes = recipeDao?.getAll()
        return recipes

    }


    fun loadTags(): LiveData<List<Tag>>? {
        val tagDao = db?.tagDao()
        return tagDao?.getAll()
    }

}