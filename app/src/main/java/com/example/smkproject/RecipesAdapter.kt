package com.example.smkproject


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.smkproject.common.MainRepository
import com.example.smkproject.models.Recipe


class RecipesAdapter(listRecipes: ArrayList<Recipe>,
                     private val shortClickListener: (Recipe) -> Unit,
                     private val longClickListener: (pos: Int, Recipe) -> Boolean): RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>(),
    Filterable {
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


    var recipes: ArrayList<Recipe> = arrayListOf()
        set(value) {
            field = value
            filtredRecipes.clear()
            filtredRecipes.addAll(field)
            notifyDataSetChanged()
        }
    var filtredRecipes: ArrayList<Recipe> = arrayListOf()


    init {
        this.recipes = listRecipes
        Log.d("mLog","filtredRecipes ${filtredRecipes.size}")
        MainRepository.getFilter = {text: String -> getFilter().filter(text)}
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val v: View =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_container, parent, false)
        return RecipeViewHolder(v)
    }

    override fun getItemCount(): Int {
        return filtredRecipes.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = filtredRecipes.get(position)
        holder.titleRecipe?.setText(item.title)
        holder.describeRecipe?.setText(item.describe)
        holder.tagsRecipe?.setText(item.tags)
        holder.itemView.setOnClickListener { shortClickListener(item) }
        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            longClickListener(position, item)
        })

    }
    fun remove(position: Int) {
        filtredRecipes.removeAt(position)
        notifyItemRemoved(position)
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val queryString = constraint?.toString()?.toLowerCase()
                val filterResults = Filter.FilterResults()



                if (queryString==null || queryString.isEmpty()){
                    filterResults.values = recipes
                } else{
                    filterResults.values = recipes.filter {
                        it.title.toLowerCase().contains(queryString) ||
                                it.describe.toLowerCase().contains(queryString) ||
                                it.ingredients.toLowerCase().contains(queryString) ||
                                it.tags.toLowerCase().contains(queryString) ||
                                it.dateTime.toLowerCase().contains(queryString) }
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filtredRecipes.clear()
                filtredRecipes.addAll(results?.values as ArrayList<Recipe>)
                notifyDataSetChanged();
            }
        }
    }
}