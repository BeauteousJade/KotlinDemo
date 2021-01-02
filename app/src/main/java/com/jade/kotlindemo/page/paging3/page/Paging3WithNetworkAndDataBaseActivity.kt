package com.jade.kotlindemo.page.paging3.page

import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.jade.kotlindemo.R
import com.jade.kotlindemo.page.paging3.NetWorkAndDataBaseViewModel
import com.jade.kotlindemo.page.paging3.dataBase.Message
import kotlinx.coroutines.flow.Flow

class Paging3WithNetworkAndDataBaseActivity : BasePaging3Activity() {

    @Suppress("UNCHECKED_CAST")
    val viewModel by viewModels<NetWorkAndDataBaseViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>) =
                NetWorkAndDataBaseViewModel() as T

        }
    }

    override fun getLayoutId() = R.layout.activity_paging3

    @ExperimentalPagingApi
    override fun getMessageFlow(): Flow<PagingData<Message>> {
        return viewModel.messageFlow
    }
}