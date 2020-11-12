package com.example.smkproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.smkproject.presenters.MainPresenter
import com.example.smkproject.views.MainView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView {
    private val presenter = MainPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newRecipeButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?){
                presenter.onGoAddRecipeView()
            }
        })
    }
    override fun navigateTo(target: Class<*>) {
        startActivity(Intent(this, target))
    }






}