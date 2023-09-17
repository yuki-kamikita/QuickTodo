package com.akaiyukiusagi.quicktodo.model.room.entity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import java.time.LocalDateTime

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val content: String = "aaa",
    val isCompleted: Boolean = false,
//    val completedAt: LocalDateTime,
//    val createdAt: LocalDateTime
)

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAllTasks(): LiveData<List<Task>>

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}

