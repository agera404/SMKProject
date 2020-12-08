package com.example.smkproject


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.smkproject.models.Recipe


class RecipesAdapter(listRecipes: List<Recipe>): RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {
    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cv: CardView? = null
        var titleRecipe: TextView? = null
        var describeRecipe: TextView? = null
        var tagsRecipe: TextView? = null
        init {
            //replase view binding!!
           // super(itemView)
            cv = itemView.findViewById(R.id.recipeLayout)
            titleRecipe = itemView.findViewById(R.id.titleTVRC)
            describeRecipe = itemView.findViewById(R.id.describTVRC)
            tagsRecipe = itemView.findViewById(R.id.tagsTVRC)
        }
    }

    var listRecipes: List<Recipe> = arrayListOf()
    init {
        this.listRecipes = listRecipes
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val v: View =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_container, parent, false)
        return RecipeViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listRecipes.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {

        holder.titleRecipe?.setText(listRecipes.get(position).title)
        holder.describeRecipe?.setText(listRecipes.get(position).describe)
        holder.tagsRecipe?.setText(listRecipes.get(position).tags)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }
}