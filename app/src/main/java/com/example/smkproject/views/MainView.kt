package com.example.smkproject.views


import android.view.SubMenu
import com.example.smkproject.common.ViewNavigator


interface MainView : ViewNavigator {
    fun navigateToRecipeFragment()
    fun navigateToEditRecipeFragment()
    fun createMenu()
    fun updateMenu()
}
