package com.example.smkproject.presenters


import android.os.Bundle
import android.util.Log
import com.example.smkproject.common.DBHelper
import com.example.smkproject.common.MainRepository
import com.example.smkproject.views.RecipesView


class RecipesPresenter(var view: RecipesView){



    fun showRecipes(){
        var recipes = MainRepository.getRecipesByTag(MainRepository.currentIdTag)
            for (recipe in recipes){

                view.setRecipeOnLayout(recipe.title, recipe.describ, recipe.tags)
            }

    }

}