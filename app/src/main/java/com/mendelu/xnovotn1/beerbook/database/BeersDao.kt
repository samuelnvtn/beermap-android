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
    suspend fun getCount(): Int

    //gets most drank beer
    @Query("SELECT name FROM beers GROUP BY name ORDER BY COUNT(name) DESC LIMIT 1")
    suspend fun getMostDrankBeer(): String

    //gets avg rating
    @Query("SELECT AVG(rating) FROM beers")
    suspend fun getAvgRating(): Float?

    // TODO: beers per day, week, month, year 

    @Query("SELECT * FROM beers ORDER BY timestamp DESC")
    fun getAll(): Flow<List<Beer>>

    @Query("SELECT * FROM beers WHERE id = :id")
    suspend fun getById(id: Long): Beer

    @Query("UPDATE beers SET beerState = :state WHERE id = :id")
    suspend fun changeBeerState(id: Long, state: Boolean)

    // most beers logged in one day
    @Query(
        """
        SELECT DATE(timestamp / 1000, 'unixepoch') as day, COUNT(*) as total
        FROM beers
        GROUP BY day
        ORDER BY total DESC
        LIMIT 1
    """
    )
    suspend fun getMostBeersInOneDay(): MostBeersDay?

    // total beers per month
    @Query(
        """
        SELECT strftime('%Y-%m', timestamp / 1000, 'unixepoch') as month, COUNT(*) as total
        FROM beers
        GROUP BY month
        ORDER BY month DESC
    """
    )
    suspend fun getBeersPerMonth(): List<BeersPerMonth>

    data class MostBeersDay(
        val day: String,
        val total: Int
    )

    data class BeersPerMonth(
        val month: String,
        val total: Int
    )
}