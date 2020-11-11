package com.example.smkproject.views

import android.view.View

interface AddRecipeView : ViewNavigator{
    fun onBackHomeScreen(view: View)
    fun saveRecipe()
}