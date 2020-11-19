package com.example.smkproject.presenters

import android.os.Bundle
import com.example.smkproject.common.DBHelper
import com.example.smkproject.common.MainRepository
import com.example.smkproject.views.MainView

class MainPresenter (var view: MainView, _dbHelper: DBHelper){
    var dbHelper = _dbHelper
    var repository =  MainRepository(dbHelper)

    fun addNewRecipe(){
        view.navigateToEditRecipeFragment()
    }
    fun showRecipes(idTag: Long){
        var bundle = Bundle()
        bundle.putLong("idTag", idTag)
        view.navigateToRecipeFragment(bundle)
    }



}