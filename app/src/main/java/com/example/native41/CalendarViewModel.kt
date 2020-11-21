package com.example.native41

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.native41.database.CalModel
import java.util.*

class CalendarViewModel(private val pageId: Int) : BaseViewModel() {

    val yearMonth by lazy {
        MutableLiveData<String>().apply {
            val year = pageId / 100
            val month = pageId % 100 + 1
            value = "$year $month"
        }
    }

    private val rawItems by lazy { App.db.calModelDao().getAll(pageId) }

    val items: MediatorLiveData<List<CalModel>> by lazy {
        MediatorLiveData<List<CalModel>>().apply {
            addSource(rawItems) { src ->
                src?.let { raw ->
                    val list = mutableListOf<CalModel>()
                    raw.firstOrNull()?.let { first ->
                        val cal = Calendar.getInstance().apply { time = Date(first.time) }
                        while (cal[Calendar.DAY_OF_WEEK] != cal.getActualMinimum(Calendar.DAY_OF_WEEK)) {
                            cal.add(Calendar.DAY_OF_YEAR, -1)
                            list.add(0, CalModel.fromCalendar(cal))
                        }
                    }
                    list.addAll(raw)
                    raw.lastOrNull()?.let { last ->
                        val cal = Calendar.getInstance().apply { time = Date(last.time) }
                        while (cal[Calendar.DAY_OF_WEEK] != cal.getActualMaximum(Calendar.DAY_OF_WEEK)) {
                            cal.add(Calendar.DAY_OF_YEAR, 1)
                            list.add(CalModel.fromCalendar(cal))
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