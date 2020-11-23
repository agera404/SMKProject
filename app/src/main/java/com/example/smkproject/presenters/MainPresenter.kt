package com.example.smkproject.presenters

import android.os.Bundle
import com.example.smkproject.common.DBHelper
import com.example.smkproject.common.MainRepository
import com.example.smkproject.views.MainView

class MainPresenter (var view: MainView, dbHelper: DBHelper){


    init {
        MainRepository.dbHelper = dbHelper
        MainRepository.onBack = {
            view.updateMenu()
            showRecipes(MainRepository.ID_TAG_ALLRECIPE)
        }
        MainRepository.openRecipe = {
            view.navigateTo(view.RECIPE_FRAGMENT)
        }
        MainRepository.onEditIngredients = {
            view.navigateTo(view.EDITINGREDIENT_FRAGMENT)
        }
        MainRepository.onEditRecipe = {
            view.navigateTo(view.EDITRECIPE_FRAGMENT)
        }
    }



    fun addNewRecipe(){
        view.navigateTo(view.EDITRECIPE_FRAGMENT)
    }
    fun showRecipes(idTag: Long){
        MainRepository.currentIdTag = idTag
        view.navigateTo(view.RECIPES_FRAGMENT)
    }




}