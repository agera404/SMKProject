package com.example.smkproject.presenters

import android.content.Intent
import android.util.Log
import com.example.smkproject.MainActivity
import com.example.smkproject.common.RecipesAdapterDB
import com.example.smkproject.models.Recipe
import com.example.smkproject.views.AddRecipeView
import java.text.SimpleDateFormat
import java.util.*

class AddRecipePresenter (var view: AddRecipeView){

    fun saveRecipe(title: String, describ: String, tags:String){
        val currentDate = Date()
        val dateFormat = SimpleDateFormat(
            "dd-MM-yyyy HH:mm:ss"
        )
        val date = dateFormat.format(currentDate)
        val recipe = Recipe(title, describ, date, tags)
        val adapterDB = RecipesAdapterDB(view.getDBHelper())
        Log.d("mLog", "Пытаемся сохранить рецет")
        adapterDB.saveRecipe(recipe)


    }
    fun onBack(){
        val adapterDB = RecipesAdapterDB(view.getDBHelper())
        adapterDB.loadRecipesInLog()
        view.setResult(view.intent())
        view.navigateTo(MainActivity::class.java)
    }


}