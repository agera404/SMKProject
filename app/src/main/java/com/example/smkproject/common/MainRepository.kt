package com.example.smkproject.common

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Room
import com.example.smkproject.models.Recipe
import com.example.smkproject.models.RecipeIngredient
import com.example.smkproject.models.RecipeTag
import com.example.smkproject.models.Tag
import kotlinx.coroutines.runBlocking


object MainRepository{

    var db: DataBase? = null
    var context:Context? = null
        set(value) {
        field = value
        db = DataBase.getDatabase(field!!)
    }
    init {


    }

    val ID_TAG_ALLRECIPE: Long = -1

    var selectedRecipe: Recipe? = null
        set(value) {field = value}
        get() = field
    var currentIdTag: Long = ID_TAG_ALLRECIPE // когда id < 0 показывает все рецепты

    var updateMenu: (()->Unit)? = null

    var allRecipe: List<Recipe>? = null
    var allTags: List<Tag>? = null

    private fun updateArrays() {
        runBlocking {
            loadRecipes()
            loadTags()
        }
    }

    suspend fun deleteRecipe(idRecipe: Long){
        db?.recipeDao()?.deleteRecipe(idRecipe)
        updateArrays()
    }

    suspend fun saveRecipe(recipe: Recipe) {
        db?.recipeDao()?.insert(recipe)
        updateArrays()
    }

    suspend fun getRecipesByTag(): List<Recipe> {
        if (currentIdTag == ID_TAG_ALLRECIPE) return allRecipe ?: arrayListOf<Recipe>()

        val recipeTagDao = db?.recipeTagDao()
        return recipeTagDao?.getRecipesByTag(idTag = currentIdTag) ?: arrayListOf<Recipe>()
    }

    suspend fun loadRecipes(): List<Recipe>? {
        val recipeDao = db?.recipeDao()
        allRecipe = recipeDao?.getAll()
        return allRecipe

    }

    suspend fun getAnyTag(): Tag?{
        val tagDao = db?.tagDao()
        var tag = tagDao?.getAnyTag()
        return tag
    }

    suspend fun loadTags():List<Tag>? {
        val tagDao = db?.tagDao()
        allTags = tagDao?.getAll()
        return allTags
    }

}