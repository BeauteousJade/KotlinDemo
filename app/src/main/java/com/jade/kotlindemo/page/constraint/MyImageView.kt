package com.jade.kotlindemo.page.constraint

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView

class MyImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.i("pby123", "MyImageView onMeasure")
    }
}