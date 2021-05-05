package com.jade.kotlindemo.page.constraint

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout

class MyConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        for (index in 0 until childCount) {
            val child = getChildAt(index)
            Log.i(
                "pby123",
                "index = $index, height = ${child.measuredHeight}, width = ${child.measuredWidth}"
            )
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}