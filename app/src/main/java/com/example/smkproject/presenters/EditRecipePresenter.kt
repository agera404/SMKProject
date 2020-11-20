package com.example.smkproject.presenters

import android.util.Log
import com.example.smkproject.common.DBHelper
import com.example.smkproject.common.MainRepository
import com.example.smkproject.models.Ingredient
import com.example.smkproject.models.Recipe
import com.example.smkproject.views.EditRecipeView
import java.text.SimpleDateFormat
import java.util.*

class EditRecipePresenter(var view: EditRecipeView, _dbHelper: DBHelper) {
    var dbHelper = _dbHelper
    var repository = MainRepository(dbHelper)

    var ingredients = arrayListOf<Ingredient>()

    fun saveRecipe(title: String, describ: String, tags: String): Boolean {
        view.saveIngredient()
        if (title.length>0 && describ.length>0 && tags.length>0 && ingredients.count() > 0) {
            Log.d("mLog", "ТУТ")
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val date = dateFormat.format(currentDate)

            val recipe = Recipe(-1, title, describ, date, tags, ingredients)
            repository.saveRecipe(recipe)
            return true
        }
        return false
    }

    fun setIngredient(title: String, amoung: Double, unit: String): Boolean {
        if (title.length>0 && amoung > 0 && unit.length>0) {
            ingredients.add(Ingredient(title, amoung, unit))
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

    fun onBack() {
        view.navigateToFragment()
    }


}