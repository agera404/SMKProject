package com.example.smkproject.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.smkproject.common.MainRepository


@Entity(tableName = "recipes")
class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "describe") var describe: String,
    @ColumnInfo(name = "dateTime") val dateTime: String,
    @ColumnInfo(name = "tags") var tags: String,
    @ColumnInfo(name = "ingredients") var ingredients: String
){
    @Ignore
    fun convertTags():ArrayList<Tag>{
        var tagsList: ArrayList<Tag> = arrayListOf<Tag>()
        var tagsStr = tags.split(",").toTypedArray()
        for (t in tagsStr){
            var _tag = t.trim(' ')
            tagsList.add(Tag(id = null, tag = _tag, count = null))
        }
        return tagsList
    }
    /*@Ignore
    fun ingrToStr(){
        strIngredients = ""
        for (i in ingredients) {
            strIngredients += "${i.title} (${i.amount} ${i.unit}), "
        }
        strIngredients = strIngredients.dropLast(2)
    }*/
    @Ignore
    fun convertIngredients(): ArrayList<Ingredient> {
        var _ingredients: ArrayList<Ingredient> = arrayListOf()
        for (i in ingredients.split(",").toTypedArray()) {
            var tempAmount = i.substringAfter("(").substringBefore(" ").replace(" ", "")
            if(tempAmount.isNotEmpty())
            {
                var ingr = i.substringBefore("(").replace(" ", "")
                var amount: Double = tempAmount.toDouble()
                var unit = i.substringAfter("(").substringAfter(" ").dropLast(1)
                _ingredients.add(Ingredient(id = null, title = ingr, amount = amount, unit = unit))
            }
        }
        return _ingredients
    }
}
@Dao
abstract class RecipeDao{
    @Query("SELECT * FROM recipes")
    abstract suspend fun getAll(): List<Recipe>

    @Query("SELECT * FROM recipes WHERE id = :id")
    abstract suspend fun getById(id: Long): Recipe?

    @Insert
    abstract suspend fun insertRecipe(recipe: Recipe?): Long

    suspend fun insert(recipe: Recipe?){
        val idRecipe: Long? = MainRepository.db?.recipeDao()?.insertRecipe(recipe)
        val tags = recipe?.convertTags()
        if (tags != null) {
            for (tag in tags){
                val idTag = MainRepository.db?.tagDao()?.insert(tag)
                MainRepository.db?.tagDao()?.increaseCount(idTag = idTag)
                Log.d("mLog", "idTag = $idTag idRecipe = $idRecipe")
                if (idTag != null && idRecipe!=null){
                    MainRepository.db?.recipeTagDao()?.insert(RecipeTag(id=null, recipe_id = idRecipe, tag_id = idTag))
                }
            }
        }
        /*var ingredients = recipe?.convertIngredients()
        if (ingredients != null) {
            for (ingr in ingredients){
                var idIngr = MainRepository.db?.ingredientDao()?.insert(ingr)
                if (idIngr != null && idRecipe !=null){
                    MainRepository.db?.recipeIngredientDao()?.insert(RecipeIngredient(id=null, recipe_id = idRecipe, ingredient_id = idIngr))
                }
            }
        }*/
    }

    @Update
    abstract suspend fun update(recipe: Recipe?)

    @Delete
    abstract suspend fun delete(recipe: Recipe?)
}

