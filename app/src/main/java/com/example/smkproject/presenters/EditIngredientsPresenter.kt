package com.example.smkproject.presenters

import android.util.Log
import com.example.smkproject.common.MainRepository
import com.example.smkproject.models.Ingredient
import com.example.smkproject.views.EditIngredientsView

class EditIngredientsPresenter(var view: EditIngredientsView) {
    var ingredients: ArrayList<Ingredient> = arrayListOf()
    init {
        if (MainRepository.selectedRecipe != null){
            ingredients = MainRepository.selectedRecipe!!.ingredients
        }
        Log.d("mLog", "EditIngredientsPresenter ingredients COUNT: ${ingredients.count()}")
    }
    fun setIngredients(){
        for (ingr in ingredients){
            view.loadIngredient(ingr.ingredient, ingr.amount, ingr.unit)
        }
    }
    fun onDestroy(){
        MainRepository.selectedRecipe!!.ingredients = ingredients
        MainRepository.selectedRecipe!!.ingrToStr()
        ingredients = arrayListOf()
    }
}