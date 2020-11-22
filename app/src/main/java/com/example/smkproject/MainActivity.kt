package com.example.smkproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.smkproject.common.DBHelper
import com.example.smkproject.common.MainRepository
import com.example.smkproject.presenters.MainPresenter
import com.example.smkproject.views.MainView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_edit_recipe.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,  MainView{
    private var dbHelper: DBHelper? = null
    private var presenter: MainPresenter?= null
    var navOptions: NavOptions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(context = this)
        presenter = MainPresenter(this, dbHelper!!)


        navOptions = NavOptions.Builder().setPopUpTo(R.id.recipesFragment, true).build()

        openNavigationMenuButton.setOnClickListener { openNavigationView() }

        createMenu()
        presenter!!.showRecipes(MainRepository.ID_TAG_ALLRECIPE)
        nav_view.setNavigationItemSelectedListener(this)

    }

    fun openNavigationView(){
        if(!drawerLayout.isDrawerOpen(Gravity.LEFT)) drawerLayout.openDrawer(Gravity.LEFT)
        else drawerLayout.closeDrawer(Gravity.RIGHT)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        for (t in MainRepository.tags){
            if (t.id.toInt() == item.itemId){
                presenter?.showRecipes(t.id)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.activity_main_toolbar, menu);
        return true
    }

    override fun navigateToEditRecipeFragment() {
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_to_editRecipeFragment, null, navOptions);
    }


    override fun navigateToRecipeFragment(){
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_to_recipesFragment, null, navOptions);
    }

    override fun updateMenu() {
        var menu = nav_view.menu
        menu.removeGroup(1)
        createMenu()
    }
    override fun createMenu(){
        var menu = nav_view.menu
        var subM0 = menu.addSubMenu(1,1,1,"Tags")

        for (t in MainRepository.tags){
            var title = "${t.tag} (${t.count})"
            subM0.add(0, t.id.toInt(),0, title)
        }

    }

    fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.allRecipeItemMenu -> {
                presenter?.showRecipes(MainRepository.ID_TAG_ALLRECIPE)
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.newRecipeItemMenu -> {
                presenter?.addNewRecipe()
                drawerLayout.closeDrawer(GravityCompat.START)

            }
        }
        return true

    }

    override fun navigateTo(target: Class<*>) {
        startActivityForResult(Intent(this, target), 1)
    }



}


