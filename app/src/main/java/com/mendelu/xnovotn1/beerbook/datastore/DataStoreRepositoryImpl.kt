package com.mendelu.xnovotn1.beerbook.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl (private val context: Context) : IDataStoreRepository {

    override suspend fun setTheme(theme: String) {
        context.dataStore.edit { prefs ->
            prefs[stringPreferencesKey(DataStoreConstants.KEY_THEME)] = theme
        }
    }

    override fun getTheme(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[stringPreferencesKey(DataStoreConstants.KEY_THEME)]
        }
    }

    override fun getLanguage(): Flow<String> = context.languageFlow

    override suspend fun setLanguage(language: String) {
        context.setLanguage(language)
    }
}