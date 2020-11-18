package com.example.smkproject.presenters

import android.util.Log
import com.example.smkproject.MainActivity
import com.example.smkproject.common.RecipesAdapterDB
import com.example.smkproject.models.Recipe
import com.example.smkproject.views.EditRecipeView
import java.text.SimpleDateFormat
import java.util.*

class EditRecipePresenter (var view: EditRecipeView){

    fun saveRecipe(title: String, describ: String, tags:String){
        val currentDate = Date()
        val dateFormat = SimpleDateFormat(
            "dd-MM-yyyy HH:mm:ss"
        )
        val date = dateFormat.format(currentDate)
        val recipe = Recipe(-1,title, describ, date, tags)
        val adapterDB = RecipesAdapterDB(view.getDBHelper())
        Log.d("mLog", "Пытаемся сохранить рецет")
        adapterDB.saveRecipe(recipe)


    }
    fun sortTags(tags: String): String{
        var array = tags.split(",").toTypedArray()
        array = array.sortedArray()
        var newTags = ""
        for (t in  array){
            newTags=t+", "
        }
        return newTags.dropLast(2)
    }
    fun onBack(){
        val adapterDB = RecipesAdapterDB(view.getDBHelper())
        adapterDB.loadRecipesInLog()

    }


}