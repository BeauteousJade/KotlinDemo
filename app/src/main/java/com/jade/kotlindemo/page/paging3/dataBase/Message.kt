package com.jade.kotlindemo.page.paging3.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "message"
)
data class Message(
    @PrimaryKey
    var id: Int,
    var title: String,
    var summary: String,
    var content: String,
    var icon: String,
    var key: String = ""
) {
    constructor(id: Int, title: String, summary: String, icon: String) : this(
        id,
        title,
        summary,
        "",
        icon,
        ""
    )

    constructor(id: Int, title: String, summary: String, content: String, icon: String) : this(
        id,
        title,
        summary,
        content,
        icon,
        ""
    )

    constructor(id: Int, title: String) : this(id, title, "", "", "", "")
}