package com.jade.kotlindemo.room.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    @ColumnInfo(
        name = "user_id"
    )
    val userId: String,
    @ColumnInfo(
        name = "age"
    )
    val age: Int,
    @ColumnInfo(
        name = "sex"
    )
    val sex: Int
)