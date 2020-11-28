package com.example.native41

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.native41.database.CalModel
import com.example.native41.database.CalWithCommitsModel
import java.util.*

class CalendarViewModel(private val pageId: Int) : BaseViewModel() {

    val yearMonth by lazy {
        MutableLiveData<String>().apply {
            val year = pageId / 100
            val month = pageId % 100 + 1
            value = "$year $month"
        }
    }

    private val rawItems by lazy { App.db.calWithCommitsModelDao().getAll(pageId) }

    val items: MediatorLiveData<List<CalWithCommitsModel>> by lazy {
        MediatorLiveData<List<CalWithCommitsModel>>().apply {
            addSource(rawItems) { src ->
                src?.let { raw ->
                    val list = mutableListOf<CalWithCommitsModel>()
                    raw.firstOrNull()?.let { first ->
                        val cal = Calendar.getInstance().apply { time = Date(first.cal.time) }
                        while (cal[Calendar.DAY_OF_WEEK] != cal.getActualMinimum(Calendar.DAY_OF_WEEK)) {
                            cal.add(Calendar.DAY_OF_YEAR, -1)
                            list.add(
                                0,
                                CalWithCommitsModel(CalModel.fromCalendar(cal, true), listOf())
                            )
                        }
                    }
                    list.addAll(raw)
                    raw.lastOrNull()?.let { last ->
                        val cal = Calendar.getInstance().apply { time = Date(last.cal.time) }
                        while (cal[Calendar.DAY_OF_WEEK] != cal.getActualMaximum(Calendar.DAY_OF_WEEK)) {
                            cal.add(Calendar.DAY_OF_YEAR, 1)
                            list.add(
                                CalWithCommitsModel(
                                    CalModel.fromCalendar(cal, true),
                                    listOf()
                                )
                            )
                        }
                    }
                    value = list
                }
            }
        }
    }

    init {
        rawItems.observeForever { logger.info("rawItems changed.") }
    }

    class Factory(private val pageId: Int) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CalendarViewModel(pageId) as T
        }
    }
}