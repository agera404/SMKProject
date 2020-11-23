package com.example.smkproject.presenters

import com.example.smkproject.common.MainRepository
import com.example.smkproject.views.RecipeView

class RecipePresenter(var view: RecipeView) {
    var recipe = MainRepository.selectedRecipe
    fun loadRecipe() {
        view.setField(recipe!!.title, recipe!!.describ, recipe!!.stringIngredient, recipe!!.tags)
    }

}