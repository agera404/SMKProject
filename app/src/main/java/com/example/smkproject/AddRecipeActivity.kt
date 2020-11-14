package com.example.smkproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        addrecipr_toolbar

        //back screen button
        addrecipr_toolbar.setNavigationIcon(R.drawable.ic_action_name)
        addrecipr_toolbar.setNavigationOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?){

                saveRecipe()
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
    override fun setResult(intent: Intent){
        setResult(Activity.RESULT_OK, intent)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }





    override fun getContex(): Context {
        return this@AddRecipeActivity
    }


    override fun saveRecipe(){
        if(describ.length<=0) presenter.onBack()
        if(describ.length>0){
            if(title.length<=0) title="Новый рецепт"
            presenter.saveRecipe(title, describ)
            presenter.onBack()
        }

    }


    override fun navigateTo(target: Class<*>) {
        startActivity(Intent(this, target))
    }



}