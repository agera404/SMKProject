package com.example.smkproject

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavController.OnDestinationChangedListener
import androidx.navigation.fragment.NavHostFragment
import com.example.smkproject.common.MainRepository
import com.example.smkproject.presenters.MainPresenter
import com.example.smkproject.views.MainView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,  MainView{
    private var presenter: MainPresenter?= null

    var navHostFragment: NavHostFragment? = null
    var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment!!.navController

        navController!!.addOnDestinationChangedListener(OnDestinationChangedListener { controller, destination, arguments ->
            var bt= findViewById<ImageButton>(R.id.editRecipeMenuButton)
            if (destination.id == R.id.recipeFragment) {
                bt.visibility = View.VISIBLE
                Log.d("mLog","set visible")
            } else{
                bt.visibility = View.INVISIBLE
            }
        })


        openNavigationMenuButton.setOnClickListener(onClickListener)
        editRecipeMenuButton.setOnClickListener(onClickListener)

        createMenu()
        presenter!!.showRecipes(MainRepository.ID_TAG_ALLRECIPE)
        nav_view.setNavigationItemSelectedListener(this)

    }

    var onClickListener = View.OnClickListener { v ->
        when(v){
            openNavigationMenuButton ->{
                if(!drawerLayout.isDrawerOpen(Gravity.LEFT)) drawerLayout.openDrawer(Gravity.LEFT)
                else drawerLayout.closeDrawer(Gravity.RIGHT)
            }
            editRecipeMenuButton ->{
                navController?.navigate(R.id.editRecipeFragment)
            }
        }
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        for (t in MainRepository.tags){
            if (t.id.toInt() == item.itemId){
                presenter?.showRecipes(t.id)
                navController?.popBackStack()
                navController?.navigate(R.id.recipesFragment)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.activity_main_toolbar, menu);
        return true
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
                navController?.popBackStack()
                navController?.navigate(R.id.recipesFragment)
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.newRecipeItemMenu -> {
                MainRepository.selectedRecipe = null
                navController?.navigate(R.id.editRecipeFragment)
                drawerLayout.closeDrawer(GravityCompat.START)

            }
        }
        return true

    }

}


