package com.example.smkproject.presenters


import android.util.Log
import com.example.smkproject.common.DBHelper
import com.example.smkproject.common.MainRepository
import com.example.smkproject.views.RecipesView


class RecipesPresenter(var view: RecipesView, _dbHelper:DBHelper){
    var dbHelper = _dbHelper
    var repository =  MainRepository(dbHelper)
    var idTag: Long = -1
    fun setIdTag(id: Long?){
        if (id != null) {
            idTag=id
        }
    }
    fun showRecipes(){
        var recipes = repository.getRecipesByTag(idTag)
        if(recipes!=null){
            for (recipe in recipes){
                view.setRecipeOnLayout(recipe)
            }
        } else Log.d("mLog", "recipeS is null")
    }

}