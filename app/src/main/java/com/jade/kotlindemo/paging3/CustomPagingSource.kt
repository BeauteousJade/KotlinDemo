package com.jade.kotlindemo.paging3

import androidx.paging.PagingSource

class CustomPagingSource : PagingSource<Int, Message>() {
    private var count = 1
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> {
        return try {
            val message = Service.create().getMessage(params.loadSize)
            LoadResult.Page(message, null, count++)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}