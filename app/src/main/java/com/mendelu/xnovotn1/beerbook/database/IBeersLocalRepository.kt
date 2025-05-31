package com.mendelu.xnovotn1.beerbook.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface IBeersLocalRepository {

    suspend fun insertData(beer: Beer)
    suspend fun update(beer: Beer)
    suspend fun delete(beer: Beer)
    fun getCount(): Int
    fun getMostDrankBeer(): String
    fun getAvgRating(): Float?

    // TODO: beers per day, week, month, year

    fun getAll(): Flow<List<Beer>>
    suspend fun getById(id: Long): Beer
    suspend fun changeBeerState(id: Long, state: Boolean)
}