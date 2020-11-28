package com.example.native41.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CommitDateModelDao {
    @Insert
    suspend fun insertAll(vararg commitDateModel: CommitDateModel)

    @Delete
    suspend fun delete(commitDateModel: CommitDateModel)

    @Query("SELECT * FROM commit_date_model")
    suspend fun getAll(): List<CommitDateModel>

    @Query("SELECT * FROM commit_date_model")
    fun getAllLiveData(): LiveData<List<CommitDateModel>>

    @Transaction
    suspend fun deleteAll() {
        getAll().toList().forEach {
            delete(it)
        }
    }
}