package com.jade.kotlindemo.page.paging3

import androidx.paging.PagingSource
import com.jade.kotlindemo.page.paging3.dataBase.Message

class CustomPagingSource : PagingSource<Int, Message>() {
    private var count = 1
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> {
        return try {
            val message = Service.create().getMessage(params.pageSize)
            LoadResult.Page(message, null, count++)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}