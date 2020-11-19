package com.example.smkproject.views



import android.os.Bundle
import com.example.smkproject.common.ViewNavigator


interface MainView : ViewNavigator {
    fun navigateToRecipeFragment(bundle: Bundle)
    fun navigateToEditRecipeFragment()
}
