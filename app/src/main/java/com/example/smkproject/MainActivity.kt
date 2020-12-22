package com.example.smkproject


import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
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


        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment!!.navController

        toolbarBinding = binding.toolbarInc

        binding.resetFilterButton.setOnClickListener(onClickListener)
        MainRepository.currentIdTag = MainRepository.ID_TAG_ALLRECIPE
        binding.navView.setNavigationItemSelectedListener(this)


        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_navigation);
            getSupportActionBar()?.setTitle("");
        }

        
    }

    var onClickListener = View.OnClickListener { v ->
        when(v){
            binding.resetFilterButton ->{
                binding.searchResultFor.text = String()
                binding.searchInform.visibility = View.GONE
                navController?.popBackStack()
                navController?.navigate(R.id.recipesFragment)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                if(!drawerLayout.isDrawerOpen(Gravity.LEFT)) drawerLayout.openDrawer(Gravity.LEFT)
                else drawerLayout.closeDrawer(Gravity.RIGHT)
            }
            R.id.action_editRecipe ->{
                navController?.navigate(R.id.editRecipeFragment)
            }
            R.id.action_search ->{

            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.activity_main_toolbar, menu);

        val myActionMenuItem = menu!!.findItem(R.id.action_search)
        var searchView = myActionMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                MainRepository.getFilter!!(query)
                if (query != ""){
                    binding.searchInform.visibility = View.VISIBLE
                    binding.searchResultFor.text = "${getString(R.string.search_result_for_query)} '$query':"
                }else{
                    binding.searchResultFor.text = String()
                    binding.searchInform.visibility = View.GONE
                }

                myActionMenuItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {

                MainRepository.getFilter!!(s)
                if (s != ""){
                    binding.searchInform.visibility = View.VISIBLE
                    binding.searchResultFor.text = "${getString(R.string.search_result_for_query)} '$s':"
                }else{
                    binding.searchResultFor.text = String()
                    binding.searchInform.visibility = View.GONE
                }

                return false
            }
        })


        navController!!.addOnDestinationChangedListener(OnDestinationChangedListener { controller, destination, arguments ->
            val er= menu.findItem(R.id.action_editRecipe)
            if (destination.id == R.id.recipeFragment) {
                er.setVisible(true)
                myActionMenuItem.setVisible(false)
            }else{
                er.setVisible(false)
                myActionMenuItem.setVisible(true)
            }
        })
        return true
    }

    override fun updateToolbarTitle(title: String) {
        supportActionBar?.title = title
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
            var subM0 = menu.addSubMenu(1,1,1,"Tags")

            for (t in tags){
                var title = "${t.tag} (${t.count})"
                subM0.add(0, t.id!!.toInt(),0, title)
            }
        }

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


