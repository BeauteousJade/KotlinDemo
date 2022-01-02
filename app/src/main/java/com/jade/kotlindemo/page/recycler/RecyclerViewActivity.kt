package com.jade.kotlindemo.page.recycler

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jade.kotlindemo.R

class RecyclerViewActivity : AppCompatActivity() {

    companion object {
        const val STABLE_POSITION = 5
    }

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        val containerView = findViewById<ViewGroup>(R.id.container)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.itemAnimator = null
        recyclerView.adapter = StableAdapter(buildList {
            for (i in 0..30) {
                if (i != STABLE_POSITION) {
                    add("position = $i")
                } else {
                    add("position = $i, for stable")
                }
            }
        }.toMutableList()).apply {
            setStablePosition(STABLE_POSITION)
            setRecyclerView(recyclerView)
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            private var mPreShow = false

            private val recyclerViewLocation = IntArray(2)
            private val stableItemLocation = IntArray(2)

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val stableItemView = recyclerView.findViewById<ViewGroup>(R.id.stable_item)
                val currentShow = stableItemView != null
                stableItemView?.let {
                    recyclerView.getLocationOnScreen(recyclerViewLocation)
                    stableItemView.getLocationOnScreen(stableItemLocation)
                    var stableItemViewChild =
                        stableItemView.findViewById<View>(R.id.stable_item_child)
                    if (stableItemLocation[1] < recyclerViewLocation[1] && stableItemViewChild != null) {
                        stableItemView.removeView(stableItemViewChild)
                        stableItemView.layoutParams = stableItemView.layoutParams.apply {
                            height = stableItemViewChild.height
                        }
                        containerView.addView(stableItemViewChild)
                    } else if (stableItemLocation[1] >= recyclerViewLocation[1] && stableItemViewChild == null) {
                        stableItemViewChild = containerView.findViewById(R.id.stable_item_child)
                        containerView.removeView(stableItemViewChild)
                        stableItemView.addView(stableItemViewChild)
                        stableItemView.layoutParams = stableItemView.layoutParams.apply {
                            height = ViewGroup.LayoutParams.WRAP_CONTENT
                        }
                    }
                }
                if (mPreShow != currentShow) {
                    mPreShow = currentShow
                }
                Log.i("pby123", "onScrolled, stableItemView = $stableItemView")
            }
        })
    }
}