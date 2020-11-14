package com.example.native41.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "cal_model")
data class CalModel(
    @PrimaryKey val time: Long,
    @ColumnInfo(name = "date") val date: Int,
    @ColumnInfo(name = "day_of_month") val dayOfMonth: Int,
    @ColumnInfo(name = "day_of_week") val dayOfWeek: Int,
    @ColumnInfo(name = "day_of_week_in_month") val dayOfWeekInMonth: Int,
    @ColumnInfo(name = "day_of_year") val dayOfYear: Int,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "week_of_month") val weekOfMonth: Int,
    @ColumnInfo(name = "week_of_year") val weekOfYear: Int,
    @ColumnInfo(name = "year") val year: Int,
) {
    companion object {
        fun fromCalendar(cal: Calendar):CalModel {
            return CalModel(
                cal.time.time,
                cal[Calendar.DATE],
                cal[Calendar.DAY_OF_MONTH],
                cal[Calendar.DAY_OF_WEEK],
                cal[Calendar.DAY_OF_WEEK_IN_MONTH],
                cal[Calendar.DAY_OF_YEAR],
                cal[Calendar.MONTH],
                cal[Calendar.WEEK_OF_MONTH],
                cal[Calendar.WEEK_OF_YEAR],
                cal[Calendar.YEAR]
            )
        }
    }
}