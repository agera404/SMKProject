package com.example.smkproject.presenters

import android.util.Log
import com.example.smkproject.common.MainRepository
import com.example.smkproject.models.Tag
import com.example.smkproject.views.MainView
import kotlinx.coroutines.*


class MainPresenter (var view: MainView): BasePresenter() {

    init {
        runBlocking {
            MainRepository.loadTags()
            MainRepository.loadRecipes()
        }

        MainRepository.updateMenu = { view.updateMenu() }

    }

    fun showRecipes(idTag: Long){
        MainRepository.currentIdTag = idTag
    }

}