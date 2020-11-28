package com.example.native41.database

import androidx.room.Embedded
import androidx.room.Relation

data class CalWithCommitsModel(
    @Embedded val cal: CalModel,
    @Relation(
        parentColumn = "time",
        entityColumn = "cal_model_time"
    )
    val commitDateModels: List<CommitDateModel>
)