package com.example.native41

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CalendarViewModel(private val pageId: Int) : ViewModel() {

    val yearMonth by lazy {
        MutableLiveData<String>().apply {
            val year = pageId / 100
            val month = pageId % 100 + 1
            value = "$year $month"
        }
    }

    val items by lazy { App.db.calModelDao().getAll(pageId) }

    class Factory(private val pageId: Int) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CalendarViewModel(pageId) as T
        }
    }
}