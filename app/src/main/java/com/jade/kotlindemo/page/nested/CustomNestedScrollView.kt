package com.jade.kotlindemo.page.nested

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.core.widget.NestedScrollView

class CustomNestedScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : NestedScrollView(context, attrs) {


    override fun canScrollVertically(direction: Int): Boolean {
        Log.i("pby123", "direction = $direction")
        return super.canScrollVertically(direction)
    }
}