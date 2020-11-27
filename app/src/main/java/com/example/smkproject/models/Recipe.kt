package com.example.smkproject.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*


@Entity(tableName = "recipes")
class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "describe") var describe: String,
    @ColumnInfo(name = "dateTime") val dateTime: String,
    @ColumnInfo(name = "tags") var tags: String,
    @ColumnInfo(name = "ingredients") var ingredients: String
){


    /*fun ingrToStr(){
        stringIngredient = ""
        for (i in ingredients) {
            stringIngredient += "${i.ingredient} (${i.amount} ${i.unit}), "
        }
        stringIngredient = stringIngredient.dropLast(2)
    }
    fun strToIngr(){
        for (i in stringIngredient.split(",").toTypedArray()) {
            var tempAmount = i.substringAfter("(").substringBefore(" ").replace(" ", "")
            if(tempAmount.isNotEmpty())
            {
                var ingr = i.substringBefore("(").replace(" ", "")
                var amount: Double = tempAmount.toDouble()
                var unit = i.substringAfter("(").substringAfter(" ").dropLast(1)
                ingredients.add(Ingredient(ingr, amount, unit))
            }
        }
    }*/
}
@Dao
interface RecipeDao{
    @Query("SELECT * FROM recipes")
    fun getAll(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getById(id: Long): Recipe?

    @Insert
    fun insert(recipe: Recipe?)

    @Update
    fun update(recipe: Recipe?)

    @Delete
    fun delete(recipe: Recipe?)
}

