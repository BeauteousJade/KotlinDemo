package com.jade.kotlindemo.page.paging3

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jade.kotlindemo.R
import com.jade.kotlindemo.page.paging3.dataBase.Message
import java.util.*

class CustomAdapter :
    PagingDataAdapter<Message, CustomAdapter.CustomViewHolder>(CustomDiffUtilCallback()) {

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        getItem(position)?.let {
            holder.titleView.text = "${it.title}, position = $position"
            holder.contentView.text = it.content
            holder.itemView.setBackgroundColor(Color.parseColor(generateColor()))
            Log.i("pby123", "itemCount = $itemCount")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_item_view, parent, false)
        )
    }

    private fun generateColor(): String {
        val random = Random()
        val red = Integer.toHexString(random.nextInt(256)).run {
            if (length == 1) "0$this" else this
        }
        val green = Integer.toHexString(random.nextInt(256)).run {
            if (length == 1) "0$this" else this
        }
        val blue = Integer.toHexString(random.nextInt(256)).run {
            if (length == 1) "0$this" else this
        }
        return "#${red}${green}${blue}"
    }


    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView = itemView.findViewById<TextView>(R.id.title)
        val contentView = itemView.findViewById<TextView>(R.id.content)
    }

    class CustomDiffUtilCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.title == newItem.title
        }
    }

}