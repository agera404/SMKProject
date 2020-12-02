package com.example.smkproject.models

import androidx.room.*

@Entity(tableName = "recipe_tag",
    foreignKeys = arrayOf(
    ForeignKey(entity = Recipe::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("recipe_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE),
    ForeignKey(entity = Tag::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("tag_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE))
        )
class RecipeTag(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "recipe_id", index = true)val recipe_id: Long,
    @ColumnInfo(name = "tag_id", index = true) val tag_id: Long
) {
}
@Dao
abstract class RecipeTagDao{
    @Query("SELECT * FROM recipe_tag LEFT JOIN recipes ON recipe_tag.recipe_id = recipes.id WHERE recipe_tag.tag_id = :idTag")
    abstract suspend fun getRecipesByTag(idTag: Long): List<Recipe>?

    @Query("SELECT * FROM recipe_tag WHERE recipe_id = :idRecipe")
    abstract suspend fun getByIdRecipes(idRecipe: Long?): List<RecipeTag>

    @Delete
    abstract suspend fun delete(rt: RecipeTag)

    @Insert
    abstract suspend fun insert(recipeTag: RecipeTag)

}