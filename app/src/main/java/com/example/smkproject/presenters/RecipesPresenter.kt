package com.example.smkproject.presenters

import androidx.annotation.CallSuper
import androidx.lifecycle.*
import com.example.smkproject.common.MainRepository
import com.example.smkproject.models.Recipe
import com.example.smkproject.views.RecipesView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter: CoroutineScope{

    private var job: Job = Job()
    override  val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}

class RecipesPresenter(var view: RecipesView): BasePresenter(){
    var recipes: List<Recipe>? = arrayListOf()


    fun selectRecipe(idRecipe: Long){
        for (recipe in recipes!!){
            if (recipe.id == idRecipe) MainRepository.selectedRecipe = recipe
        }
    }

    fun deleteRecipe(idRecipe: Long){
        launch {
            MainRepository.deleteRecipe(idRecipe)
        }
    }

    fun showRecipes(){
        runBlocking {
            recipes = MainRepository.getRecipesByTag()
        }
        for (recipe in recipes!!){
            view.setRecipeOnLayout(recipe.id!!, recipe.title, recipe.describe, recipe.tags)
        }

    }
}