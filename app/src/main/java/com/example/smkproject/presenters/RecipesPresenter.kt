package com.example.smkproject.presenters


import android.util.Log
import com.example.smkproject.models.Recipe
import com.example.smkproject.views.RecipesView
import kotlinx.android.synthetic.main.fragment_recipes.*

class RecipesPresenter(var view: RecipesView){
    var recipes: ArrayList<Recipe> = arrayListOf()
    fun showRecipes(){
        if(recipes!=null){
            for (recipe in recipes){
                if(recipe == null)Log.d("mLog", "recipE is null")
                else view.setRecipeOnLayout(recipe)
            }
        } else Log.d("mLog", "recipeS is null")
    }

}