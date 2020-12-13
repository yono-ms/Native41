package com.example.native41

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.native41.database.AppDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

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
    fun getAll() = runBlocking {
        assertNotNull("db is null", db)
        val list = db.commitEntityDao().getAll()
        assertEquals("getAll check zero.", 0, list.size)
    }

    @After
    fun after() {
        db.clearAllTables()
    }
}