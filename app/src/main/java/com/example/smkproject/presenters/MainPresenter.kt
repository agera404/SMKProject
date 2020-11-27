package com.example.smkproject.presenters

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
        this.viewLifecycle = view.viewLifecycle
        viewLifecycle?.addObserver(this)


        allTagLD = MainRepository.loadTags()
        allTagLD?.observe(view as MainActivity, Observer <List<Tag>>{
                overr
        })

        tags = allTagLD?.value!!
        MainRepository.updateMenu = { view.updateMenu() }

    }




    fun showRecipes(idTag: Long){
        MainRepository.currentIdTag = idTag
    }




}