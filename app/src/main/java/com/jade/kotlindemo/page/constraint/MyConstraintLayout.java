package com.jade.kotlindemo.page.constraint;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MyConstraintLayout extends ConstraintLayout {

    private int mOnMeasureWidthMeasureSpec = 0;
    private int mOnMeasureHeightMeasureSpec = 0;

    public MyConstraintLayout(@NonNull Context context) {
        super(context);
    }

    public MyConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (skipMeasure(widthMeasureSpec, heightMeasureSpec)) {
            return;
        }
        mOnMeasureWidthMeasureSpec = widthMeasureSpec;
        mOnMeasureHeightMeasureSpec = heightMeasureSpec;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private boolean skipMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mDirtyHierarchy) {
            return false;
        }
        final int childCount = getChildCount();
        for (int index = 0; index < childCount; index++) {
            View child = getChildAt(index);
            if (child.isLayoutRequested() && !(child.getMeasuredHeight() > 0 && child.getMeasuredWidth() > 0)) {
                return false;
            }
        }
        if (mOnMeasureWidthMeasureSpec == widthMeasureSpec && mOnMeasureHeightMeasureSpec == heightMeasureSpec) {
            resolveMeasuredDimension(widthMeasureSpec, heightMeasureSpec, mLayoutWidget.getWidth(), mLayoutWidget.getHeight(), mLayoutWidget.isWidthMeasuredTooSmall(), mLayoutWidget.isHeightMeasuredTooSmall());
            return true;
        }
        if (mOnMeasureWidthMeasureSpec == widthMeasureSpec && MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY && MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST && MeasureSpec.getMode(mOnMeasureHeightMeasureSpec) == MeasureSpec.AT_MOST) {
            int newSize = MeasureSpec.getSize(heightMeasureSpec);
            if (newSize >= mLayoutWidget.getHeight()) {
                mOnMeasureWidthMeasureSpec = widthMeasureSpec;
                mOnMeasureHeightMeasureSpec = heightMeasureSpec;
                resolveMeasuredDimension(
                        widthMeasureSpec,
                        heightMeasureSpec,
                        mLayoutWidget.getWidth(),
                        mLayoutWidget.getHeight(),
                        mLayoutWidget.isWidthMeasuredTooSmall(),
                        mLayoutWidget.isHeightMeasuredTooSmall()
                );
                return true;
            }
        }
        return false;
    }
}
