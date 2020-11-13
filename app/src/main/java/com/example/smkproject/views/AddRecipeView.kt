package com.example.smkproject.views

import android.content.Context
import android.content.Intent
import com.example.smkproject.common.ViewContext
import com.example.smkproject.common.ViewNavigator

interface AddRecipeView : ViewNavigator, ViewContext {

    fun setResult(intent: Intent)
    fun saveRecipe()
}