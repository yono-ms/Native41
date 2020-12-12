package com.example.native41

import android.text.format.DateFormat
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.*

/**
 * convert ISO format date text to Date instance.
 */
fun String.fromIsoToDate(): Date {
    val isoFormat = "yyyy-MM-dd'T'HH:mm:ssX"
    kotlin.runCatching {
        SimpleDateFormat(isoFormat, Locale.US).parse(this)
    }.onSuccess {
        return it ?: Date()
    }.onFailure {
        val logger = LoggerFactory.getLogger("DateExtension")
        logger.error("SimpleDateFormat parse", it)
    }
    return Date()
}

fun Date.toYearMonth(): String {
    kotlin.runCatching {
        val locale = Locale.getDefault()
        val cal = Calendar.getInstance(locale).apply { time = this@toYearMonth }
        val pattern = DateFormat.getBestDateTimePattern(locale, "yyyyMMM")
        SimpleDateFormat(pattern, locale).format(cal.time)
    }.onSuccess {
        return it
    }.onFailure {
        val logger = LoggerFactory.getLogger("DateExtension")
        logger.error("SimpleDateFormat parse", it)
        return it.message ?: ""
    }
    return ""
}

fun Date.toBestString(): String {
    kotlin.runCatching {
        val locale = Locale.getDefault()
        val cal = Calendar.getInstance(locale).apply { time = this@toBestString }
        val pattern = DateFormat.getBestDateTimePattern(locale, "yyyyMMMdEEEHHmmss")
        SimpleDateFormat(pattern, locale).format(cal.time)
    }.onSuccess {
        return it
    }.onFailure {
        val logger = LoggerFactory.getLogger("DateExtension")
        logger.error("SimpleDateFormat parse", it)
        return it.message ?: ""
    }
    return ""
}