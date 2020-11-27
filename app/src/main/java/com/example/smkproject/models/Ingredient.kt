package com.example.smkproject.models

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
class Ingredient(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "title")val title: String,
    @ColumnInfo(name = "amount")val amount: Double,
    @ColumnInfo(name = "unit")val unit: String) {

}
@Dao
interface IngredientDao{

}