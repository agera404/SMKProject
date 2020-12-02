package com.example.smkproject.models

import androidx.room.*

@Entity(tableName = "ingredients",
    indices = [Index(value = ["title"],
        unique = true)])
class Ingredient(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "title")val title: String,
    @ColumnInfo(name = "amount")val amount: Double,
    @ColumnInfo(name = "unit")val unit: String) {

}
@Dao
abstract class IngredientDao{
    @Insert
    abstract suspend fun insert(ingredient: Ingredient?): Long?

    @Update
    abstract suspend fun update(ingredient: Ingredient?)

    @Delete
    abstract suspend fun delete(ingredient: Ingredient?)


    suspend fun insertOrUpdate(ingredient: Ingredient?): Long? {
        var item = getByIngredient(ingredient?.title!!)
        if (item != null){
            return item.id
        }else{
            return insert(ingredient)
        }
    }

    @Query("SELECT * FROM ingredients WHERE title = :ingredients")
    abstract suspend fun getByIngredient(ingredients: String): Ingredient?

    @Query("SELECT * FROM ingredients WHERE id = :id")
    abstract suspend fun getById(id: Long): Ingredient?

}