package com.example.smkproject

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.smkproject.common.DBHelper
import com.example.smkproject.models.Recipe
import com.example.smkproject.presenters.RecipesPresenter
import com.example.smkproject.views.RecipesView
import kotlinx.android.synthetic.main.fragment_recipes.*


class RecipesFragment : Fragment(), RecipesView {
    private var dbHelper: DBHelper? = null
    private var presenter: RecipesPresenter? = null



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        dbHelper = DBHelper(context)
        presenter = RecipesPresenter(this, dbHelper!!)

        val bundle = arguments
        var idTag: Long? = bundle?.getLong("idTag")
        Log.d("mLog","idTag - $idTag")
        presenter?.setIdTag(idTag)



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

    //переделать, передавать из презентера не рецепт, а text: String
    private fun createLinLayRecipe(recipe: Recipe): View {

        val linLay = LinearLayout(context)
        linLay.orientation = LinearLayout.VERTICAL;
        val linLayParam =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        val textView: TextView = TextView(context)
        val text =
            "<h2>" + recipe.title + "</h2><br/><p>" + recipe.describ + "<p><br/>" + "<p>" + recipe.dateTime + "</p><br/><p>" +recipe.tags+"</p>"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            textView.text = Html.fromHtml(text);
        }
        linLay.addView(textView)
        return linLay
    }
    //переделать, передавать из презентера не рецепт, а text: String
    override fun setRecipeOnLayout(recipe: Recipe) {
        if (recipe!=null){
            val child = createLinLayRecipe(recipe)
            recipesLayout.addView(child)
            Log.d("mLog", "recipE set on layout")
        }else return

    }
}