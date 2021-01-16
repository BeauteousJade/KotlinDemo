package com.jade.kotlindemo.page.paging3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class NetWorkViewModel : ViewModel() {
    val messageFlow = Pager(PagingConfig(20, enablePlaceholders = true)) {
        CustomPagingSource()
    }.flow.cachedIn(viewModelScope)
}