package com.example.smkproject.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.smkproject.common.MainRepository
import java.lang.Exception


@Entity(tableName = "recipes")
class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "describe") var describe: String,
    @ColumnInfo(name = "dateTime") var dateTime: String,
    @ColumnInfo(name = "tags") var tags: String,
    @ColumnInfo(name = "ingredients") var ingredients: String
){
    @Ignore
    fun convertTags():ArrayList<Tag>{
        var tagsList: ArrayList<Tag> = arrayListOf<Tag>()
        var tagsStr = tags.split(",").toTypedArray()
        for (t in tagsStr){
            var _tag = t.trim(' ')
            if (_tag.isNotEmpty() && _tag.isNotEmpty())
                tagsList.add(Tag(id = null, tag = _tag))
        }
        return tagsList
    }
    @Ignore
    fun convertIngredients(items: ArrayList<Ingredient>){
        ingredients = ""
        for (item in items) {
            ingredients += "${item.title} (${item.amount} ${item.unit}), "
        }
        ingredients = ingredients.dropLast(2)
    }
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

        var idRecipe: Long? = null
        //insert
        if (recipe?.id == null) {
            MainRepository.db?.recipeDao()?.insertRecipe(recipe)?.let { idRecipe ->
                recipe?.convertTags()?.let {tags ->
                    for (tag in tags){
                        MainRepository.db?.apply {
                            tagDao()?.insertOrUpdate(tag)?.let {idTag ->
                                tagDao()?.increaseCount(idTag = idTag)
                                recipeTagDao()?.insert(RecipeTag(id=null, recipe_id = idRecipe, tag_id = idTag))
                            }
                        }
                    }
                }
                recipe?.convertIngredients()?.let { ingredients->
                    for (ingredient in ingredients){
                        MainRepository.db?.apply {
                            ingredientDao()?.insertOrUpdate(ingredient)?.let { idIngredient ->
                                recipeIngredientDao()?.insert(RecipeIngredient(id=null, recipe_id = idRecipe, ingredient_id = idIngredient))
                            }
                        }
                    }
                }
            }
        }
        //update
        else {
            recipe.id.let {idRecipe->
                getById(idRecipe)?.let { oldRecipe ->
                    recipe.convertTags()?.let { nTags ->
                        oldRecipe?.convertTags()?.let { oTags ->
                            for (oTag in oTags){
                                for (nTag in nTags){
                                    if (oTag.tag == nTag.tag) oTags.remove(oTag)
                                }
                            }
                            for (oTag in oTags){
                                MainRepository.db?.apply {
                                    tagDao()?.reduceCount(oTag.id!!)
                                    recipeTagDao()?.getByIdRecipes(oldRecipe.id!!)?.let {
                                        for (i in it){
                                            if (i.tag_id == oTag.id)  MainRepository.db?.recipeTagDao()?.delete(i)
                                        }
                                    }
                                }
                            }
                        }
                        for (tag in nTags){
                            MainRepository.db?.apply{
                                tagDao()?.insertOrUpdate(tag)?.let {idTag ->
                                    //tagDao()?.increaseCount(idTag = idTag)
                                    recipeTagDao()?.insert(RecipeTag(id=null, recipe_id = idRecipe, tag_id = idTag))
                                }
                            }
                        }
                    }
                    recipe.convertIngredients()?.let { nIngredients ->
                        oldRecipe.convertIngredients()?.let { oIngredients ->
                            for (oIngredient in oIngredients){
                                for (n in nIngredients){
                                    if (oIngredient.title == n.title) oIngredients.remove(oIngredient)
                                }
                            }
                            for (oIngredient in oIngredients){
                                MainRepository.db?.recipeIngredientDao()?.getByIdRecipe(oldRecipe.id!!)?.let {
                                    for (i in it){
                                        if (i.ingredient_id == oIngredient.id)  MainRepository.db?.recipeIngredientDao()?.delete(i)
                                    }
                                }
                            }
                        }
                        for (ingredient in nIngredients){
                            MainRepository.db?.apply {
                                ingredientDao()?.insertOrUpdate(ingredient)?.let { idIngredient ->
                                    recipeIngredientDao()?.insert(RecipeIngredient(id=null, recipe_id = idRecipe, ingredient_id = idIngredient))
                                }
                            }
                        }
                    }
                }
            }
            update(recipe)
        }
    }

    @Update
    abstract suspend fun update(recipe: Recipe?)

    @Delete
    abstract fun delete(recipe: Recipe?)

    suspend fun deleteRecipe(id: Long){
        val recipe = getById(id)
        if (recipe != null){
            val recipeIngredient = MainRepository.db?.recipeIngredientDao()?.getByIdRecipe(id)
            if (recipeIngredient != null) {
                for (item in recipeIngredient){
                    var ingredient = MainRepository.db?.ingredientDao()?.getById(item.ingredient_id)
                    Log.d("mLog","item.ingredient_id = ${item.ingredient_id}")
                    if (ingredient != null){
                        //не работает строка
                        MainRepository.db?.ingredientDao()?.delete(ingredient)
                    }
                }
            }
            val recipeTag = MainRepository.db?.recipeTagDao()?.getByIdRecipes(id)//в таблице рецепт_тег вытаскиваем все записи с recipe_id
            Log.d("mLog","recipeTag?.count() = ${recipeTag?.count()}")
            if (recipeTag!=null){                                                               //  id  |  recipe_id  |  tag_id  |
                for (item in recipeTag){                                             //для каждой строки
                    var tag =  MainRepository.db?.tagDao()?.getById(item.tag_id)         //ищем тег по tag_id в таблиые tags( id  |  tag  |  count  |  )
                    Log.d("mLog","item.tag_id = ${item.tag_id}")
                    if (tag != null) {                                                         //если тег нашли,
                        if ((tag.count-1)<=0){                                                 // проверяем можно ли уменьшить счетчик(count),
                            MainRepository.db?.tagDao()?.delete(tag)                           //если после уменьшения он 0 или меньше 0, удаляем тег из таблицы tags
                        }else{
                            MainRepository.db?.tagDao()?.reduceCount(item.tag_id)              //иначе просто уменьшаем счетчик.
                        }
                    }
                }
            }
            delete(recipe)
        }
    }
}

