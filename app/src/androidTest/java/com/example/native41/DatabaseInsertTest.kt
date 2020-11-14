package com.example.native41

import android.util.Log
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.native41.database.AppDatabase
import com.example.native41.database.CalModel
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*

@RunWith(AndroidJUnit4::class)
class DatabaseInsertTest {
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

    @Test
    fun insertAll() = runBlocking {
        assertNotNull("db is null", db)
        assertEquals("getAll check zero.", 0, db.calModelDao().getAll().size)

        val timeZone = TimeZone.getDefault()
        val locale = Locale.getDefault()
        Log.e("TEST", "$locale $timeZone")
        val cal = Calendar.getInstance(timeZone, locale).apply {
            SimpleDateFormat("yyyy/MM/dd").parse("2000/01/01")?.let {
                time = it
            }
        }
        Log.e("TEST", cal.toString())
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

        val start = Date().time

        db.calModelDao().insertAll(*list.toTypedArray())

        val elapse = Date().time - start
        Log.e("TEST", "elapse=$elapse")

        assertNotEquals("getAll check zero.", 0, db.calModelDao().getAll().size)
    }

    @After
    fun after() {
        db.clearAllTables()
    }
}