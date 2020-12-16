package com.example.smkproject.presenters

import androidx.lifecycle.Lifecycle
import com.example.smkproject.common.MainRepository
import com.example.smkproject.views.RecipeView

class RecipePresenter(var view: RecipeView){

    var recipe = MainRepository.selectedRecipe
    fun loadRecipe() {
        var list = recipe?.convertIngredients()
        var ingredients = String()
        if (list != null) {
            for (i in list){
                ingredients+="${list.indexOf(i)+1}) ${i.title}: ${i.amount} ${i.unit} \n"
            }
        }
        ingredients = ingredients.dropLast(1)
        view.setField(recipe!!.title, recipe!!.describe, ingredients, recipe!!.tags)
    }

}