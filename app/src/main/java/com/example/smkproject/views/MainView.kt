package com.example.smkproject.views

import android.view.View
import androidx.fragment.app.Fragment
import com.example.smkproject.common.ViewContext
import com.example.smkproject.common.ViewNavigator

interface MainView : ViewNavigator, ViewContext {
    fun showRecipe(): Fragment

}
