package com.example.smkproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.smkproject.common.DBHelper
import com.example.smkproject.presenters.MainPresenter
import com.example.smkproject.views.MainView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,  MainView {
    private var dbHelper: DBHelper? = null
    private var presenter: MainPresenter?= null

    var navController: NavController? = null
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(context = this)
        presenter = MainPresenter(this, dbHelper!!)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        createMenu()
        presenter!!.showRecipes(-1)
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        for (t in presenter?.repository?.tags!!){
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
        navController?.navigate(R.id.editRecipeFragment)
    }

    override fun navigateToRecipeFragment(bundle: Bundle){
        navController?.navigate(R.id.recipesFragment, bundle)
    }

    fun createMenu(){
        var menu = nav_view.menu
        var subM0 = menu.addSubMenu("Tags")
        for (t in presenter?.repository?.tags!!){
            var title = "${t.tag} (${t.count})"
            subM0.add(0, t.id.toInt(),0, title)
        }
        menu?.findItem(R.id.allRecipeItemMenu)?.setOnMenuItemClickListener(object:MenuItem.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                presenter?.showRecipes(-1)
                return true
            }
        })
        menu?.findItem(R.id.newRecipeItemMenu)?.setOnMenuItemClickListener(object :MenuItem.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                presenter?.addNewRecipe()
                return true
            }
        })
    }



    override fun navigateTo(target: Class<*>) {
        startActivityForResult(Intent(this, target), 1)
    }



}


