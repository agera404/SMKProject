package com.example.smkproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smkproject.common.DBHelper
import com.example.smkproject.presenters.EditRecipePresenter
import com.example.smkproject.views.EditRecipeView
import kotlinx.android.synthetic.main.fragment_edit_recipe.*


class EditRecipeFragment : Fragment(),EditRecipeView {
    private val presenter = EditRecipePresenter(this)
    var title:String = "Новый рецепт"
    var describ: String = ""
    var tags: String = ""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_recipe, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addRecupeButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                presenter.saveRecipe(title,describ,tags)
                presenter.onBack()
            }
        })
        //слушаем события titleRecipeEditText
        titleRecipeEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                title = titleRecipeEditText.text.toString()

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
        //слушаем события describRecipeEditText
        describRecipeEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                describ = describRecipeEditText.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
        //слушаем события describRecipeEditText
        tagsEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                tags = tagsEditText.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })

    }

    override fun getDBHelper(): DBHelper {
        var dbHelper = DBHelper(context)
        return dbHelper
    }

    override fun saveRecipe(){
        if(describ.length<=0) presenter.onBack()
        if(describ.length>0){
            if(title.length<=0) title="Новый рецепт"
            presenter.saveRecipe(title, describ, tags)
            presenter.onBack()
        }

    }

}