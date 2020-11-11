package com.example.smkproject.presenters

import com.example.smkproject.AddRecipeActivity
import com.example.smkproject.views.MainView

class MainPresenter (var view: MainView){

    fun onAddNewRecipe(){
        view.navigateTo(AddRecipeActivity::class.java)

    }
}