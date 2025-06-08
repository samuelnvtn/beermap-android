package com.mendelu.xnovotn1.beerbook.datastore

import kotlinx.coroutines.flow.Flow

interface IDataStoreRepository {

    fun getTheme(): Flow<String>
    suspend fun setTheme(theme: String)

    fun getLanguage(): Flow<String>
    suspend fun setLanguage(language: String)

}