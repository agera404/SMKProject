package com.example.smkproject.common

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Room
import com.example.smkproject.models.Recipe
import com.example.smkproject.models.RecipeTag
import com.example.smkproject.models.Tag


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
        var idRecipe: Long? = db?.recipeDao()?.insert(recipe)
        var tags = recipe.convertTags()
        for (tag in tags){
            var idTag = db?.tagDao()?.insert(tag)
            if (idTag != null && idRecipe!=null){
                db?.recipeTagDao()?.insert(RecipeTag(id=null, recipe_id = idRecipe, tag_id = idTag))
            }
        }
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

    fun getAnyTag(): Tag?{
        val tagDao = db?.tagDao()
        var tag = tagDao?.getAnyTag()
        Log.d("mLog", "tag = ${tag?.tag}")
        return tag
    }

    fun loadTags(): LiveData<List<Tag>>? {
        val tagDao = db?.tagDao()
        return tagDao?.getAll()
    }

}