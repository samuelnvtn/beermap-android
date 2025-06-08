package com.mendelu.xnovotn1.beerbook.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DataStoreConstants.PREFERENCES_NAME
)

val Context.themeFlow: Flow<String> get() = dataStore.data
    .map { prefs -> prefs[stringPreferencesKey(DataStoreConstants.THEME)] ?: "system" }

suspend fun Context.setThemePreference(theme: String) {
    dataStore.edit { prefs ->
        prefs[stringPreferencesKey(DataStoreConstants.THEME)] = theme
    }
}

val Context.languageFlow: Flow<String>
    get() = dataStore.data
    .map { prefs -> prefs[stringPreferencesKey(DataStoreConstants.LANGUAGE)] ?: "cs" }

suspend fun Context.setLanguage(language: String) {
    dataStore.edit { prefs ->
        prefs[stringPreferencesKey(DataStoreConstants.LANGUAGE)] = language
    }
}

