package com.example.smkproject.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Entity(tableName = "tags")
class Tag(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "tag") val tag: String,
    @ColumnInfo(name = "count")val count: Int) {

}

@Dao
interface TagDao{
    @Query("SELECT * FROM tags")
    fun getAll(): LiveData<List<Tag>>?

    @Query("SELECT * FROM tags WHERE id = :id")
    fun getById(id: Long): Tag?

    @Insert
    fun insert(tag: Tag?)

    @Update
    fun update(tag: Tag?)

    @Delete
    fun delete(tag: Tag?)
}