package com.jade.kotlindemo.paging3

data class Message(
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
    ) {

    }

    constructor(id: Int, title: String, summary: String, content: String, icon: String) : this(
        id,
        title,
        summary,
        content,
        icon,
        ""
    ) {

    }
}