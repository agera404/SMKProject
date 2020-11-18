package com.example.smkproject.views

import android.content.Intent
import com.example.smkproject.common.DBHelper
import com.example.smkproject.common.ViewNavigator

interface EditRecipeView{
    fun saveRecipe()
    fun getDBHelper(): DBHelper
}