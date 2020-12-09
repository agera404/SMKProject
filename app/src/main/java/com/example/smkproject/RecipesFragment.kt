package com.example.smkproject

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smkproject.databinding.FragmentRecipesBinding
import com.example.smkproject.models.Recipe
import com.example.smkproject.presenters.RecipesPresenter
import com.example.smkproject.views.RecipesView


class RecipesFragment : Fragment(), RecipesView {


    private var presenter: RecipesPresenter? = null

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    var adapter: RecipesAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = RecipesPresenter(this)
        presenter?.loadRecipes()

        val llm = LinearLayoutManager(context);
        binding.recipesLayout.layoutManager = llm
        adapter = RecipesAdapter(listRecipes = presenter!!.recipes!! as ArrayList<Recipe>, shortClickListener = { item: Recipe -> clickListener(item)}, longClickListener = { pos:Int, item: Recipe -> longClickListener(pos,item)})
        binding.recipesLayout.adapter = adapter

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    var clickListener = {recipe: Recipe ->
            presenter?.selectRecipe(recipe.id!!)
            findNavController().navigate(R.id.recipeFragment)
    }
    var longClickListener =  {pos: Int, recipe: Recipe ->
        val items = arrayOf<CharSequence>(getString(R.string.delete_recipe))
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setItems(items,
            DialogInterface.OnClickListener { dialog, item ->
                presenter?.deleteRecipe(recipe.id!!)
                adapter?.remove(pos)
            })
        builder.show()
        true
    }
}