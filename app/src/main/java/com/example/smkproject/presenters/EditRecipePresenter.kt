package com.example.smkproject.presenters

import android.util.Log
import androidx.lifecycle.Lifecycle
import com.example.smkproject.common.MainRepository
import com.example.smkproject.models.Ingredient
import com.example.smkproject.models.Recipe
import com.example.smkproject.views.EditRecipeView
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class EditRecipePresenter(var view: EditRecipeView): BasePresenter() {
    private var viewLifecycle: Lifecycle? = null
    var recipe: Recipe? = null

    init {
        if (MainRepository.selectedRecipe != null) {
            recipe = MainRepository.selectedRecipe
        }else{
            recipe = Recipe(title =  "",describe = "",dateTime = "",tags = "",ingredients = "")
        }


    }

    fun isRecipeNotNull(): Boolean {
        if (recipe != null) {
            return true
        }
        return false
    }

    fun saveRecipe() {
        //recipe?.ingredients  = MainRepository.selectedRecipe?.ingredients!!
       //recipe?.stringIngredient = MainRepository.selectedRecipe!!.stringIngredient

        //if (recipe?.title?.length!! > 0 && recipe?.describe?.length!! > 0 && recipe?.tags?.length!! > 0 && recipe?.ingredients!!.count() > 0) {
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val date = dateFormat.format(currentDate)
            launch {
                MainRepository.saveRecipe(recipe!!)
            }

        //}
    }

}