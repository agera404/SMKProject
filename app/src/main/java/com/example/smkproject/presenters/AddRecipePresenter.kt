package com.example.smkproject.presenters

import android.content.Context
import com.example.smkproject.AddRecipeActivity
import com.example.smkproject.MainActivity
import com.example.smkproject.common.MainRepository
import com.example.smkproject.models.Recipe
import com.example.smkproject.views.AddRecipeView
import java.util.*

class AddRecipePresenter (var view: AddRecipeView){


    fun saveRecipe(title: String, describ: String){
        val currentDate = Date()
        val recipe = Recipe(title, describ, currentDate)
        val mainRepository = MainRepository()
        mainRepository.saveRecipeInDb(view.getContex(), recipe)
    }
    fun onBack(){
        val mainRepository = MainRepository()
        mainRepository.loadRecipes(view.getContex())
        view.navigateTo(MainActivity::class.java)
    }


}