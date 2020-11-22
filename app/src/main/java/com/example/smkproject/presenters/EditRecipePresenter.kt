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
        MainRepository.onSaveRecipe = {saveRecipe()}
    }
    var ingredients = arrayListOf<Ingredient>()

    fun saveRecipe(): Recipe? {
        view.saveIngredient()
        if (title.length>0 && describ.length>0 && tags.length>0 && ingredients.count() > 0) {
            Log.d("mLog", "ТУТ")
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val date = dateFormat.format(currentDate)
            val recipe = Recipe(-1, title, describ, date, tags, ingredients)
            return recipe
        }
        return null
    }

    fun setIngredient(_title: String, _amoung: Double, _unit: String): Boolean {
        if (_title.length>0 && _amoung > 0 && _unit.length>0) {
            ingredients.add(Ingredient(_title, _amoung, _unit))
            return true
        }
        return false
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