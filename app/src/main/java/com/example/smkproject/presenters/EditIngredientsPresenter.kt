package com.example.smkproject.presenters

import android.util.Log
import com.example.smkproject.common.MainRepository
import com.example.smkproject.models.Ingredient
import com.example.smkproject.views.EditIngredientsView

class EditIngredientsPresenter(var view: EditIngredientsView) {
    var ingredients: ArrayList<Ingredient> = arrayListOf()
    init {
        if (MainRepository.selectedRecipe != null){
            ingredients = MainRepository.selectedRecipe!!.convertIngredients()
        }
    }
    fun setIngredients(){
        for (ingr in ingredients){
            view.loadIngredient(ingr.title, ingr.amount, ingr.unit)
        }
    }
    fun onDestroy(){
        MainRepository.selectedRecipe?.convertIngredients(ingredients)
        ingredients = arrayListOf()
    }
}