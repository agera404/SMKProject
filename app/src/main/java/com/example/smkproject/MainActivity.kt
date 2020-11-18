package com.example.smkproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
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
    var mSlideState=false;
    var navController: NavController? = null
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        tags = presenter.loadTags()
        createMenu()
        createFragment()
        nav_view.setNavigationItemSelectedListener(this)
    }
    fun createFragment(){
        val bundle = Bundle()
        bundle.putSerializable("recipes", ArrayList<Recipe>(presenter.loadRecipes()))
        navController?.navigate(R.id.recipesFragment, bundle)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.activity_main_toolbar, menu);
        return true
    }

    override fun showRecipe(_recipes: ArrayList<Recipe>){

        val bundle = Bundle()
        Log.d("mLog",_recipes.count().toString())
        bundle.putSerializable("recipes",_recipes)
        navController?.navigate(R.id.recipesFragment,bundle)
    }

    fun createMenu(){
        var menu = nav_view.menu
        var subM0 = menu.addSubMenu("Tags")
        for (t in tags){
            var title = "${t.tag} (${t.count})"
            subM0.add(0, t.id.toInt(),0, title)
        }
        var subM1 = menu.addSubMenu("Other")
        subM1.add("New Recipe").setOnMenuItemClickListener(object: MenuItem.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                navController?.navigate(R.id.editRecipeFragment)
                return true
            }
        })
        subM1.add("All recipes").setOnMenuItemClickListener(object: MenuItem.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                createFragment()
                return true
            }
        })
    }

    override fun getDBHelper(): DBHelper {
        var dbHelper = DBHelper(this)
        return dbHelper
    }

    override fun navigateTo(target: Class<*>) {
        startActivityForResult(Intent(this, target), 1)
    }



}


