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
    fun getTags(): List<Tag>{
        var tags: List<Tag> = arrayListOf()
        runBlocking {
            tags = MainRepository.loadTags()!!
        }
        return tags
    }


}