package com.example.native41.database

import androidx.room.*

@Dao
interface CalModelDao {
    @Query("SELECT * FROM cal_model")
    suspend fun getAll(): List<CalModel>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(vararg calModel: CalModel)

    @Update
    suspend fun updateAll(vararg calModel: CalModel)

    @Delete
    suspend fun delete(calModel: CalModel)
}