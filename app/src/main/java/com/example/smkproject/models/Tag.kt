package com.example.smkproject.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Entity(tableName = "tags",
    indices = [Index(value = ["tag"],
        unique = true)])
class Tag(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "tag") val tag: String,
    @ColumnInfo(name = "count") var count: Int?) {

}

@Dao
interface TagDao{

    @Query("SELECT * FROM tags LIMIT 1")
    suspend fun getAnyTag(): Tag

    @Query("SELECT * FROM tags")
    suspend fun getAll(): List<Tag>?

    @Query("SELECT * FROM tags WHERE id = :id")
    suspend fun getById(id: Long): Tag?

    @Query("SELECT * FROM tags WHERE tag = :tag")
    suspend fun getByTag(tag: String): Tag?

    @Query("UPDATE tags SET count = (SELECT count FROM tags WHERE id = :idTag) + 1")
    suspend fun increaseCount(idTag: Long?)

    //suspend fun reduceCount(idTag: String)

    @Insert
    suspend fun insert(tag: Tag?): Long

    @Update
    suspend fun update(tag: Tag?)

    @Delete
    suspend fun delete(tag: Tag?)
}