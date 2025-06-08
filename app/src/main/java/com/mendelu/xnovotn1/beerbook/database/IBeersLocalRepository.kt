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
    suspend fun getCount(): Int
    suspend fun getMostDrankBeer(): String
    suspend fun getAvgRating(): Float?
    suspend fun getMostBeersInOneDay(): BeersDao.MostBeersDay?
    suspend fun getBeersPerMonth(): List<BeersDao.BeersPerMonth>

    fun getAll(): Flow<List<Beer>>
    suspend fun getById(id: Long): Beer
    suspend fun changeBeerState(id: Long, state: Boolean)
}