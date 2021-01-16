package com.jade.kotlindemo.page.paging3.page

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jade.kotlindemo.R
import com.jade.kotlindemo.page.paging3.CustomAdapter
import com.jade.kotlindemo.page.paging3.dataBase.Message
import kotlinx.android.synthetic.main.activity_paging3.*
import kotlinx.coroutines.flow.*

abstract class BasePaging3Activity : AppCompatActivity() {

    protected lateinit var mAdapter: CustomAdapter
    private lateinit var mMessageFlow: Flow<PagingData<Message>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.refreshLayout)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        mMessageFlow = getMessageFlow()
        mAdapter= CustomAdapter()
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        lifecycleScope.launchWhenCreated {
            mMessageFlow.collectLatest {
                mAdapter.submitData(it)
            }
        }
        lifecycleScope.launchWhenCreated {
            mAdapter.loadStateFlow.collectLatest {
                refreshLayout.isRefreshing = it.refresh is LoadState.Loading
            }
        }
        lifecycleScope.launchWhenCreated {
            mAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { recyclerView.scrollToPosition(0) }
        }
        swipeRefreshLayout.setOnRefreshListener {
            mAdapter.refresh()
        }
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun getMessageFlow(): Flow<PagingData<Message>>
}