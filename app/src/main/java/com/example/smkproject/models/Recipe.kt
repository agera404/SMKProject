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
        val idRecipe: Long? = MainRepository.db?.recipeDao()?.insertRecipe(recipe)
        val tags = recipe?.convertTags()
        if (tags != null) {
            for (tag in tags){
                val idTag = MainRepository.db?.tagDao()?.insertOrUpdate(tag)
                if (idTag != null) {
                    MainRepository.db?.tagDao()?.increaseCount(idTag = idTag)
                }
                if (idTag != null && idRecipe!=null){
                    MainRepository.db?.recipeTagDao()?.insert(RecipeTag(id=null, recipe_id = idRecipe, tag_id = idTag))
                }
            }
        }
        var ingredients = recipe?.convertIngredients()
        if (ingredients != null) {
            for (ingr in ingredients){
                var idIngr = MainRepository.db?.ingredientDao()?.insertOrUpdate(ingr)
                if (idIngr != null && idRecipe !=null){
                    MainRepository.db?.recipeIngredientDao()?.insert(RecipeIngredient(id=null, recipe_id = idRecipe, ingredient_id = idIngr))
                }
            }
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

