package com.example.native41.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CalWithCommitsModelDao {
    @Transaction
    @Query("SELECT * FROM cal_model WHERE page_id = :pageId ORDER BY time")
    fun getAll(pageId: Int): LiveData<List<CalWithCommitsModel>>
}