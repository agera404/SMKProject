package com.example.smkproject.views

import android.view.View

interface MainView : ViewNavigator{
    fun onAddNewRecipe(view: View)
}
interface ViewNavigator {
    fun navigateTo(target: Class<*>)
}