package com.jade.kotlindemo.page.constraint

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class MyConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var mOnMeasureWidthMeasureSpec = 0
    private var mOnMeasureHeightMeasureSpec = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (skipMeasure(widthMeasureSpec, heightMeasureSpec)) {
            return
        }
        mOnMeasureWidthMeasureSpec = widthMeasureSpec
        mOnMeasureHeightMeasureSpec = heightMeasureSpec
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun skipMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int): Boolean {
        if (mDirtyHierarchy) {
            return false
        }
        for (index in 0 until childCount) {
            val child = getChildAt(index)
            if (child.isLayoutRequested && !(child.measuredHeight > 0 && child.measuredWidth > 0)) {
                return false
            }
        }
        if (mOnMeasureWidthMeasureSpec == widthMeasureSpec && mOnMeasureHeightMeasureSpec == heightMeasureSpec) {
            resolveMeasuredDimension(
                widthMeasureSpec, heightMeasureSpec, mLayoutWidget.width, mLayoutWidget.height,
                mLayoutWidget.isWidthMeasuredTooSmall, mLayoutWidget.isHeightMeasuredTooSmall
            )
            return true
        }
        if (mOnMeasureWidthMeasureSpec == widthMeasureSpec && MeasureSpec.getMode(
                widthMeasureSpec
            ) == MeasureSpec.EXACTLY && MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST && MeasureSpec.getMode(
                mOnMeasureHeightMeasureSpec
            ) == MeasureSpec.AT_MOST
        ) {
            val newSize = MeasureSpec.getSize(heightMeasureSpec)
            if (newSize >= mLayoutWidget.height) {
                mOnMeasureWidthMeasureSpec = widthMeasureSpec
                mOnMeasureHeightMeasureSpec = heightMeasureSpec
                resolveMeasuredDimension(
                    widthMeasureSpec,
                    heightMeasureSpec,
                    mLayoutWidget.width,
                    mLayoutWidget.height,
                    mLayoutWidget.isWidthMeasuredTooSmall,
                    mLayoutWidget.isHeightMeasuredTooSmall
                )
                return true
            }
        }
        return false
    }
}