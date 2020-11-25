package com.example.smkproject.presenters

import android.os.Bundle
import com.example.smkproject.common.DBHelper
import com.example.smkproject.common.MainRepository
import com.example.smkproject.views.MainView

class MainPresenter (var view: MainView, dbHelper: DBHelper){


    init {
        MainRepository.dbHelper = dbHelper
        MainRepository.updateMenu = { view.updateMenu() }
    }




    fun showRecipes(idTag: Long){
        MainRepository.currentIdTag = idTag
    }




}