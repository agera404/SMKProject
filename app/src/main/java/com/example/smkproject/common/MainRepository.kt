package com.example.smkproject.common

import android.app.Application
import androidx.room.Room
import com.example.smkproject.models.Recipe
import com.example.smkproject.models.Tag


object MainRepository: Application() {

    val db: DataBase? = null
    init {
        val db: DataBase = Room.databaseBuilder(
            applicationContext,
            DataBase::class.java, "database"
        ).build()
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

    fun deleteRecipe(idRecipe: Long){
        if (db != null){
            val recipeDao = db.recipeDao()
            val recipe = recipeDao.getById(idRecipe)
            recipeDao.delete(recipe)
            updateArrays()
        }
    }

    fun saveRecipe(recipe: Recipe) {
        if (db != null){
            val recipeDao = db.recipeDao()
            recipeDao.insert(recipe)
            updateArrays()
        }
    }

    fun getRecipesByTag(idTag: Long): ArrayList<Recipe> {
        if (db != null){
            val recipeTagDao = db.recipeTagDao()
            return recipeTagDao.getRecipesByTag(idTag = idTag) ?: arrayListOf<Recipe>()
        }
        return arrayListOf<Recipe>()
    }

    fun loadRecipes(): ArrayList<Recipe> {
        if (db != null){
            val recipeDao = db.recipeDao()
            return recipeDao.getAll() ?: arrayListOf<Recipe>()
        }
        return arrayListOf<Recipe>()
    }

    fun setSelectedRecipe(id: Long) {
        for (recipe in allRecipes) {
            if (recipe.id == id)
                selectedRecipe = recipe
        }
    }

    fun loadTags(): ArrayList<Tag> {
        if (db != null){
            val tagDao = db.tagDao()
            return tagDao.getAll() ?: arrayListOf<Tag>()
        }
        return arrayListOf<Tag>()
    }
}