package com.example.smkproject.views


import android.view.SubMenu


interface MainView{
    val RECIPES_FRAGMENT: Int
    val RECIPE_FRAGMENT: Int
    val EDITRECIPE_FRAGMENT: Int
    val EDITINGREDIENT_FRAGMENT: Int
    fun navigateTo(i: Int)
    fun createMenu()
    fun updateMenu()
}
