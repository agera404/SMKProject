package com.example.smkproject.presenters

import com.example.smkproject.common.MainRepository
import com.example.smkproject.views.MainView

class MainPresenter (var view: MainView){


    init {

        MainRepository.updateMenu = { view.updateMenu() }
    }




    fun showRecipes(idTag: Long){
        MainRepository.currentIdTag = idTag
    }




}