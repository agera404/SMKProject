package com.example.smkproject

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smkproject.models.Recipe
import com.example.smkproject.presenters.MainPresenter
import com.example.smkproject.views.MainView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainView {
    private val presenter = MainPresenter(this)
    var recipes: ArrayList<Recipe> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recipes= presenter.loadRecipe()
        newRecipeButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?){
                presenter.goAddRecipeView()
            }
        })
    }


    override fun showRecipe() {
        fun createLinLayRecipe(recipe: Recipe): LinearLayout {
            val linLay = LinearLayout(this)
            linLay.orientation = LinearLayout.VERTICAL;
            val linLayParam =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            val textView: TextView = TextView(this)
            val text =
                "<h2>" + recipe.title + "</h2><br/><p>" + recipe.describ + "<p><br/>" + "<p>" + recipe.dateTime + "</p>"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
            } else {
                textView.text = Html.fromHtml(text);
            }
            linLay.addView(textView)
            return linLay
        }

        if (recipes.size>0){
            var countChild = rootLinLay.childCount

            for (recipe in recipes){
                val child = createLinLayRecipe(recipe)
                rootLinLay.addView(child,countChild)
            }

        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        this.recipes = data.getSerializableExtra("recipes") as ArrayList<Recipe>

    }

    override fun navigateTo(target: Class<*>) {
        startActivityForResult(intent, 1)
    }

    override fun getContex(): Context {
        return this@MainActivity
    }


}