package com.example.smkproject.common

import android.os.Parcelable
import android.os.ProxyFileDescriptorCallback
import com.example.smkproject.models.Ingredient
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
    lateinit var tags: ArrayList<Tag>

    val ID_TAG_ALLRECIPE: Long = -1

    var onBack: (() -> Unit)? = null
    var openRecipe: (() -> Unit)? = null
    var onEditIngredients: (() -> Unit)? = null
    var onEditRecipe: (() -> Unit)? = null
    var setIngredient:((Ingredient) -> Unit)? = null
    var currentIngredient: Ingredient? = null
    get() = field
    set(value) {
        if(value != null){
            field = value
            setIngredient?.invoke(field!!)
        }
    }

    var selectedRecipe: Recipe? = null
    var currentIdTag: Long = ID_TAG_ALLRECIPE // когда id < 0 показывает все рецепты


    private fun updateArrays() {
        allRecipes = loadRecipes()
        tags = loadTags()
    }

    fun saveRecipe(recipe: Recipe) {
        var adapterDB = RecipesDB(dbHelper)
        adapterDB.saveRecipe(recipe!!)
        tags = loadTags()
        allRecipes = loadRecipes()
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
    }

    fun loadTags(): ArrayList<Tag> {
        val adapterDB = TagsAdapterDB(dbHelper)
        return adapterDB.loadTags()
    }
}