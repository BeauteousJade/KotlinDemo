package com.jade.kotlindemo.page.paging3

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.jade.kotlindemo.helper.DataBaseHelper
import com.jade.kotlindemo.page.paging3.dataBase.Message

@ExperimentalPagingApi
class CustomRemoteMediator : RemoteMediator<Int, Message>() {

    private val mMessageDao = DataBaseHelper.dataBase.messageDao()
    private var count = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Message>
    ): MediatorResult {
        val startIndex = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(true)
            LoadType.APPEND -> {
                val stringBuilder = StringBuilder()
                state.pages.forEach {
                    stringBuilder.append("size = ${it.data.size}, count = ${it.data.count()}\n")
                }
                Log.i("pby123", stringBuilder.toString())
                count += state.config.initialLoadSize
                count
            }
        }
        val pages = state.pages
        var totalCount = 0
        if(pages.isNotEmpty()){
            pages.forEach {
                totalCount += it.data.size + it.itemsBefore + it.itemsAfter
            }
        }

        Log.i("pby123", "CustomRemoteMediator,loadType = $loadType, totalCount = $totalCount")
        return try {
            val messages = Service.create().getMessage(state.config.initialLoadSize, startIndex)
            DataBaseHelper.dataBase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    mMessageDao.clearMessage()
                }
                mMessageDao.insertMessage(messages)
            }
            MediatorResult.Success(messages.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }

    }
}