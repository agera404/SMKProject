package com.example.smkproject.views

import android.content.Context
import com.example.smkproject.common.ViewNavigator

interface AddRecipeView : ViewNavigator {
    fun getContex(): Context

    fun isCheckRecipe(): Boolean
}