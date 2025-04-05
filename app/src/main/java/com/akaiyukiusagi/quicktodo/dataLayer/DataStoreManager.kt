package com.akaiyukiusagi.quicktodo.dataLayer

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.booleanPreferencesKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun saveBoolean(booleanPreference: BooleanPreference, value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[booleanPreference.key()] = value
        }
    }

    fun getBooleanFlow(booleanPreference: BooleanPreference): Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[booleanPreference.key()] ?: booleanPreference.initialValue
        }
}

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }
}

enum class BooleanPreference(val initialValue: Boolean) {
    SHOW_DONE_TASKS(true);

    fun key(): Preferences.Key<Boolean> = booleanPreferencesKey(name)
}