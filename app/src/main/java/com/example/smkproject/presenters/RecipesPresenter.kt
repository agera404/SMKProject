package com.example.smkproject.presenters

import androidx.annotation.CallSuper
import androidx.lifecycle.*
import com.example.smkproject.common.MainRepository
import com.example.smkproject.models.Recipe
import com.example.smkproject.views.RecipesView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter: CoroutineScope, ViewModel(), LifecycleObserver {

    private var job: Job = Job()
    override  val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
}

class RecipesPresenter(var view: RecipesView): BasePresenter(){
    private var viewLifecycle: Lifecycle? = null
    lateinit var recipes: List<Recipe>
    var allRecipesLD: LiveData<List<Recipe>>? = null

    init{
        this.viewLifecycle = view.viewLifecycle?.lifecycle
        viewLifecycle?.addObserver(this)
        launch {
            allRecipesLD?.observe(view.viewLifecycle!!, Observer <List<Recipe>>{
                    allRecipes -> recipes
            })
            allRecipesLD = MainRepository.loadRecipes()

        }

    }

    fun selectRecipe(idRecipe: Long){
        for (recipe in recipes){
            if (recipe.id == idRecipe) MainRepository.selectedRecipe = recipe
        }
    }

    fun deleteRecipe(idRecipe: Long){
        launch {
            MainRepository.deleteRecipe(idRecipe)
        }
    }

    fun showRecipes(){
        launch {
            recipes = MainRepository.getRecipesByTag(MainRepository.currentIdTag)
            for (recipe in recipes){
                view.setRecipeOnLayout(recipe.id!!, recipe.title, recipe.describe, recipe.tags)
            }
        }


    }
}