package com.example.native41.database

import androidx.room.*
import com.example.native41.network.CommitModel
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.*

@Entity(
    tableName = "commit_date_model",
    indices = [Index("cal_model_time")]
)
data class CommitDateModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "cal_model_time") val calModelTime: Long,
    @ColumnInfo(name = "page_id") val pageId: Int,
    @ColumnInfo(name = "committer_date") val committerDate: Long,
    @ColumnInfo(name = "message") val message: String,
) {
    companion object {

        private val logger = LoggerFactory.getLogger(CommitDateModel::javaClass.name)

        private const val isoFormat = "yyyy-MM-dd'T'HH:mm:ssX"

        fun fromCommit(commitModel: CommitModel): CommitDateModel {
            val apiDate = commitModel.commit.committer.date
            val date = convertApiToDate(apiDate)
            return CommitDateModel(
                0,
                trimDate(date).time,
                convertToPageId(date),
                date.time,
                commitModel.commit.message
            )
        }

        private fun convertToPageId(date: Date): Int {
            return Calendar.getInstance().apply {
                time = date
            }.let {
                it[Calendar.YEAR] * 100 + it[Calendar.DAY_OF_MONTH]
            }
        }

        private fun convertApiToDate(apiDate: String): Date {
            kotlin.runCatching {
                SimpleDateFormat(isoFormat, Locale.US).parse(apiDate)
            }.onSuccess {
                return it ?: Date()
            }.onFailure {
                logger.error("SimpleDateFormat parse", it)
            }
            return Date()
        }

        private const val pattern = "yyyy/MM/dd"

        private fun trimDate(date: Date): Date {
            kotlin.runCatching {
                SimpleDateFormat(pattern, Locale.getDefault()).let { simpleDateFormat ->
                    simpleDateFormat.format(date).let { text ->
                        simpleDateFormat.parse(text)
                    }
                }
            }.onSuccess {
                return it
            }.onFailure {
                logger.error("SimpleDateFormat parse", it)
            }
            return Date()
        }

    }
}