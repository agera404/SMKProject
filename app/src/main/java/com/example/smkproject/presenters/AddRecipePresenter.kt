package com.example.smkproject.presenters

import android.content.Intent
import android.provider.SyncStateContract
import com.example.smkproject.MainActivity
import com.example.smkproject.common.MainRepository
import com.example.smkproject.models.Recipe
import com.example.smkproject.views.AddRecipeView
import java.text.SimpleDateFormat
import java.util.*

class AddRecipePresenter (var view: AddRecipeView){

    fun saveRecipe(title: String, describ: String){
        val currentDate = Date()
        val dateFormat = SimpleDateFormat(
            "dd-MM-yyyy HH:mm:ss"
        )
        val date = dateFormat.format(currentDate)
        val recipe = Recipe(title, describ, date)
        val mainRepository = MainRepository()
        mainRepository.saveRecipeInDb(view.getContex(), recipe)


    }
    fun onBack(){
        val mainRepository = MainRepository()
        mainRepository.loadRecipesInLog(view.getContex())
        val intent = Intent(view.getContex(), MainActivity::class.java)
        view.setResult(intent)
        view.navigateTo(MainActivity::class.java)
    }


}