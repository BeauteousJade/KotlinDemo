package com.jade.kotlindemo.page.nested

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.OverScroller
import androidx.core.view.NestedScrollingChild3
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import kotlin.math.abs
import kotlin.math.max

class CustomNestedViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr), NestedScrollingChild3 {


    companion object {
        const val INVALID_POINTER = -1
    }


    private var mIsBeingDragged = false
    private var mLastMotionY = 0
    private var mNestedYOffset = 0
    private var mActivePointerId = INVALID_POINTER
    private var mVelocityTracker: VelocityTracker? = null
    private var mLastScrollY = 0

    private val mNestedScrollingChildHelper: NestedScrollingChildHelper =
        NestedScrollingChildHelper(this)
    private val mTouchSlop: Int
    private val mMinimumVelocity: Int
    private val mMaximumVelocity: Int
    private val mOverScroller: OverScroller
    private val mScrollConsumed: IntArray = IntArray(2)
    private val mScrollOffset: IntArray = IntArray(2)

    init {
        isNestedScrollingEnabled = true
        mOverScroller = OverScroller(context)
        val configuration = ViewConfiguration.get(context)
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
                mActivePointerId = ev.getPointerId(0)
                mLastMotionY = ev.y.toInt()
                iniOrResetVelocityTracker()
                mVelocityTracker?.addMovement(ev)
                mOverScroller.computeScrollOffset()
                mIsBeingDragged = !mOverScroller.isFinished
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH)

            }
            MotionEvent.ACTION_MOVE -> {
                val activePointerId = mActivePointerId
                if (activePointerId == INVALID_POINTER) {
                    return mIsBeingDragged
                }
                val pointerIndex = ev.findPointerIndex(activePointerId)
                if (pointerIndex == -1) {
                    return mIsBeingDragged
                }
                val y = ev.getY(pointerIndex).toInt()
                val deltaY = abs(y - mLastMotionY)
                if (deltaY > mTouchSlop) {
                    mIsBeingDragged = true
                    initVelocityTrackerIfNoExists()
                    mVelocityTracker?.addMovement(ev)
                    mNestedYOffset = 0
                    mLastMotionY = y
                    parent?.let {
                        it.requestDisallowInterceptTouchEvent(true)
                    }
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                mIsBeingDragged = false
                mActivePointerId = INVALID_POINTER
                recyclerVelocityTracker()
                stopNestedScroll(ViewCompat.TYPE_TOUCH)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                onSecondaryPointerUp(ev)
            }
        }
        return mIsBeingDragged
    }

    private fun onSecondaryPointerUp(event: MotionEvent) {
        val pointerIndex = event.actionIndex
        val pointerId = event.getPointerId(pointerIndex)
        if (pointerId == mActivePointerId) {
            val newPointerIndex = if (pointerIndex == 0) 1 else 0
            mLastMotionY = event.getY(newPointerIndex).toInt()
            mActivePointerId = event.getPointerId(newPointerIndex)
            mVelocityTracker?.let {
                it.clear()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked
        if (action == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0
        }
        val vtev = MotionEvent.obtain(event)
        vtev.offsetLocation(0f, mNestedYOffset.toFloat())
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (mIsBeingDragged) {
                    parent?.let {
                        it.requestDisallowInterceptTouchEvent(true)
                    }
                }
                abortAnimateScroll()

                mActivePointerId = event.getPointerId(0)
                mLastMotionY = event.y.toInt()
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH)
            }
            MotionEvent.ACTION_MOVE -> {
                Log.i("pby123", "move")
                val pointerIndex = event.findPointerIndex(mActivePointerId)
                if (pointerIndex == -1) {
                    return true
                }

                val y = event.getY(pointerIndex).toInt()
                var deltaY = mLastMotionY - y
                if (!mIsBeingDragged && abs(deltaY) > mTouchSlop) {
                    parent?.let {
                        it.requestDisallowInterceptTouchEvent(true)
                    }
                    mIsBeingDragged = true
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop
                    } else {
                        deltaY += mTouchSlop
                    }
                }
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
                    if (overScrollBy(
                            0,
                            deltaY,
                            0,
                            oldScrollY,
                            0,
                            range,
                            0,
                            0,
                            true
                        ) && !hasNestedScrollingParent(ViewCompat.TYPE_TOUCH)
                    ) {
                        mVelocityTracker?.clear()
                    }
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
                mActivePointerId = INVALID_POINTER
                val velocityTracker = mVelocityTracker
                velocityTracker?.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                // 向上滑动速度为负，向下滑动速度为正
                val initVelocity = velocityTracker?.getYVelocity(mActivePointerId)?.toInt() ?: 0
                if (abs(initVelocity) > mMinimumVelocity) {
                    if (!dispatchNestedPreFling(0F, -initVelocity.toFloat())) {
                        dispatchNestedFling(0F, -initVelocity.toFloat(), true)
                        fling(-initVelocity.toFloat())
                    }
                }
                endDrag()
            }
            MotionEvent.ACTION_CANCEL -> {
                mActivePointerId = INVALID_POINTER
                endDrag()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                val newPointerIndex = event.actionIndex
                mLastMotionY = event.getY(newPointerIndex).toInt()
                mActivePointerId = event.getPointerId(newPointerIndex)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                onSecondaryPointerUp(event)
            }
        }

        mVelocityTracker?.let {
            it.addMovement(vtev)
        }
        vtev.recycle()

        return true
    }


    override fun computeScroll() {
        if (mOverScroller.isFinished) {
            return
        }
        mOverScroller.computeScrollOffset()
        val y = mOverScroller.currY
        var deltaY = y - mLastScrollY
        mLastScrollY = y
        mScrollConsumed[1] = 0
        dispatchNestedPreScroll(0, deltaY, mScrollConsumed, null, ViewCompat.TYPE_NON_TOUCH)
        deltaY -= mScrollConsumed[1]
        val range = getScrollRange()
        if (deltaY != 0) {
            val oldScrollY = scrollY
            overScrollBy(0, deltaY, 0, oldScrollY, 0, range, 0, 0, false)
            val consumedY = scrollY - oldScrollY
            deltaY -= consumedY
            mScrollConsumed[1] = 0
            dispatchNestedScroll(
                0,
                consumedY,
                0,
                deltaY,
                null,
                ViewCompat.TYPE_NON_TOUCH,
                mScrollConsumed
            )
            deltaY -= mScrollConsumed[1]
        }
        if (deltaY != 0) {
            abortAnimateScroll()
        }
        if (!mOverScroller.isFinished) {
            ViewCompat.postInvalidateOnAnimation(this)
        } else {
            abortAnimateScroll()
        }
    }

    private fun iniOrResetVelocityTracker() {
        mVelocityTracker = mVelocityTracker?.let {
            it.clear()
            it
        } ?: kotlin.run {
            VelocityTracker.obtain()
        }
    }

    private fun initVelocityTrackerIfNoExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
    }

    private fun recyclerVelocityTracker() {
        mVelocityTracker = mVelocityTracker?.let {
            it.recycle()
            null
        }
    }

    private fun fling(velocityY: Float) {
        mOverScroller.fling(0, scrollY, 0, velocityY.toInt(), 0, 0, Int.MIN_VALUE, Int.MAX_VALUE)
        runAnimatedScroll()
    }

    private fun runAnimatedScroll() {
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH)
        mLastScrollY = scrollY
        ViewCompat.postInvalidateOnAnimation(this)
    }

    private fun abortAnimateScroll() {
        if (!mOverScroller.isFinished) {
            mOverScroller.abortAnimation()
            stopNestedScroll(ViewCompat.TYPE_NON_TOUCH)
        }
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