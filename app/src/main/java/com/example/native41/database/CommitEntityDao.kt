package com.example.native41.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.native41.calendar.YearMonth

@Dao
interface CommitEntityDao {
    @Insert
    suspend fun insertAll(vararg commitEntity: CommitEntity)

    @Delete
    suspend fun delete(commitEntity: CommitEntity)

    @Query("SELECT * FROM commit_entity")
    suspend fun getAll(): List<CommitEntity>

    @Query("SELECT * FROM commit_entity")
    fun getAllLiveData(): LiveData<List<CommitEntity>>

    @Query("SELECT * FROM commit_entity WHERE year=:year AND month=:month")
    fun getAllLiveData(year: Int, month: Int): LiveData<List<CommitEntity>>

    @Query("SELECT * FROM commit_entity WHERE year=:year AND month=:month AND day_of_month=:dayOfMonth")
    fun getAllLiveData(year: Int, month: Int, dayOfMonth: Int): LiveData<List<CommitEntity>>

    @Query("SELECT DISTINCT year, month FROM commit_entity ORDER BY year, month")
    fun getYearMonthLiveData(): LiveData<List<YearMonth>>

    @Transaction
    suspend fun deleteAll() {
        getAll().toList().forEach { delete(it) }
    }
}