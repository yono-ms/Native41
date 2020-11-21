package com.example.native41.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CalModelDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(vararg calModel: CalModel)

    @Update
    suspend fun updateAll(vararg calModel: CalModel)

    @Delete
    suspend fun delete(calModel: CalModel)

    @Query("SELECT * FROM cal_model ORDER BY time")
    suspend fun getAll(): List<CalModel>

    @Query("SELECT * FROM cal_model WHERE page_id = :pageId ORDER BY time")
    fun getAll(pageId: Int): LiveData<List<CalModel>>

    @Query("SELECT DISTINCT page_id FROM cal_model ORDER BY time")
    suspend fun getPageIds(): List<Int>

    @Query("SELECT DISTINCT page_id FROM cal_model ORDER BY time")
    fun getPageIdsLiveData(): LiveData<List<Int>>
}