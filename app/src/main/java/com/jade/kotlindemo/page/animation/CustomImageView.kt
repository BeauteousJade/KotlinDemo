package com.jade.kotlindemo.page.animation

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import com.jade.kotlindemo.R

class CustomImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mDrawable: Drawable? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        if (mDrawable == null) {
            mDrawable = context.getDrawable(R.drawable.ic_launcher_background)
        }
        setImageDrawable(mDrawable)
        super.onDraw(canvas)
    }
}