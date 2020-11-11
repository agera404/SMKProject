package com.example.smkproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.example.smkproject.presenters.AddRecipePresenter
import com.example.smkproject.views.AddRecipeView
import kotlinx.android.synthetic.main.activity_add_recipe.*

class AddRecipeActivity : AppCompatActivity(), AddRecipeView  {
    private val presenter = AddRecipePresenter(this)
    var title:String = "Новый рецепт"
    var describ: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        //слушаем события titleRecipeTextView
        titleRecipeTextView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
        //слушаем события describRecipeTextView
        describRecipeTextView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
    }

    override fun onBackHomeScreen(view: View) {
        saveRecipe()
        presenter.onBack()
    }

    override fun saveRecipe() {
        title = titleRecipeTextView.text.toString()
        describ = describRecipeTextView.text.toString()
        presenter.saveRecipe(title, describ)
    }

    override fun navigateTo(target: Class<*>) {
        TODO("Not yet implemented")
    }



}