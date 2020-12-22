package com.example.smkproject.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Entity(tableName = "tags")
class Tag(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "tag") var tag: String,
    @ColumnInfo(name = "count") var count: Int = 0) {

}

@Dao
abstract class TagDao{

    @Query("SELECT * FROM tags LIMIT 1")
    abstract suspend fun getAnyTag(): Tag

    @Query("SELECT * FROM tags")
    abstract suspend fun getAll(): List<Tag>?

    @Query("SELECT * FROM tags WHERE id = :id")
    abstract suspend fun getById(id: Long): Tag?

    @Query("SELECT * FROM tags WHERE tag = :tag")
    abstract suspend fun getByTag(tag: String): Tag?


    @Query("UPDATE tags SET count = count + 1 WHERE id = :idTag")
    abstract suspend fun increaseCount(idTag: Long)

    @Query("UPDATE tags SET count = count - 1  WHERE id = :idTag")
    abstract suspend fun reduceCount(idTag: Long)


    suspend fun insertOrUpdate(tag: Tag?): Long? {
        var item = getByTag(tag?.tag!!)
        if (item != null){
            return item.id
        }else{

            var id = insert(tag)
            increaseCount(id)
            return id
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(tag: Tag?): Long

    @Update
    abstract suspend fun update(tag: Tag?)

    @Delete
    abstract suspend fun delete(tag: Tag?)
}