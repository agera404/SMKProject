package com.example.smkproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
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



        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = RecipesFragment()
        fragment.setRecipes(presenter.loadRecipes())
        ft.add(R.id.rootLinLay,fragment)
        ft.commit();

        //create new recipe button
        main_toolbar.setNavigationIcon(R.drawable.ic_add_recipe)
        main_toolbar.setNavigationOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?){
                presenter.goAddRecipeView()
            }
        })
    }




    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        showRecipe()
    }

    override fun showRecipe(): Fragment {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = RecipesFragment()
        fragment.setRecipes(presenter.loadRecipes())
        ft.replace(R.id.recipesFragment, fragment)
        ft.commit();
        return fragment
    }

    override fun navigateTo(target: Class<*>) {
        startActivityForResult(Intent(this, target), 1)
    }

    override fun getContex(): Context {
        return this@MainActivity
    }


}


