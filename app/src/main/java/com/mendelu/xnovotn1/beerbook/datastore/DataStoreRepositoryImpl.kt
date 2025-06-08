package com.mendelu.xnovotn1.beerbook.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl (private val context: Context) : IDataStoreRepository {

    override fun getTheme(): Flow<String> = context.themeFlow
    override suspend fun setTheme(theme: String) = context.setThemePreference(theme)

    override fun getLanguage(): Flow<String> = context.languageFlow

    override suspend fun setLanguage(language: String) {
        context.setLanguage(language)
    }
}