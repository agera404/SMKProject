package com.example.smkproject.presenters

import androidx.lifecycle.Lifecycle
import com.example.smkproject.common.MainRepository
import com.example.smkproject.views.RecipeView

class RecipePresenter(var view: RecipeView){

    init {

    }
    var recipe = MainRepository.selectedRecipe
    fun loadRecipe() {
        view.setField(recipe!!.title, recipe!!.describe, recipe!!.ingredients, recipe!!.tags)
    }

}