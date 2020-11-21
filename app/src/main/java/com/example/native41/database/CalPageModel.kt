package com.example.native41.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cal_page_model")
data class CalPageModel(
    @PrimaryKey val id: Int,
)