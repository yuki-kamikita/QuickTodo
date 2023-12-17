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
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val isCompleted: Boolean = false,
    val sendNotification: Boolean = false,
    val completedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE isCompleted = 0")
    fun getTodoTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE isCompleted = 1 ORDER BY completedAt DESC")
    fun getDoneTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE sendNotification = 1 AND isCompleted = 0")
    fun getNotificationTasks(): LiveData<List<Task>>

    @Query("UPDATE task SET isCompleted = 1, completedAt = :completedAt WHERE id = :taskId")
    suspend fun markAsCompleted(taskId: Int, completedAt: LocalDateTime)

    @Query("UPDATE task SET isCompleted = 0, completedAt = NULL WHERE id = :taskId")
    suspend fun markAsNotCompleted(taskId: Int)

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}

