package com.example.smkproject.common

import android.util.Log
import com.example.smkproject.models.Recipe
import com.example.smkproject.models.Tag



object MainRepository {
    var dbHelper: DBHelper? = null
        get() = field
        set(value) {
            field = value
            allRecipes = loadRecipes()
            tags = loadTags()
        }
    lateinit var allRecipes: ArrayList<Recipe>
    lateinit var tags: ArrayList<Tag>

    val ID_TAG_ALLRECIPE: Long = -1

    var selectedRecipe: Recipe? = null
    var currentIdTag: Long = ID_TAG_ALLRECIPE // когда id < 0 показывает все рецепты

    var updateMenu: (()->Unit)? = null


    private fun updateArrays() {
        allRecipes = loadRecipes()
        tags = loadTags()
        updateMenu?.invoke()
    }

    fun saveRecipe(recipe: Recipe) {
        var adapterDB = RecipesDB(dbHelper)
        adapterDB.saveRecipe(recipe!!)
        updateArrays()
    }

    fun getRecipesByTag(idTag: Long): ArrayList<Recipe> {

        for (tag in tags) {
            if (tag.id == idTag) return tag.recipes
        }
        return allRecipes
    }

    fun loadRecipes(): ArrayList<Recipe> {
        val adapterDB = RecipesDB(dbHelper)
        return adapterDB.loadRecipes()
    }

    fun setSelectedRecipe(id: Long) {
        for (recipe in allRecipes) {
            if (recipe.id == id)
                selectedRecipe = recipe
        }
        Log.d("mLog", "Выбранный рецепт: ${selectedRecipe?.title} ingredients count :${selectedRecipe?.ingredients?.count()}")
    }

    fun loadTags(): ArrayList<Tag> {
        val adapterDB = TagsAdapterDB(dbHelper)
        return adapterDB.loadTags()
    }
}