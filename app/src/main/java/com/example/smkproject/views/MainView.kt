package com.example.smkproject.views

import androidx.lifecycle.Lifecycle


interface MainView{
    var viewLifecycle: Lifecycle?
    fun createMenu()
    fun updateMenu()
}
