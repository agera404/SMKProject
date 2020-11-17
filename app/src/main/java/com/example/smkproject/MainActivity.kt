package com.example.smkproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.smkproject.common.DBHelper
import com.example.smkproject.models.Recipe
import com.example.smkproject.models.Tag
import com.example.smkproject.presenters.MainPresenter
import com.example.smkproject.views.MainView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,  MainView {
    private val presenter = MainPresenter(this)
    var tags: ArrayList<Tag> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tags = presenter.loadTags()
        for (t in tags){
            //установить зависимость с рецептами
        }
        createMenu()

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = RecipesFragment()
        fragment.setRecipes(presenter.loadRecipes())
        ft.add(R.id.rootLinLay,fragment)
        ft.commit();

        addRecipeButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                presenter.goAddRecipeView()
            }
        })
        //create new recipe button
        /*main_toolbar.setNavigationIcon(R.drawable.ic_add_recipe)
        main_toolbar.setNavigationOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?){
                presenter.goAddRecipeView()
            }
        })*/
        nav_view.setNavigationItemSelectedListener(this)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        for (t in tags){
            if (t.id.toInt() == item.itemId){
                showRecipe(t.recipes)
            }
        }


        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        tags = presenter.loadTags()
        createMenu()
        showRecipe(presenter.loadRecipes())
    }

    override fun showRecipe(_recipes: ArrayList<Recipe>): Fragment {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = RecipesFragment()
        fragment.setRecipes(_recipes)
        ft.replace(R.id.recipesFragment, fragment)
        ft.commit();
        return fragment
    }

    fun createMenu(){
        var menu = nav_view.menu
        for (t in tags){
            var title = "${t.tag} (${t.count})"
            menu.add(0, t.id.toInt(),0, title)
        }
    }

    override fun getDBHelper(): DBHelper {
        var dbHelper = DBHelper(this)
        return dbHelper
    }

    override fun navigateTo(target: Class<*>) {
        startActivityForResult(Intent(this, target), 1)
    }



}


