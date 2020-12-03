package com.example.smkproject

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smkproject.common.MainRepository
import com.example.smkproject.databinding.FragmentRecipesBinding
import com.example.smkproject.databinding.RecipeContainerBinding
import com.example.smkproject.presenters.RecipesPresenter
import com.example.smkproject.views.RecipesView
import kotlinx.android.synthetic.main.fragment_recipes.*


class RecipesFragment : Fragment(), RecipesView {


    private var presenter: RecipesPresenter? = null

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = RecipesPresenter(this)
        presenter?.showRecipes()
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


    var clickListener = View.OnClickListener{v ->
        var tag = v.tag.toString()
        var idRecipe = tag.toLong()
        presenter?.selectRecipe(idRecipe)
        findNavController().navigate(R.id.recipeFragment)
    }
    var longClickListener =  View.OnLongClickListener { v ->
        val items =
            arrayOf<CharSequence>(getString(R.string.delete_recipe))

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setItems(items,
            DialogInterface.OnClickListener { dialog, item ->
                var idRecipe  = v.tag.toString().toLong()
                (v?.parent as ViewGroup).removeView(v)
                presenter?.deleteRecipe(idRecipe)
            })
        builder.show()
        true
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
        view.setOnClickListener(clickListener)
        view.setOnLongClickListener(longClickListener)
        recipesLayout.addView(view)
    }
}