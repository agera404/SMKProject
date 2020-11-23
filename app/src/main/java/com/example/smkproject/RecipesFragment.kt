package com.example.smkproject

import android.icu.text.CaseMap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.smkproject.common.MainRepository
import com.example.smkproject.presenters.RecipesPresenter
import com.example.smkproject.views.RecipesView
import kotlinx.android.synthetic.main.fragment_recipes.*


class RecipesFragment : Fragment(), RecipesView {
    private var presenter: RecipesPresenter? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = RecipesPresenter(this)
        presenter?.showRecipes()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

    var onClickListener = View.OnClickListener{v ->
        var tag = v.tag.toString()
        var idRecipe = tag.toLong()
        Log.d("mLog", "$tag - $idRecipe")
        presenter?.openRecipe(idRecipe)
    }


    override fun setRecipeOnLayout(id: Long, title: String, describ: String, tags: String) {
        val indexTitel = 0
        val indexDescrib = 1
        val indexTags = 2

        var view = LayoutInflater.from(context).inflate(R.layout.recipe_container, null) as LinearLayout
        (view.children.elementAt(indexTitel) as TextView).text = title
        (view.children.elementAt(indexDescrib) as TextView).text = describ
        (view.children.elementAt(indexTags) as TextView).text = tags
        view.tag =id.toString()
        view.setOnClickListener(onClickListener)
        recipesLayout.addView(view)

    }
}