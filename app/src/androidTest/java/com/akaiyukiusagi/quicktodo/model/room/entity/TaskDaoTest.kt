package com.akaiyukiusagi.quicktodo.model.room.entity

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.akaiyukiusagi.quicktodo.core.LogHelper
import com.akaiyukiusagi.quicktodo.model.room.DatabaseFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {
    private lateinit var database: DatabaseFactory
    private lateinit var taskDao: TaskDao
    private lateinit var oldDate: LocalDateTime
    private lateinit var newDate: LocalDateTime

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            DatabaseFactory::class.java
        ).allowMainThreadQueries().build()
        taskDao = database.taskDao()

        oldDate = LocalDateTime.of(2023,1,1,12,0)
        newDate = LocalDateTime.of(2023,1,2,12,0)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndGetAllTasksTest() = runBlocking {
        val task1 = Task(id = 1, content = "Test Task 1")
        val task2 = Task(id = 2, content = "Test Task 2")

        taskDao.insert(task1)
        taskDao.insert(task2)

        val allTasks = taskDao.getAllTasks().first()

        LogHelper.d(task1.toString())
        LogHelper.d(allTasks.toString())
        assertEquals(2, allTasks.size) // 要素数
        assertEquals(task1, allTasks[0]) // 1つ目の内容チェック
        assertEquals(task2, allTasks[1]) // 2つ目の内容チェック
    }

    @Test
    fun getTodoTasksTest() = runBlocking {
        val todoTask = Task(id = 1, content = "Todo Task", isCompleted = false)
        val doneTask = Task(id = 2, content = "Done Task", isCompleted = true)

        taskDao.insert(todoTask)
        taskDao.insert(doneTask)

        val todoTasks = taskDao.getTodoTasks().first()
        assertTrue(todoTasks.contains(todoTask))
        assertFalse(todoTasks.contains(doneTask))
    }

    @Test
    fun getDoneTasksTest() = runBlocking {
        val todoTask = Task(id = 1, content = "Todo Task", isCompleted = false)
        val doneTask1 = Task(id = 2, content = "Done Task 1", isCompleted = true, completedAt = oldDate)
        val doneTask2 = Task(id = 3, content = "Done Task 2", isCompleted = true, completedAt = newDate)

        taskDao.insert(todoTask)
        taskDao.insert(doneTask1)
        taskDao.insert(doneTask2)

        val doneTasks = taskDao.getDoneTasks().first()
        assertFalse(doneTasks.contains(todoTask)) // 未完は取得しない
        assertEquals(doneTask2, doneTasks[0]) // 降順になってる確認
        assertEquals(doneTask1, doneTasks[1])
    }

    @Test
    fun getNotificationTasksTest() = runBlocking {
        val task = Task(id = 1, content = "Task", sendNotification = false)
        val notificationTask = Task(id = 2, content = "Notification Task", sendNotification = true)

        taskDao.insert(task)
        taskDao.insert(notificationTask)

        val notificationTasks = taskDao.getNotificationTasks().first()
        assertFalse(notificationTasks.contains(task))
        assertTrue(notificationTasks.contains(notificationTask))
    }

    @Test
    fun markAsCompletedAndGetDoneTasksTest() = runBlocking {
        val task = Task(id = 1, content = "Test Task")
        taskDao.insert(task)

        val now = LocalDateTime.now()
        taskDao.markAsCompleted(task.id, now)

        val doneTasks = taskDao.getDoneTasks().first()

        LogHelper.d(task.toString())
        LogHelper.d(doneTasks.toString())
        assertTrue(doneTasks.any { it.id == task.id && it.isCompleted }) // 完了したタスクが存在するか検証
    }

    @Test
    fun markAsNotCompletedTest() = runBlocking {
        val task = Task(id = 1, content = "Completed Task", isCompleted = true)

        taskDao.insert(task)
        taskDao.markAsNotCompleted(task.id)

        val todoTasks = taskDao.getTodoTasks().first()
        assertTrue(todoTasks.any { it.id == task.id && !it.isCompleted })
    }

    @Test
    fun updateTaskTest() = runBlocking {
        val task = Task(id = 1, content = "Original Content")
        val updatedTask = task.copy(content = "Updated Content")

        taskDao.insert(task)
        taskDao.update(updatedTask)

        val allTasks = taskDao.getAllTasks().first()
        assertTrue(allTasks.any { it == updatedTask })
    }

    @Test
    fun deleteTaskTest() = runBlocking {
        val task = Task(id = 1, content = "Task to Delete")

        taskDao.insert(task)
        taskDao.delete(task)

        val allTasks = taskDao.getAllTasks().first()
        assertFalse(allTasks.contains(task))
    }
}
