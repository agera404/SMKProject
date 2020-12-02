package com.example.smkproject.models

import androidx.room.*

@Entity(tableName = "recipe_ingredient",foreignKeys = arrayOf(
    ForeignKey(entity = Recipe::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("recipe_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE),
    ForeignKey(entity = Tag::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("ingredient_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)),
        indices = [Index(value = ["recipe_id", "ingredient_id"],
        unique = true)]
)
class RecipeIngredient(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "recipe_id", index = true)val recipe_id: Long,
    @ColumnInfo(name = "ingredient_id", index = true) val ingredient_id: Long
) {
}
@Dao
abstract class RecipeIngredientDao{
    @Insert
    abstract suspend fun insert(recipe: RecipeIngredient?)

    @Delete
    abstract suspend fun delete(ri: RecipeIngredient)

    @Query("SELECT * FROM recipe_ingredient WHERE recipe_id = :idRecipe")
    abstract suspend fun getByIdRecipe(idRecipe: Long?): List<RecipeIngredient>
}