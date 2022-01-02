package com.jade.kotlindemo.page.recycler

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jade.kotlindemo.R
import com.jade.kotlindemo.helper.Utils
import com.jade.kotlindemo.helper.recyclerview.MyAdapter

class StableAdapter(private val list: MutableList<String>) : MyAdapter(list) {

    companion object {
        const val STABLE_ITEM = 1
    }

    private var mStablePosition = -1
    private var mRecyclerView: RecyclerView? = null


    fun setStablePosition(position: Int) {
        mStablePosition = position
    }


    fun setRecyclerView(recyclerView: RecyclerView) {
        mRecyclerView = recyclerView;
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        if (viewType == STABLE_ITEM) {
            return MyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_stable_item_view, parent, false)
            )
        }

        return super.onCreateViewHolder(parent, viewType)
    }

    @ExperimentalStdlibApi
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (getItemViewType(position) == STABLE_ITEM) {
            val color = Color.parseColor(Utils.generateColor())
            holder.itemView.setBackgroundColor(color)
            holder.titleView.text = list[position]
            holder.itemView.findViewById<View>(R.id.stable_item_child)?.setBackgroundColor(color)
            holder.itemView.findViewById<View>(R.id.stable_item_child)?.setOnClickListener {
                val size = itemCount
                val removeList = ArrayList<String>()
                for (index in mStablePosition + 1 until size) {
                    removeList.add(list[index])
                }
                list.removeAll(removeList)
                notifyItemRangeRemoved(mStablePosition + 1, size - mStablePosition)
                val newList = buildList {
                    for (index in 0..30) {
                        add("refresh Item, position = $index")
                    }
                }
                list.addAll(newList)
//                notifyDataSetChanged()
                notifyItemRangeInserted(mStablePosition + 1, newList.size)
                mRecyclerView?.post {
                    mRecyclerView?.scrollToPosition(mStablePosition)
                }
            }
            return
        }
        super.onBindViewHolder(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        if (position == mStablePosition) {
            return STABLE_ITEM
        }
        return super.getItemViewType(position)
    }

}