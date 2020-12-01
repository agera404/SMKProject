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
interface IngredientDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tag: Ingredient?): Long

    @Query("SELECT * FROM ingredients WHERE title = :ingredients")
    suspend fun getByIngredients(ingredients: String): Ingredient?
}