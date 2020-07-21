package com.vishal.foodrunner.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DishDao {

    @Insert
    fun insertDish(dishEntity:DishEntity)

    @Delete
    fun deleteDish(dishEntity:DishEntity)

    @Query("DELETE FROM Dishes" )
    fun deleteAll()

    @Query("SELECT * FROM Dishes")
    fun getAllData():List<DishEntity>

    @Query("SELECT * FROM Dishes WHERE dish_id=:dishId")
    fun getDishById(dishId: String):DishEntity
}
