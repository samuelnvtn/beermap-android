package com.mendelu.xnovotn1.beerbook.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BeersDao {

    @Insert
    suspend fun insertData(beer: Beer)

    @Update
    suspend fun update(beer: Beer)

    @Delete
    suspend fun delete(beer: Beer)

    //counts all beers
    @Query("SELECT COUNT(id) FROM beers")
    fun getCount(): Int

    //gets most drank beer
    @Query("SELECT name FROM beers GROUP BY name ORDER BY COUNT(name) DESC LIMIT 1")
    fun getMostDrankBeer(): String

    //gets avg rating
    @Query("SELECT AVG(rating) FROM beers")
    fun getAvgRating(): Float?

    // TODO: beers per day, week, month, year 
    
    @Query("SELECT * FROM beers ORDER BY id DESC")
    fun getAll(): Flow<List<Beer>>

    @Query("SELECT * FROM beers WHERE id = :id")
    suspend fun getById(id: Long): Beer

    @Query("UPDATE beers SET beerState = :state WHERE id = :id")
    suspend fun changeBeerState(id: Long, state: Boolean)
}