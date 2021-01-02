package com.jade.kotlindemo.page.paging3

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.jade.kotlindemo.helper.DataBaseHelper
import com.jade.kotlindemo.page.paging3.dataBase.Message
import java.lang.StringBuilder

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
                count += 20
                count
            }
        }
        val messages = Service.create().getMessage(20, startIndex)
        DataBaseHelper.dataBase.withTransaction {
            Log.i("pby123", "loadType = $loadType")
            if (loadType == LoadType.REFRESH) {
                mMessageDao.clearMessage()
            }
            mMessageDao.insertMessage(messages)
        }
        return MediatorResult.Success(messages.isEmpty())
    }
}