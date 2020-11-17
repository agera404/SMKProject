package com.example.smkproject.presenters

import com.example.smkproject.AddRecipeActivity
import com.example.smkproject.common.RecipesAdapterDB
import com.example.smkproject.common.TagsAdapterDB
import com.example.smkproject.models.Recipe
import com.example.smkproject.models.Tag
import com.example.smkproject.views.MainView

class MainPresenter (var view: MainView){


    fun goAddRecipeView(){
       view.navigateTo(AddRecipeActivity::class.java)
    }
    fun loadRecipes(): ArrayList<Recipe>{
        val adapterDB = RecipesAdapterDB(view.getDBHelper())
        return adapterDB.loadRecipes()
    }
    fun loadTags(): ArrayList<Tag>{
        val adapterDB = TagsAdapterDB(view.getDBHelper())
        return adapterDB.loadTags()
    }

}