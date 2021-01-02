package com.jade.kotlindemo.page.paging3.dataBase

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MessageDao {

    @Query("select * from message")
    fun getMessage(): PagingSource<Int, Message>

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    fun insertMessage(messages: List<Message>)

    @Query("delete from message")
    fun clearMessage()
}