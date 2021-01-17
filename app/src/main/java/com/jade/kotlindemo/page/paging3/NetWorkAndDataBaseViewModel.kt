package com.jade.kotlindemo.page.paging3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.jade.kotlindemo.helper.DataBaseHelper

class NetWorkAndDataBaseViewModel : ViewModel() {
    @ExperimentalPagingApi
    val messageFlow = Pager(PagingConfig(20, jumpThreshold = 100), remoteMediator = CustomRemoteMediator()) {
        DataBaseHelper.dataBase.messageDao().getMessage()
    }.flow.cachedIn(viewModelScope)
}