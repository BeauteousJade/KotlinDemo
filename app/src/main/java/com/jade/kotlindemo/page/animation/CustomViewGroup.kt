package com.jade.kotlindemo.page.animation

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewParent
import android.widget.FrameLayout

class CustomViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    override fun onDescendantInvalidated(child: View, target: View) {
        super.onDescendantInvalidated(child, target)
        Throwable().printStackTrace()
    }

    override fun invalidate() {
        super.invalidate()

        Throwable().printStackTrace()
    }

    override fun invalidateDrawable(drawable: Drawable) {
        super.invalidateDrawable(drawable)
        Throwable().printStackTrace()

    }

    override fun invalidate(dirty: Rect?) {
        super.invalidate(dirty)
        Throwable().printStackTrace()

    }

    override fun invalidate(l: Int, t: Int, r: Int, b: Int) {
        super.invalidate(l, t, r, b)
        Throwable().printStackTrace()

    }

    override fun invalidateChildInParent(location: IntArray?, dirty: Rect?): ViewParent {
        return super.invalidateChildInParent(location, dirty)
        Throwable().printStackTrace()

    }

    override fun requestLayout() {
        super.requestLayout()
    }
}