package com.example.smkproject.presenters

import android.util.Log
import com.example.smkproject.common.MainRepository
import com.example.smkproject.models.Ingredient
import com.example.smkproject.models.Recipe
import com.example.smkproject.views.EditRecipeView
import java.text.SimpleDateFormat
import java.util.*

class EditRecipePresenter(var view: EditRecipeView) {

    var title = ""
    var describ = ""
    var tags = ""
    init {
        MainRepository.setIngredient = { ing: Ingredient->
            this.setIngredient(ing)
        }
    }
    var ingredients = arrayListOf<Ingredient>()

    fun saveRecipe(){

        if (title.length>0 && describ.length>0 && tags.length>0 && ingredients.count() > 0) {
            Log.d("mLog", "ТУТ")
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val date = dateFormat.format(currentDate)
            Log.d("mLog", "$title $describ $tags")
            val recipe = Recipe(-1, title, describ, date, tags, ingredients)
            MainRepository.saveRecipe(recipe)
        }
    }

    fun setIngredient(ingredient: Ingredient){
        if (ingredient.ingredient.length>0 && ingredient.amount > 0 && ingredient.unit.length>0) {
            ingredients.add(ingredient)
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