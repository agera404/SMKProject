package com.example.smkproject

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.navigation.NavController
import androidx.navigation.NavController.OnDestinationChangedListener
import androidx.navigation.fragment.NavHostFragment
import com.example.smkproject.common.MainRepository
import com.example.smkproject.databinding.ActivityMainBinding
import com.example.smkproject.databinding.ToolbarBinding
import com.example.smkproject.presenters.MainPresenter
import com.example.smkproject.views.MainView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,  MainView{

    private lateinit var presenter: MainPresenter

    var navHostFragment: NavHostFragment? = null
    var navController: NavController? = null

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbarBinding: ToolbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        MainRepository.context= this@MainActivity

        presenter = MainPresenter(this)
        createMenu()

        //binding.navHostFragment
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment!!.navController

        navController!!.addOnDestinationChangedListener(OnDestinationChangedListener { controller, destination, arguments ->
            val bt= findViewById<ImageButton>(R.id.editRecipeMenuButton)
            if (destination.id == R.id.recipeFragment) {
                bt.visibility = View.VISIBLE
            } else{
                bt.visibility = View.INVISIBLE
            }
        })

        toolbarBinding = binding.toolbar
        toolbarBinding.openNavigationMenuButton.setOnClickListener(onClickListener)
        toolbarBinding.editRecipeMenuButton.setOnClickListener(onClickListener)
        MainRepository.currentIdTag = MainRepository.ID_TAG_ALLRECIPE
        binding.navView.setNavigationItemSelectedListener(this)
    }

    var onClickListener = View.OnClickListener { v ->
        when(v){
            toolbarBinding.openNavigationMenuButton ->{
                if(!drawerLayout.isDrawerOpen(Gravity.LEFT)) drawerLayout.openDrawer(Gravity.LEFT)
                else drawerLayout.closeDrawer(Gravity.RIGHT)
            }
            toolbarBinding.editRecipeMenuButton ->{
                navController?.navigate(R.id.editRecipeFragment)
            }
        }
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        for (t in MainRepository.allTags!!){
            if (t.id?.toInt()!!  == item.itemId){
                MainRepository.currentIdTag = t.id
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
        var menu = binding.navView.menu
        menu.removeGroup(1)
        createMenu()
    }



    override fun createMenu(){
        var tags = presenter.getTags()
        var menu = nav_view.menu
        if (tags.count()!! >0){
            Log.d("mLog", "createMenu()")

            var subM0 = menu.addSubMenu(1,1,1,"Tags")

            for (t in tags){
                var title = "${t.tag} (${t.count})"
                subM0.add(0, t.id!!.toInt(),0, title)
            }
        }
        //var adapter: ArrayAdapter<String> = ArrayAdapter() адаптер для поиска
        var searchItem: MenuItem = menu.findItem(R.id.search)//binding.navView.
        var searchView: SearchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                //на каждый символ
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                //кнопка нажата
                return false
            }

        })
    }

    fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.allRecipeItemMenu -> {
                MainRepository.currentIdTag = MainRepository.ID_TAG_ALLRECIPE
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


