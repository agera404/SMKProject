package com.example.smkproject.views

import androidx.lifecycle.Lifecycle


interface MainView{
    fun createMenu()
    fun updateMenu()
    fun updateToolbarTitle(title: String)
}
