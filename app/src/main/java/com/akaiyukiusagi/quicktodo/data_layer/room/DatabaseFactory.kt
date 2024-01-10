package com.akaiyukiusagi.quicktodo.data_layer.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.akaiyukiusagi.quicktodo.data_layer.room.entity.Task
import com.akaiyukiusagi.quicktodo.data_layer.room.entity.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [Task::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
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
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Singleton
    @Provides
    fun provideTaskDao(database: DatabaseFactory): TaskDao {
        return database.taskDao()
    }

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Task ADD COLUMN completedAt TEXT")
            database.execSQL("ALTER TABLE Task ADD COLUMN createdAt TEXT NOT NULL DEFAULT '2023-01-01T00:00:00'")
        }
    }
}
