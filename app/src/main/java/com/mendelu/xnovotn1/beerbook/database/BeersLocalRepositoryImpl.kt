package com.mendelu.xnovotn1.beerbook.database

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BeersLocalRepositoryImpl @Inject constructor(private val beersDao: BeersDao): IBeersLocalRepository {
    override suspend fun insertData(beer: Beer) {
        return beersDao.insertData(beer)
    }

    override suspend fun update(beer: Beer) {
        beersDao.update(beer)
    }

    override suspend fun delete(beer: Beer) {
       beersDao.delete(beer)
    }

    override fun getCount(): Int {
        return beersDao.getCount()
    }

    override fun getMostDrankBeer(): String {
        return beersDao.getMostDrankBeer()
    }

    override fun getAvgRating(): Float? {
        return beersDao.getAvgRating()
    }

    override fun getAll(): Flow<List<Beer>> {
        return beersDao.getAll()
    }

    override suspend fun getById(id: Long): Beer {
        return beersDao.getById(id)
    }

    override suspend fun changeBeerState(id: Long, state: Boolean) {
        beersDao.changeBeerState(id, state)
    }
}