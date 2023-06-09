package com.jiwon.mpteam_layout

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RestaurantData::class],
    version = 1
)

abstract class RestaurantDatabase: RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDAO

    companion object {
        private var INSTANCE: RestaurantDatabase? = null

        fun getDatabase(context: Context): RestaurantDatabase {
            val tempInstance = INSTANCE

            if(tempInstance != null) {
                return tempInstance
            }
            val instance = Room.databaseBuilder(
                context,
                RestaurantDatabase::class.java,
                "restaurantdb"
            ).build()

            INSTANCE = instance
            return instance
        }
    }
}