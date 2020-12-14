package com.example.smkproject.views

import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.smkproject.R
import com.example.smkproject.common.MainRepository

class Toolbar(view: View): Fragment(){
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val myActionMenuItem = menu!!.findItem(R.id.action_search)
        var searchView = myActionMenuItem.actionView as SearchView
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

    }
}