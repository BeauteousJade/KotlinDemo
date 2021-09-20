package com.jade.kotlindemo.page.nested

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.core.view.NestedScrollingChild3
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import kotlin.math.abs
import kotlin.math.max

class CustomNestedViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr), NestedScrollingChild3 {

    private var mIsBeingDragged = false
    private var mLastMotionY = 0
    private var mNestedYOffset = 0

    private val mNestedScrollingChildHelper: NestedScrollingChildHelper =
        NestedScrollingChildHelper(this)
    private val mTouchSlop: Int
    private val mMinimumVelocity: Int
    private val mMaximumVelocity: Int

    private val mScrollConsumed: IntArray = IntArray(2)
    private val mScrollOffset: IntArray = IntArray(2)

    init {
        isNestedScrollingEnabled = true
        val configuration = ViewConfiguration.get(getContext())
        mTouchSlop = configuration.scaledTouchSlop
        mMinimumVelocity = configuration.scaledMinimumFlingVelocity
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for (i in 0 until childCount) {
            measureChildWithMargins(
                getChildAt(i),
                widthMeasureSpec,
                paddingTop + paddingBottom,
                heightMeasureSpec,
                paddingLeft + paddingRight
            )
        }
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var useHeight = paddingTop
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as MarginLayoutParams
            val left = lp.leftMargin
            val right = left + child.measuredWidth
            val top = useHeight + lp.topMargin
            val bottom = top + child.measuredHeight
            child.layout(left, top, right, bottom)
            useHeight += child.measuredHeight + lp.topMargin + lp.bottomMargin
        }
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.actionMasked
        if (action == MotionEvent.ACTION_MOVE && mIsBeingDragged) {
            return true
        }

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mLastMotionY = ev.y.toInt()
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH)

            }
            MotionEvent.ACTION_MOVE -> {
                val y = ev.y.toInt()
                val deltaY = abs(y - mLastMotionY)
                if (deltaY > mTouchSlop) {
                    mIsBeingDragged = true
                    mNestedYOffset = 0
                    mLastMotionY = y
                    parent?.let {
                        parent.requestDisallowInterceptTouchEvent(true)
                    }
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                mIsBeingDragged = false
                stopNestedScroll(ViewCompat.TYPE_TOUCH)
            }
        }
        Log.i("pby123", "mIsBeingDragged = $mIsBeingDragged")
        return mIsBeingDragged
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked
        if (action == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0
        }
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mLastMotionY = event.y.toInt()
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH)
            }
            MotionEvent.ACTION_MOVE -> {
                val y = event.y.toInt()
                var deltaY = mLastMotionY - y
                if (!mIsBeingDragged && abs(deltaY) > mTouchSlop) {
                    parent?.let {
                        requestDisallowInterceptTouchEvent(true)
                    }
                    mIsBeingDragged = true
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop
                    } else {
                        deltaY += mTouchSlop
                    }
                }
                Log.i("pby123", "deltalY = $deltaY")
                if (mIsBeingDragged) {
                    if (dispatchNestedPreScroll(
                            0,
                            deltaY,
                            mScrollConsumed,
                            mScrollOffset,
                            ViewCompat.TYPE_TOUCH
                        )
                    ) {
                        deltaY -= mScrollConsumed[1]
                        mNestedYOffset += mScrollOffset[1]
                    }
                    mLastMotionY = y - mScrollOffset[1]

                    val oldScrollY = scrollY
                    val range = getScrollRange()
                    overScrollBy(0, deltaY, 0, oldScrollY, 0, range, 0, 0, true)
                    val scrollDeltaY = scrollY - oldScrollY
                    val unconsumedY = deltaY - scrollDeltaY
                    mScrollConsumed[1] = 0
                    dispatchNestedScroll(
                        0,
                        scrollDeltaY,
                        0,
                        unconsumedY,
                        mScrollOffset,
                        ViewCompat.TYPE_TOUCH,
                        mScrollConsumed
                    )
                    mLastMotionY -= mScrollOffset[1]
                    mNestedYOffset += mScrollOffset[1]
                }
            }
            MotionEvent.ACTION_UP -> {
                endDrag()
            }
            MotionEvent.ACTION_CANCEL -> {
                endDrag()
            }
        }

        return true
    }


    override fun computeVerticalScrollRange(): Int {

        val child = getChildAt(childCount - 1)
        val lp = child.layoutParams as MarginLayoutParams

        return child.bottom + lp.bottomMargin
    }

    override fun computeVerticalScrollOffset(): Int {
        return max(0, super.computeVerticalScrollOffset())
    }


    private fun endDrag() {
        mIsBeingDragged = false
        stopNestedScroll(ViewCompat.TYPE_TOUCH)
    }

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        super.scrollTo(scrollX, scrollY)
    }

    private fun getScrollRange(): Int {
        val child = getChildAt(childCount - 1)
        val lp = child.layoutParams as MarginLayoutParams
        // scrollTo 会改变View 的 bottom?
        // TODO jade
        val childSize = getChildAt(childCount - 1).bottom + lp.topMargin + lp.bottomMargin
        val parentSpace = height - paddingTop - paddingBottom
        return max(0, childSize - parentSpace)
    }


    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    // NestedScrollingChild
    override fun startNestedScroll(axes: Int): Boolean {
        return startNestedScroll(axes, ViewCompat.TYPE_TOUCH)
    }

    // NestedScrollingChild
    override fun hasNestedScrollingParent(): Boolean {
        return hasNestedScrollingParent(ViewCompat.TYPE_TOUCH)
    }

    // NestedScrollingChild
    override fun isNestedScrollingEnabled(): Boolean {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled
    }

    // NestedScrollingChild
    override fun dispatchNestedFling(
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    // NestedScrollingChild
    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY)
    }

    // NestedScrollingChild
    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?
    ): Boolean {
        return dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, ViewCompat.TYPE_TOUCH)
    }

    // NestedScrollingChild
    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?
    ): Boolean {
        return dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow,
            ViewCompat.TYPE_TOUCH
        )
    }

    // NestedScrollingChild
    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mNestedScrollingChildHelper.isNestedScrollingEnabled = enabled
    }

    // NestedScrollingChild
    override fun stopNestedScroll() {
        stopNestedScroll(ViewCompat.TYPE_TOUCH)
    }


    // NestedScrollingChild2
    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return mNestedScrollingChildHelper.startNestedScroll(axes, type)
    }

    // NestedScrollingChild2
    override fun stopNestedScroll(type: Int) {
        mNestedScrollingChildHelper.stopNestedScroll(type)
    }

    // NestedScrollingChild2
    override fun hasNestedScrollingParent(type: Int): Boolean {
        return mNestedScrollingChildHelper.hasNestedScrollingParent(type)
    }

    // NestedScrollingChild2
    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return mNestedScrollingChildHelper.dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow
        )
    }

    // NestedScrollingChild2
    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(
            dx,
            dy,
            consumed,
            offsetInWindow,
            type
        )
    }

    // NestedScrollingChild3
    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int,
        consumed: IntArray
    ) {
        mNestedScrollingChildHelper.dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow,
            type,
            consumed
        )
    }


}