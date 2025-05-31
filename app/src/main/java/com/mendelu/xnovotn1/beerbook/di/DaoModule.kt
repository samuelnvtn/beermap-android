package com.mendelu.xnovotn1.beerbook.di

import com.mendelu.xnovotn1.beerbook.database.BeersDao
import com.mendelu.xnovotn1.beerbook.database.BeersDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideDao(database: BeersDatabase): BeersDao {
        return database.beersDao()
    }
}
