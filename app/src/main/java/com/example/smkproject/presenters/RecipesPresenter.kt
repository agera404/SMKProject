package com.example.smkproject.presenters

import com.example.smkproject.common.MainRepository
import com.example.smkproject.views.RecipesView


class RecipesPresenter(var view: RecipesView){

    fun selectRecipe(idRecipe: Long){
        MainRepository.setSelectedRecipe(idRecipe)
    }

    fun showRecipes(){
        var recipes = MainRepository.getRecipesByTag(MainRepository.currentIdTag)
            for (recipe in recipes){
                view.setRecipeOnLayout(recipe.id!!, recipe.title, recipe.describe, recipe.tags)
            }

    }

}