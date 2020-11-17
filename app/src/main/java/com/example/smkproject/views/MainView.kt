package com.example.smkproject.views


import androidx.fragment.app.Fragment
import com.example.smkproject.common.DBHelper
import com.example.smkproject.common.ViewNavigator
import com.example.smkproject.models.Recipe

interface MainView : ViewNavigator {
    fun showRecipe(recipes: ArrayList<Recipe>): Fragment
    fun getDBHelper(): DBHelper
}
