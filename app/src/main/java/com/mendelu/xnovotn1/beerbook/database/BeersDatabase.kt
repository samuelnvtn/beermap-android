package com.mendelu.xnovotn1.beerbook.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Beer::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BeersDatabase : RoomDatabase() {

    abstract fun beersDao() : BeersDao

    companion object {
        private var INSTANCE: BeersDatabase? = null
        fun getDatabase(context: Context): BeersDatabase {
            if (INSTANCE == null) {
                synchronized(BeersDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            BeersDatabase::class.java,
                            "beers_database"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

}