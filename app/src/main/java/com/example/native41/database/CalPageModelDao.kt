package com.example.native41.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CalPageModelDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(vararg calPageModel: CalPageModel)

    @Update
    suspend fun updateAll(vararg calPageModel: CalPageModel)

    @Delete
    suspend fun delete(calPageModel: CalPageModel)

    @Transaction
    @Query("SELECT * FROM cal_page_model")
    suspend fun getCalPageWithCalModels(): List<CalPageWithCalModel>

    @Transaction
    @Query("SELECT * FROM cal_page_model")
    fun getCalPageWithCalModelsLiveData(): LiveData<List<CalPageWithCalModel>>
}