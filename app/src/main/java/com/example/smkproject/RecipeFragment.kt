package com.example.smkproject

import android.icu.text.CaseMap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.LifecycleOwner
import com.example.smkproject.databinding.FragmentRecipeBinding
import com.example.smkproject.databinding.FragmentRecipesBinding
import com.example.smkproject.models.Ingredient
import com.example.smkproject.presenters.RecipePresenter
import com.example.smkproject.views.RecipeView
import kotlinx.android.synthetic.main.fragment_edit_recipe.*
import kotlinx.android.synthetic.main.fragment_recipe.*


class RecipeFragment : Fragment(), RecipeView {
    var presenter: RecipePresenter? = null

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = RecipePresenter(this)
        presenter!!.loadRecipe()


    }

    override fun setField(title: String, describ: String, ingredient: String, tags: String){
        binding.titleTV.text = title
        binding.describTV.text = describ
        binding.ingredientTV.text = ingredient
        binding.tagsTV.text = tags
    }


}