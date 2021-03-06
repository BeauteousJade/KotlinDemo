package com.jade.kotlindemo.page.paging3.page

import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.jade.kotlindemo.R
import com.jade.kotlindemo.page.paging3.NetWorkAndDataBaseViewModel
import com.jade.kotlindemo.page.paging3.NetWorkViewModel
import com.jade.kotlindemo.page.paging3.dataBase.Message
import kotlinx.coroutines.flow.Flow

class Paging3WithNetWorkActivity : BasePaging3Activity() {

    @Suppress("UNCHECKED_CAST")
    private val viewModel by viewModels<NetWorkViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>) = NetWorkViewModel() as T
        }
    }

    override fun getLayoutId() = R.layout.activity_paging3

    @ExperimentalPagingApi
    override fun getMessageFlow(): Flow<PagingData<Message>> {
        return viewModel.messageFlow
    }
}