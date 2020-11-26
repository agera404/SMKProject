package com.example.smkproject.presenters

import android.util.Log
import com.example.smkproject.common.MainRepository
import com.example.smkproject.models.Ingredient
import com.example.smkproject.models.Recipe
import com.example.smkproject.views.EditRecipeView
import java.text.SimpleDateFormat
import java.util.*

class EditRecipePresenter(var view: EditRecipeView) {

    var recipe: Recipe? = null

    init {
        if (MainRepository.selectedRecipe != null) {
            recipe = MainRepository.selectedRecipe
        }else{
            recipe = Recipe(null, "","","","","")
        }

    }

    fun isRecipeNotNull(): Boolean {
        if (recipe != null) {
            return true
        }
        return false
    }

    fun saveRecipe() {
        recipe?.ingredients  = MainRepository.selectedRecipe?.ingredients!!
       //recipe?.stringIngredient = MainRepository.selectedRecipe!!.stringIngredient

        if (recipe?.title?.length!! > 0 && recipe?.describe?.length!! > 0 && recipe?.tags?.length!! > 0 && recipe?.ingredients!!.count() > 0) {
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val date = dateFormat.format(currentDate)

            MainRepository.saveRecipe(recipe!!)
        }
    }

    fun sortTags(tags: String): String {
        var array = tags.split(",").toTypedArray()
        array = array.sortedArray()
        var newTags = ""
        for (t in array) {
            newTags = t + ", "
        }
        return newTags.dropLast(2)
    }


}