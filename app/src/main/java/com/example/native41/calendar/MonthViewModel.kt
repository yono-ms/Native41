package com.example.native41.calendar

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.native41.App
import com.example.native41.BaseViewModel
import com.example.native41.database.CommitEntity
import com.example.native41.toYearMonth
import java.util.*

class MonthViewModel(private val year: Int, private val month: Int) : BaseViewModel() {

    class Factory(private val year: Int, private val month: Int) :
        ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MonthViewModel(year, month) as T
        }
    }

    val yearMonth by lazy { MutableLiveData(getYearMonth()) }

    private fun getYearMonth(): String {
        return Calendar.getInstance(Locale.getDefault()).apply {
            set(year, month, 1)
        }.time.toYearMonth()
    }

    private val commits by lazy { App.db.commitEntityDao().getAllLiveData(year, month) }

    val items by lazy {
        MediatorLiveData<List<MonthItem>>().apply {
            fun mediate(commits: List<CommitEntity>) {
                val cal = Calendar.getInstance().apply { set(year, month, 1) }
                logger.debug("cal=$cal")
                val startWeek = cal[Calendar.WEEK_OF_YEAR]
                logger.debug("startWeek=$startWeek")
                val lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
                logger.debug("lastDayOfMonth=$lastDayOfMonth")
                cal.set(year, month, lastDayOfMonth)
                val lastWeek =
                    if (cal[Calendar.WEEK_OF_YEAR] == 1) cal.getActualMaximum(Calendar.WEEK_OF_YEAR) else cal[Calendar.WEEK_OF_YEAR]
                logger.debug("lastWeek=$lastWeek")
                cal.set(Calendar.DAY_OF_YEAR, 1)
                val list = mutableListOf<MonthItem>()
                while (cal[Calendar.YEAR] == year) {
                    if (cal[Calendar.WEEK_OF_YEAR] in startWeek..lastWeek) {
                        list.add(
                            MonthItem(
                                cal[Calendar.YEAR],
                                cal[Calendar.MONTH],
                                cal[Calendar.DAY_OF_MONTH],
                                cal[Calendar.DAY_OF_WEEK],
                                commits.any { e ->
                                    e.year == cal[Calendar.YEAR] && e.month == cal[Calendar.MONTH] && e.dayOfMonth == cal[Calendar.DAY_OF_MONTH]
                                }
                            )
                        )
                    }
                    cal.add(Calendar.DAY_OF_YEAR, 1)
                }
                value = list
            }
            addSource(commits) {
                mediate(it)
            }
        }
    }
}