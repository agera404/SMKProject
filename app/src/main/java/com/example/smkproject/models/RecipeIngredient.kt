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
        onUpdate = ForeignKey.CASCADE)
))
class RecipeIngredient(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "recipe_id")val recipe_id: Long,
    @ColumnInfo(name = "ingredient_id") val ingredient_id: Long
) {
}
@Dao
public interface RecipeIngredientDao{

}