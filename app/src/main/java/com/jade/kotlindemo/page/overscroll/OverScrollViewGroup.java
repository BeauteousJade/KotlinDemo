package com.jade.kotlindemo.page.overscroll;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewCompat.ScrollAxis;
import androidx.core.view.ViewCompat.NestedScrollType;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 支持过渡滑动的ViewGroup，用以包裹RecyclerView。
 */
public final class OverScrollViewGroup extends FrameLayout implements NestedScrollingParent3 {

    private static final float RATIO = 2f;

    private boolean mIsStartScrollByTouch = false;
    private boolean mIsStartScrollByNonTouch = false;
    private ValueAnimator mValueAnimator;
    private OnTargetViewOffsetListener mOnTargetViewOffsetListener;

    private final NestedScrollingParentHelper mNestedScrollingParentHelper =
            new NestedScrollingParentHelper(this);

    public OverScrollViewGroup(@NonNull Context context) {
        super(context);
    }

    public OverScrollViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OverScrollViewGroup(@NonNull Context context, @Nullable AttributeSet attrs,
                               int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public OverScrollViewGroup(@NonNull Context context, @Nullable AttributeSet attrs,
                               int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 设置 targetView的位置监听。
     */
    public void setOnTargetViewOffsetListener(
            @Nullable OnTargetViewOffsetListener onTargetViewOffsetListener) {
        mOnTargetViewOffsetListener = onTargetViewOffsetListener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAnimator();
    }

    private void cancelAnimator() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator.removeAllListeners();
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, @ViewCompat.ScrollAxis int nestedScrollAxes) {
        return onStartNestedScroll(child, target, nestedScrollAxes, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target,
                                       @ScrollAxis int axes, @NestedScrollType int type) {
        mIsStartScrollByTouch = mIsStartScrollByTouch || type == ViewCompat.TYPE_TOUCH;
        mIsStartScrollByNonTouch = mIsStartScrollByNonTouch || type == ViewCompat.TYPE_NON_TOUCH;
        // 只处理水平滑动
        return (axes & ViewCompat.SCROLL_AXIS_HORIZONTAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target,
                                       @ScrollAxis int axes) {
        onNestedScrollAccepted(child, target, axes, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target,
                                       @ScrollAxis int axes, @NestedScrollType int type) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes, type);
    }


    @Override
    public void onStopNestedScroll(@NonNull View child) {
        onStopNestedScroll(child, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, @NestedScrollType int type) {
        mNestedScrollingParentHelper.onStopNestedScroll(target, type);
        if (type == ViewCompat.TYPE_TOUCH) {
            mIsStartScrollByTouch = false;
        }
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            mIsStartScrollByNonTouch = false;
        }
        // 当TYPE_TOUCH 和 TYPE_NON_TOUCH都停止了，就重置RecyclerView的位置。
        if (!mIsStartScrollByTouch && !mIsStartScrollByNonTouch && target.getTranslationX() != 0) {
            resetTarget(target);
        }
    }

    /**
     * TODO 如果需要修改重置的动画，可以修改这里。
     * 重置targetView 的位置。
     */
    private void resetTarget(@NonNull View target) {
        final float translationX = target.getTranslationX();
        cancelAnimator();
        final boolean isNegative = translationX < 0;
        mValueAnimator = ValueAnimator.ofFloat(antiConvertOffset(target, translationX), 0);
        mValueAnimator.setDuration(200);
        mValueAnimator.setInterpolator(new DecelerateInterpolator());
        mValueAnimator.addUpdateListener(animation -> {
            Float value = (Float) animation.getAnimatedValue();
            dispatchTargetViewOffsetChange(target, convertOffset(target, value) * (isNegative ? -1 : 1));
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dispatchTargetViewOffsetChange(target, 0);
            }
        });
        mValueAnimator.start();
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        onNestedPreScroll(target, dx, dy, consumed, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed,
                                  @NestedScrollType int type) {
        // dx > 0 从右往左滑动。<-
        // dx < 0 从左往右滑动。->
        int translationX = (int) target.getTranslationX();
        // 漏出右侧边缘，并且RecyclerView向右滑动。
        if (translationX > 0 && dx > 0) {
            int consumedX = translationX - dx <= 0 ? translationX : dx;
            consumed[0] = consumedX;
            dispatchTargetViewOffsetChange(target, translationX - consumedX);
        } else if (translationX < 0 && dx < 0) {
            // 漏出左侧边缘，并且RecyclerView向左滑动。
            int consumedX = translationX - dx >= 0 ? translationX : dx;
            consumed[0] = consumedX;
            dispatchTargetViewOffsetChange(target, translationX - consumedX);
        }
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed) {
        onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed, @NestedScrollType int type) {
        onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, new int[2]);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @NestedScrollType int type, @NonNull int[] consumed) {
        Log.i("pby123", "dxUnconsumed = " + dxUnconsumed);
        final int translationX = (int) target.getTranslationX();
        if (translationX >= 0 && dxUnconsumed < 0) {
            consumed[0] = dxUnconsumed;
            final float realOffset = -antiConvertOffset(target, translationX);
            dispatchTargetViewOffsetChange(target, convertOffset(target, realOffset + dxUnconsumed));
        } else if (translationX <= 0 && dxUnconsumed > 0) {
            consumed[0] = dxUnconsumed;
            final float realOffset = antiConvertOffset(target, translationX);
            dispatchTargetViewOffsetChange(target, -convertOffset(target, realOffset + dxUnconsumed));
        }
        // 如果已经滚动不了了，强制停止滑动。
        if (translationX != 0 && Math.abs(Math.abs(translationX) - Math.abs(target.getTranslationX())) <= 5
                && type == ViewCompat.TYPE_NON_TOUCH) {
            consumed[0] = 0;
        }
    }

    /**
     * TODO 如果需要修改阻尼函数，就修改这里。注意要对应的修改antiConvertOffset方法。
     * <p>
     * 使用转换函数，将offset 转为真实的Offset。
     *
     * @see #antiConvertOffset(View, float) 此方法与本方法是计算方式相反的。
     */
    private int convertOffset(@NonNull View target, float realOffset) {
        final float width = target.getWidth() / 1f;
        return (int) (width - RATIO * width * width / (Math.abs(realOffset) + RATIO * width));
    }

    /**
     * 使用反转换函数，将真实的Offset转为offset。
     *
     * @see #convertOffset(View, float) 此方法与本方法是计算方式相反的。
     */
    private int antiConvertOffset(@NonNull View target, float convertOffset) {
        final float width = target.getWidth() / 1f;
        return (int) (RATIO * width * width / (width - Math.abs(convertOffset)) - RATIO * width);
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY,
                                 boolean consumed) {
        return false;
    }

    private void dispatchTargetViewOffsetChange(@NonNull View target, int offset) {
        target.setTranslationX(offset);
        if (mOnTargetViewOffsetListener != null) {
            mOnTargetViewOffsetListener.onOffsetChange(offset);
        }
    }

    /**
     * targetView 位置变换。
     */
    public interface OnTargetViewOffsetListener {
        void onOffsetChange(int offset);
    }
}
