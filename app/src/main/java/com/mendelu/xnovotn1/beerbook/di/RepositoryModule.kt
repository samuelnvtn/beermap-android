package com.mendelu.xnovotn1.beerbook.di

import com.mendelu.xnovotn1.beerbook.database.BeersDao
import com.mendelu.xnovotn1.beerbook.database.BeersLocalRepositoryImpl
import com.mendelu.xnovotn1.beerbook.database.IBeersLocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(dao: BeersDao): IBeersLocalRepository {
        return BeersLocalRepositoryImpl(dao)
    }
}
