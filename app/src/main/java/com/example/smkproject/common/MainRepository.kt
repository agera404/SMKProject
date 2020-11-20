package com.example.smkproject.common

import com.example.smkproject.models.Recipe
import com.example.smkproject.models.Tag

class MainRepository(_dbHelper: DBHelper) {
    var dbHelper = _dbHelper
    var allRecipes = loadRecipes()
    var tags = loadTags()
    init{

    }
    private fun updateArrays(){
        allRecipes = loadRecipes()
        tags = loadTags()
    }
    fun saveRecipe(recipe:Recipe){
        var adapterDB = RecipesDB(dbHelper)
        adapterDB.saveRecipe(recipe)
    }
    fun getRecipesByTag(idTag: Long): ArrayList<Recipe>{

        for (tag in tags){
            if (tag.id == idTag) return tag.recipes
        }
        return allRecipes
    }
    fun loadRecipes(): ArrayList<Recipe>{
        val adapterDB = RecipesDB(dbHelper)
        return adapterDB.loadRecipes()
    }
    fun loadTags(): ArrayList<Tag>{
        val adapterDB = TagsAdapterDB(dbHelper)
        return adapterDB.loadTags()
    }
}