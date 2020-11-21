package com.example.native41

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.native41.database.AppDatabase
import com.example.native41.database.CalModel
import com.example.native41.database.CalPageModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*

@RunWith(AndroidJUnit4::class)
class DatabasePagerItemsTest {
    private lateinit var db: AppDatabase

    @Before
    fun before() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "test_database"
        ).build()
    }

    private suspend fun insertAll() {
        val timeZone = TimeZone.getDefault()
        val locale = Locale.getDefault()
        val cal = Calendar.getInstance(timeZone, locale).apply {
            SimpleDateFormat("yyyy/MM/dd").parse("2000/01/01")?.let {
                time = it
            }
        }
        val year = Calendar.getInstance(timeZone, locale).apply {
            time = Date()
        }.let {
            it[Calendar.YEAR]
        }
        val list = mutableListOf<CalModel>()
        while (cal[Calendar.YEAR] in 2000..year) {
            list.add(CalModel.fromCalendar(cal))
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        db.calModelDao().insertAll(*list.toTypedArray())
    }

    @Test
    fun getPagerItems() = runBlocking {
        db.clearAllTables()
        insertAll()
        val pageIds = db.calModelDao().getPageIds()
        Assert.assertNotEquals("pageIds check zero.", 0, pageIds.size)
        val calPageModels = mutableListOf<CalPageModel>()
        pageIds.forEach { e -> calPageModels.add(CalPageModel(e)) }
        db.calPageModelDao().insertAll(*calPageModels.toTypedArray())
        val list = db.calPageModelDao().getCalPageWithCalModels()
        Assert.assertNotEquals("list size check", 0, list.size)
    }

    @After
    fun after() {
        db.clearAllTables()
    }
}