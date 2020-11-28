package com.example.native41

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.native41.database.CalModel
import com.example.native41.database.CalPageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class SplashViewModel : BaseViewModel() {

    companion object {
        const val pattern = "yyyy/MM/dd"
        const val startDate = "2015/01/01"
    }

    val busy by lazy { MutableLiveData(false) }
    val initialized: MutableLiveData<Unit> by lazy { MutableLiveData(null) }

    fun initialize() {
        logger.info("initialize")
        if (busy.value == true) {
            logger.info("...busy.")
            return
        }
        busy.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                App.db.clearAllTables()
                val cal = Calendar.getInstance().apply {
                    SimpleDateFormat(pattern, Locale.getDefault()).parse(startDate)?.let {
                        time = it
                    }
                }
                val startYear = cal[Calendar.YEAR]
                val todayCal = Calendar.getInstance().apply { time = Date() }
                val thisYear = todayCal[Calendar.YEAR]
                val thisMonth = todayCal[Calendar.MONTH]
                val list = mutableListOf<CalModel>()
                while (cal[Calendar.YEAR] in startYear..thisYear) {
                    if (cal[Calendar.YEAR] == thisYear && cal[Calendar.MONTH] > thisMonth) {
                        break
                    }
                    list.add(CalModel.fromCalendar(cal))
                    cal.add(Calendar.DAY_OF_YEAR, 1)
                }
                App.db.calModelDao().insertAll(*list.toTypedArray())
                val pageList = mutableListOf<CalPageModel>()
                App.db.calModelDao().getPageIds().forEach { e -> pageList.add(CalPageModel(e)) }
                App.db.calPageModelDao().insertAll(*pageList.toTypedArray())

                initialized.postValue(Unit)
                busy.postValue(false)
            }
        }
    }
}