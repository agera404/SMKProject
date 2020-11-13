package com.example.smkproject.presenters

import com.example.smkproject.AddRecipeActivity
import com.example.smkproject.common.MainRepository
import com.example.smkproject.models.Recipe
import com.example.smkproject.views.MainView

class MainPresenter (var view: MainView){

    fun goAddRecipeView(){
       view.navigateTo(AddRecipeActivity::class.java)
    }
    fun loadRecipe(): ArrayList<Recipe>{
        val mainRepository = MainRepository()
        return mainRepository.loadRecipes(view.getContex())
    }
}