package com.jade.kotlindemo.page.layoutmanager

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BubbleLayoutManager : RecyclerView.LayoutManager() {

    private var maxLine = -1;
    private var mEnableLayoutLastItem = true


    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (itemCount <= 0) {
            return
        }
        if (state.itemCount <= 0) {
            removeAndRecycleAllViews(recycler)
            return
        }
        detachAndScrapAttachedViews(recycler)
        val sumWidth = width
        var curLineLeft = 0
        var curLineTop = 0
        var lastLineMaxHeight = 0
        var lineCount = 1
        var isLayoutLastItem = false
        val lastChildInfo = getChildInfoByPosition(itemCount - 1, recycler)
        for (position in 0 until itemCount - 1) {
            val childInfo = getChildInfoByPosition(position, recycler)
            val isNewLine = curLineLeft + childInfo.second > sumWidth
            val left = if (isNewLine) 0 else curLineLeft
            if (maxLine == lineCount) {
                if (left + childInfo.second + lastChildInfo.second > sumWidth) {
                    isLayoutLastItem = true
                    break
                }
                val nextChildInfo = getChildInfoByPosition(position + 1, recycler)
                if (left + childInfo.second + nextChildInfo.second > sumWidth) {
                    isLayoutLastItem = true
                }
            }
            if (!isNewLine) {
                lastLineMaxHeight = lastLineMaxHeight.coerceAtLeast(childInfo.third)
                curLineLeft += childInfo.second
            } else {
                curLineTop += lastLineMaxHeight
                curLineLeft += childInfo.second
                lineCount++
            }
            layoutChildInternal(
                childInfo.first,
                left,
                curLineTop,
                left + childInfo.second,
                curLineTop + childInfo.third
            )
            if (isLayoutLastItem) {
                break
            }
        }
        if (isLayoutLastItem || mEnableLayoutLastItem) {
            val isNewLine = curLineLeft + lastChildInfo.second > sumWidth
            val left = if (isNewLine) 0 else curLineLeft
            val top = if (isNewLine) curLineTop + lastLineMaxHeight else curLineLeft
            layoutChildInternal(
                lastChildInfo.first,
                left,
                top,
                left + lastChildInfo.second,
                top + lastChildInfo.third
            )
        }
    }


    private fun getChildInfoByPosition(
        position: Int,
        recycler: RecyclerView.Recycler
    ): Triple<View, Int, Int> {
        val view = recycler.getViewForPosition(position)
        measureChildWithMargins(view, 0, 0)
        val width = getDecoratedMeasuredWidth(view)
        val height = getDecoratedMeasuredHeight(view)
        return Triple(view, width, height)
    }

    private fun layoutChildInternal(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        addView(view)
        layoutDecorated(view, left, top, right, bottom)
    }


    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }
}