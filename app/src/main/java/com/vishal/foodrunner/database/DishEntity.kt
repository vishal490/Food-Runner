package com.vishal.foodrunner.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Dishes")
data class DishEntity(
    @PrimaryKey val dish_id:Int,
    @ColumnInfo(name = "dish_name")val dishName:String,
    @ColumnInfo(name = "dish_price")val dishPrice:String
)