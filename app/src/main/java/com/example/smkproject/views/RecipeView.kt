package com.example.smkproject.views

import androidx.lifecycle.LifecycleOwner

interface RecipeView {
    fun setField(title: String, describ: String, ingredient: String, tag: String)
}