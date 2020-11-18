package com.example.native41.database

import androidx.room.Embedded
import androidx.room.Relation

data class CalPageWithCalModel (
    @Embedded val calPage: CalPageModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "page_id"
    )
    val calModels: List<CalModel>
)