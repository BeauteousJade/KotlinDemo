package com.jade.kotlindemo.page.room.bean

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(
        name = "user_id"
    )
    val userId: Int = 0,
    @ColumnInfo(
        name = "name"
    )
    val name: String,
    @ColumnInfo(
        name = "age"
    )
    val age: Int,
    @ColumnInfo(
        name = "sex"
    )
    val sex: Int
)