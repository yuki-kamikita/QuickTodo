package com.akaiyukiusagi.quicktodo.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.akaiyukiusagi.quicktodo.model.room.entity.Task
import com.akaiyukiusagi.quicktodo.model.room.entity.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class DatabaseFactory : RoomDatabase() {
    abstract fun taskDao(): TaskDao

}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabaseFactory(
        @ApplicationContext context: Context
    ): DatabaseFactory {
        return Room.databaseBuilder(
            context,
            DatabaseFactory::class.java,
            "task"
        ).build()
    }

    @Singleton
    @Provides
    fun provideTaskDao(database: DatabaseFactory): TaskDao {
        return database.taskDao()
    }
}
