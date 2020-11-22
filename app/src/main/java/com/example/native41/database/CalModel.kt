package com.example.native41.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.native41.R
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
    @ColumnInfo(name = "page_id") val pageId: Int,
    @ColumnInfo(name = "padding") val padding: Boolean,
) {
    companion object {
        fun fromCalendar(cal: Calendar, padding: Boolean = false): CalModel {
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
                cal[Calendar.YEAR],
                cal[Calendar.YEAR] * 100 + cal[Calendar.MONTH],
                padding,
            )
        }
    }

    val textColor: Int
        get() {
            return if (padding) {
                R.color.dark_gray
            } else {
                when (dayOfWeek) {
                    Calendar.SUNDAY -> R.color.sunday
                    Calendar.SATURDAY -> R.color.saturday
                    else -> R.color.weekday
                }
            }
        }

    val background: Int
        get() {
            return if (padding) {
                R.color.gray
            } else {
                R.color.white
            }
        }
}