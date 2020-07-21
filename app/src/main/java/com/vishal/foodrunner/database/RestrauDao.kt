package com.vishal.foodrunner.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
@Dao
interface RestrauDao {

    @Insert
    fun insertRestrau(resEntity:RestrauEntity)

    @Delete
    fun deleteRestrau(resEntity:RestrauEntity)

    @Query("DELETE FROM Restaurant" )
    fun deleteAll()

    @Query("SELECT * FROM Restaurant")
    fun getAllData():List<RestrauEntity>

    @Query("SELECT * FROM Restaurant WHERE res_id=:resId")
    fun getRestrauById(resId: String):RestrauEntity
}
