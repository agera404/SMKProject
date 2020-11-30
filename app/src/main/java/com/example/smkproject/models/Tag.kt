package com.example.smkproject.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Entity(tableName = "tags")
class Tag(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "tag") val tag: String,
    @ColumnInfo(name = "count")val count: Int) {

}

@Dao
interface TagDao{

    @Query("SELECT * FROM tags LIMIT 1")
    suspend fun getAnyTag(): Tag

    @Query("SELECT * FROM tags")
    suspend fun getAll(): List<Tag>?

    @Query("SELECT * FROM tags WHERE id = :id")
    suspend fun getById(id: Long): Tag?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tag: Tag?): Long

    @Update
    suspend fun update(tag: Tag?)

    @Delete
    suspend fun delete(tag: Tag?)
}