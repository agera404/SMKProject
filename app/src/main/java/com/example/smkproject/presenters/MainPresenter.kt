package com.example.smkproject.presenters

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.smkproject.MainActivity
import com.example.smkproject.common.MainRepository
import com.example.smkproject.models.Tag
import com.example.smkproject.views.MainView
import kotlinx.coroutines.launch


class MainPresenter (var view: MainView): BasePresenter() {
    private var viewLifecycle: Lifecycle? = null
    var allTagLD: LiveData<List<Tag>>? = null
    var tags: List<Tag>? = arrayListOf<Tag>()

    init {
        MainRepository.context = (view as MainActivity).applicationContext
        this.viewLifecycle = view.viewLifecycle
        viewLifecycle?.addObserver(this)
        val observer = Observer<List<Tag>> { allTags ->
            tags = allTags
        }
        allTagLD?.observe(view as MainActivity, observer)
        launch {
            allTagLD = MainRepository.loadTags()
        }
        Log.d("mLog", "${tags?.count()}")
        if (tags!=null) view.createMenu()
        if (tags?.count()!!>0 && tags != null){
            MainRepository.updateMenu = { view.updateMenu() }
        }

    }





    fun showRecipes(idTag: Long){
        MainRepository.currentIdTag = idTag
    }




}