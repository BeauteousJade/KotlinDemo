package com.jade.kotlindemo.page.paging3.page

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.jade.kotlindemo.R
import com.jade.kotlindemo.helper.DataBaseHelper
import com.jade.kotlindemo.page.paging3.NetWorkAndDataBaseViewModel
import com.jade.kotlindemo.page.paging3.dataBase.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class Paging3WithNetworkAndDataBaseActivity : BasePaging3Activity() {

    @Suppress("UNCHECKED_CAST")
    val viewModel by viewModels<NetWorkAndDataBaseViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>) =
                NetWorkAndDataBaseViewModel() as T

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.button).setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                DataBaseHelper.dataBase.messageDao().clearMessage()
            }
        }
    }

    override fun getLayoutId() = R.layout.activity_paging3_with_data_base

    @ExperimentalPagingApi
    override fun getMessageFlow(): Flow<PagingData<Message>> {
        return viewModel.messageFlow
    }
}