package com.example.smkproject

import android.content.Context
import android.content.Intent
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

        //Обрабатываем события OnClick кнопки backHomeButton
        backHomeButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?){
                isCheckRecipe()
                presenter.saveRecipe(title, describ)
                presenter.onBack()
            }

            })

        //слушаем события titleRecipeTextView
        titleRecipeTextView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                title = titleRecipeTextView.text.toString()

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
        //слушаем события describRecipeTextView
        describRecipeTextView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                describ = describRecipeTextView.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
    }

    override fun getContex(): Context {
        return this@AddRecipeActivity
    }


    override fun isCheckRecipe(): Boolean {
        if (title.length<0) title="Новый рецепт"
        return true;
    }


    override fun navigateTo(target: Class<*>) {
        startActivity(Intent(this, target))
    }



}