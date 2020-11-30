package com.example.smkproject.views

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

interface RecipesView{
    fun setRecipeOnLayout(id: Long, title: String, describ: String, tags: String)
}