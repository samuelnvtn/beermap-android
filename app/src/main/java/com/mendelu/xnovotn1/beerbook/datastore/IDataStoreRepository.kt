package com.mendelu.xnovotn1.beerbook.datastore

import kotlinx.coroutines.flow.Flow

interface IDataStoreRepository {

    suspend fun setTheme(theme: String)
    fun getTheme(): Flow<String?>

    suspend fun setLanguage(language: String)
    fun getLanguage(): Flow<String?>

}