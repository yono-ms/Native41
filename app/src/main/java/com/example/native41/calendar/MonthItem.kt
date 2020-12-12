package com.example.native41.calendar

import android.graphics.Color

data class MonthItem(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val dayOfWeek: Int,
    var isCommitted: Boolean = false
) {
    fun dayOfMonthString(): String {
        return dayOfMonth.toString()
    }

    fun dayOfWeekColor(): Int {
        return when (dayOfWeek) {
            1 -> Color.RED
            7 -> Color.BLUE
            else -> Color.BLACK
        }
    }
}