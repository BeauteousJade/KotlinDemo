package com.jade.kotlindemo.page.room.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Library(
    @PrimaryKey(
        autoGenerate = true
    )
    @ColumnInfo(
        name = "library_id"
    )
    val libraryId: Int,
    @ColumnInfo(
        name = "user_owner_id"
    )
    val userOwnerId: Int
)