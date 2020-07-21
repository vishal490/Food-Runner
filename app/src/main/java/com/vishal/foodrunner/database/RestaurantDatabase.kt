package com.vishal.foodrunner.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities=[RestrauEntity::class],version=1,exportSchema = false)
abstract class RestaurantDatabase:RoomDatabase() {
abstract fun resDao():RestrauDao
}