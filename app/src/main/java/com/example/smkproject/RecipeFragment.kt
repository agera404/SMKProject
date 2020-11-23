package com.example.smkproject

import android.icu.text.CaseMap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smkproject.models.Ingredient
import com.example.smkproject.presenters.RecipePresenter
import com.example.smkproject.views.RecipeView
import kotlinx.android.synthetic.main.fragment_recipe.*


class RecipeFragment : Fragment(), RecipeView {

    var presenter: RecipePresenter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = RecipePresenter(this)
        presenter!!.loadRecipe()
    }

    override fun setField(title: String, describ: String, ingredient: String, tags: String){
        titleTV.text = title
        describTV.text = describ
        ingredientTV.text = ingredient
        tagsTV.text = tags
    }


}