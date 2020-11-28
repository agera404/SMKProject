package com.example.smkproject.presenters

import android.os.Debug
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData

import androidx.lifecycle.Observer
import com.example.smkproject.MainActivity
import com.example.smkproject.common.MainRepository
import com.example.smkproject.models.Tag
import com.example.smkproject.views.MainView
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainPresenter (var view: MainView): BasePresenter() {
    private var viewLifecycle: Lifecycle? = null
    var allTagLD: LiveData<List<Tag>>? = null
    var tags: List<Tag>? = arrayListOf<Tag>()

    init {
        MainRepository.context = (view as MainActivity).applicationContext
        this.viewLifecycle = view.viewLifecycle
        viewLifecycle?.addObserver(this)

        launch {
            allTagLD = MainRepository.loadTags()
            Log.d("mLog", "allTagDL.value = ${allTagLD?.value != null}")
        }
        val observer = Observer<List<Tag>> { allTags ->
            tags = allTags
        }
        allTagLD?.observe(view as MainActivity, observer)
        if (tags?.count()!!>0 && tags != null){
            MainRepository.updateMenu = { view.updateMenu() }
        }

    }





    fun showRecipes(idTag: Long){
        MainRepository.currentIdTag = idTag
    }




}