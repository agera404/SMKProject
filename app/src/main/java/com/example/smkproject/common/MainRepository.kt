package com.example.smkproject.common

import android.os.Parcelable
import android.os.ProxyFileDescriptorCallback
import com.example.smkproject.models.Recipe
import com.example.smkproject.models.Tag
import kotlinx.android.parcel.Parcelize


object MainRepository {
    var dbHelper: DBHelper? = null
        get() = field
    set(value) {
        field = value
        allRecipes = loadRecipes()
        tags = loadTags()
    }
    lateinit var allRecipes: ArrayList<Recipe>
    lateinit var tags: ArrayList <Tag>

    val ID_TAG_ALLRECIPE: Long = -1

    var onBack: (()-> Unit)? = null

    var onSaveRecipe: (() -> Recipe?)? = null
    get() = field
    set(value) {
        field = field
    }

    var currentIdTag: Long = ID_TAG_ALLRECIPE // когда id < 0 показывает все рецепты


    private fun updateArrays(){
        allRecipes = loadRecipes()
        tags = loadTags()
    }
    fun saveRecipe(){
        var recipe = onSaveRecipe?.invoke()
        var adapterDB = RecipesDB(dbHelper)
        adapterDB.saveRecipe(recipe!!)
        tags = loadTags()
        allRecipes = loadRecipes()
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