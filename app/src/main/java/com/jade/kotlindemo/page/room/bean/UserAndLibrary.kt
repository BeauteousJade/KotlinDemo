package com.jade.kotlindemo.page.room.bean

import androidx.room.Embedded
import androidx.room.Relation


data class UserAndLibrary(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "library_id"
    )
    val library: Library
)