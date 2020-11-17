package com.example.smkproject.views

import android.content.Intent
import com.example.smkproject.common.DBHelper
import com.example.smkproject.common.ViewNavigator

interface AddRecipeView : ViewNavigator {

    fun setResult(intent: Intent)
    fun saveRecipe()
    fun getDBHelper(): DBHelper
    fun intent(): Intent
}