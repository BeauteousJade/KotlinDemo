/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.R;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.TypedValue;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Defines container for a key frame of for storing KeyAttributes.
 * KeyAttributes change post layout values of a view.
 * @hide
 */

public class KeyAttributes extends Key {
    static final String NAME = "KeyAttribute";
    private static final String TAG = "KeyAttribute";
    private String mTransitionEasing;
    private int mCurveFit = -1;
    private boolean mVisibility = false;
    private float mAlpha = Float.NaN;
    private float mElevation = Float.NaN;
    private float mRotation = Float.NaN;
    private float mRotationX = Float.NaN;
    private float mRotationY = Float.NaN;
    private float mPivotX = Float.NaN;
    private float mPivotY = Float.NaN;
    private float mTransitionPathRotate = Float.NaN;
    private float mScaleX = Float.NaN;
    private float mScaleY = Float.NaN;
    private float mTranslationX = Float.NaN;
    private float mTranslationY = Float.NaN;
    private float mTranslationZ = Float.NaN;
    private float mProgress = Float.NaN;
    public static final int KEY_TYPE = 1;

    {
        mType = KEY_TYPE;
        mCustomConstraints = new HashMap<>();
    }

    @Override
    public void load(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeyAttribute);
        Loader.read(this, a);
    }

    /**
     * Gets the curve fit type this drives the interpolation
     *
     * @return
     */
    int getCurveFit() {
        return mCurveFit;
    }

    @Override
    public void getAttributeNames(HashSet<String> attributes) {

        if (!Float.isNaN(mAlpha)) {
            attributes.add(Key.ALPHA);
        }
        if (!Float.isNaN(mElevation)) {
            attributes.add(Key.ELEVATION);
        }
        if (!Float.isNaN(mRotation)) {
            attributes.add(Key.ROTATION);
        }
        if (!Float.isNaN(mRotationX)) {
            attributes.add(Key.ROTATION_X);
        }
        if (!Float.isNaN(mRotationY)) {
            attributes.add(Key.ROTATION_Y);
        }
        if (!Float.isNaN(mPivotX)) {
            attributes.add(Key.PIVOT_X);
        }
        if (!Float.isNaN(mPivotY)) {
            attributes.add(Key.PIVOT_Y);
        }
        if (!Float.isNaN(mTranslationX)) {
            attributes.add(Key.TRANSLATION_X);
        }
        if (!Float.isNaN(mTranslationY)) {
            attributes.add(Key.TRANSLATION_Y);
        }
        if (!Float.isNaN(mTranslationZ)) {
            attributes.add(Key.TRANSLATION_Z);
        }
        if (!Float.isNaN(mTransitionPathRotate)) {
            attributes.add(Key.TRANSITION_PATH_ROTATE);
        }
        if (!Float.isNaN(mScaleX)) {
            attributes.add(Key.SCALE_X);
        }
        if (!Float.isNaN(mScaleY)) {
            attributes.add(Key.SCALE_Y);
        }
        if (!Float.isNaN(mProgress)) {
            attributes.add(Key.PROGRESS);
        }
        if (mCustomConstraints.size() > 0) {
            for (String s : mCustomConstraints.keySet()) {
                attributes.add(Key.CUSTOM + "," + s);
            }
        }
    }

    public void setInterpolation(HashMap<String, Integer> interpolation) {
        if (mCurveFit == -1) {
            return;
        }
        if (!Float.isNaN(mAlpha)) {
            interpolation.put(Key.ALPHA, mCurveFit);
        }
        if (!Float.isNaN(mElevation)) {
            interpolation.put(Key.ELEVATION, mCurveFit);
        }
        if (!Float.isNaN(mRotation)) {
            interpolation.put(Key.ROTATION, mCurveFit);
        }
        if (!Float.isNaN(mRotationX)) {
            interpolation.put(Key.ROTATION_X, mCurveFit);
        }
        if (!Float.isNaN(mRotationY)) {
            interpolation.put(Key.ROTATION_Y, mCurveFit);
        }
        if (!Float.isNaN(mPivotX)) {
            interpolation.put(Key.PIVOT_X, mCurveFit);
        }
        if (!Float.isNaN(mPivotY)) {
            interpolation.put(Key.PIVOT_Y, mCurveFit);
        }
        if (!Float.isNaN(mTranslationX)) {
            interpolation.put(Key.TRANSLATION_X, mCurveFit);
        }
        if (!Float.isNaN(mTranslationY)) {
            interpolation.put(Key.TRANSLATION_Y, mCurveFit);
        }
        if (!Float.isNaN(mTranslationZ)) {
            interpolation.put(Key.TRANSLATION_Z, mCurveFit);
        }
        if (!Float.isNaN(mTransitionPathRotate)) {
            interpolation.put(Key.TRANSITION_PATH_ROTATE, mCurveFit);
        }
        if (!Float.isNaN(mScaleX)) {
            interpolation.put(Key.SCALE_X, mCurveFit);
        }
        if (!Float.isNaN(mScaleY)) {
            interpolation.put(Key.SCALE_Y, mCurveFit);
        }
        if (!Float.isNaN(mProgress)) {
            interpolation.put(Key.PROGRESS, mCurveFit);
        }
        if (mCustomConstraints.size() > 0) {
            for (String s : mCustomConstraints.keySet()) {
                interpolation.put(Key.CUSTOM + "," + s, mCurveFit);
            }
        }

    }

    @Override
    public void addValues(HashMap<String, SplineSet> splines) {
        for (String s : splines.keySet()) {
            SplineSet splineSet = splines.get(s);
            if (s.startsWith(Key.CUSTOM)) {
                String ckey = s.substring(Key.CUSTOM.length() + 1);
                ConstraintAttribute cvalue = mCustomConstraints.get(ckey);
                if (cvalue != null) {
                    ((SplineSet.CustomSet) splineSet).setPoint(mFramePosition, cvalue);
                }
                continue;
            }
            switch (s) {
                case Key.ALPHA:
                    if (!Float.isNaN(mAlpha)) {
                        splineSet.setPoint(mFramePosition, mAlpha);
                    }
                    break;
                case Key.ELEVATION:
                    if (!Float.isNaN(mElevation)) {
                        splineSet.setPoint(mFramePosition, mElevation);
                    }
                    break;
                case Key.ROTATION:
                    if (!Float.isNaN(mRotation)) {
                        splineSet.setPoint(mFramePosition, mRotation);
                    }
                    break;
                case Key.ROTATION_X:
                    if (!Float.isNaN(mRotationX)) {
                        splineSet.setPoint(mFramePosition, mRotationX);
                    }
                    break;
                case Key.ROTATION_Y:
                    if (!Float.isNaN(mRotationY)) {
                        splineSet.setPoint(mFramePosition, mRotationY);
                    }
                    break;
                case Key.PIVOT_X:
                    if (!Float.isNaN(mRotationX)) {
                        splineSet.setPoint(mFramePosition, mPivotX);
                    }
                    break;
                case Key.PIVOT_Y:
                    if (!Float.isNaN(mRotationY)) {
                        splineSet.setPoint(mFramePosition, mPivotY);
                    }
                    break;
                    case Key.TRANSITION_PATH_ROTATE:
                    if (!Float.isNaN(mTransitionPathRotate)) {
                        splineSet.setPoint(mFramePosition, mTransitionPathRotate);
                    }
                    break;
                case Key.SCALE_X:
                    if (!Float.isNaN(mScaleX)) {
                        splineSet.setPoint(mFramePosition, mScaleX);
                    }
                    break;
                case Key.SCALE_Y:
                    if (!Float.isNaN(mScaleY)) {
                        splineSet.setPoint(mFramePosition, mScaleY);
                    }
                    break;
                case Key.TRANSLATION_X:
                    if (!Float.isNaN(mTranslationX)) {
                        splineSet.setPoint(mFramePosition, mTranslationX);
                    }
                    break;
                case Key.TRANSLATION_Y:
                    if (!Float.isNaN(mTranslationY)) {
                        splineSet.setPoint(mFramePosition, mTranslationY);
                    }
                    break;
                case Key.TRANSLATION_Z:
                    if (!Float.isNaN(mTranslationZ)) {
                        splineSet.setPoint(mFramePosition, mTranslationZ);
                    }
                    break;
                case Key.PROGRESS:
                    if (!Float.isNaN(mProgress)) {
                        splineSet.setPoint(mFramePosition, mProgress);
                    }
                    break;
                default:
                    Log.v("KeyAttributes", "UNKNOWN addValues \"" + s + "\"");
            }
        }
    }

    @Override
    public void setValue(String tag, Object value) {
        switch (tag) {
            case "alpha":
                mAlpha = toFloat(value);
                break;
            case "curveFit":
                mCurveFit = toInt(value);
                break;
            case "elevation":
                mElevation = toFloat(value);
                break;
            case "motionProgress":
                mProgress = toFloat(value);
                break;
            case "rotation":
                mRotation = toFloat(value);
                break;
            case "rotationX":
                mRotationX = toFloat(value);
                break;
            case "rotationY":
                mRotationY = toFloat(value);
                break;
            case "pivotX":
                mPivotX = toFloat(value);
                break;
            case "pivotY":
                mPivotY = toFloat(value);
                break;
            case "scaleX":
                mScaleX = toFloat(value);
                break;
            case "scaleY":
                mScaleY = toFloat(value);
                break;
            case "transitionEasing":
                mTransitionEasing = value.toString();
                break;
            case "visibility":
                mVisibility = toBoolean(value);
                break;
            case "transitionPathRotate":
                mTransitionPathRotate = toFloat(value);
                break;
            case "translationX":
                mTranslationX = toFloat(value);
                break;
            case "translationY":
                mTranslationY = toFloat(value);
                break;
            case "mTranslationZ":
                mTranslationZ = toFloat(value);
                break;
        }
    }

    private static class Loader {
        private static final int ANDROID_ALPHA = 1;
        private static final int ANDROID_ELEVATION = 2;
        private static final int ANDROID_ROTATION = 4;
        private static final int ANDROID_ROTATION_X = 5;
        private static final int ANDROID_ROTATION_Y = 6;
        private static final int TRANSITION_PATH_ROTATE = 8;
        private static final int ANDROID_SCALE_X = 7;
        private static final int TRANSITION_EASING = 9;
        private static final int TARGET_ID = 10;
        private static final int FRAME_POSITION = 12;
        private static final int CURVE_FIT = 13;
        private static final int ANDROID_SCALE_Y = 14;
        private static final int ANDROID_TRANSLATION_X = 15;
        private static final int ANDROID_TRANSLATION_Y = 16;
        private static final int ANDROID_TRANSLATION_Z = 17;
        private static final int PROGRESS = 18;
        private static final int ANDROID_PIVOT_X = 19;
        private static final int ANDROID_PIVOT_Y = 20;

        private static SparseIntArray mAttrMap = new SparseIntArray();

        static {
            mAttrMap.append(R.styleable.KeyAttribute_android_alpha, ANDROID_ALPHA);
            mAttrMap.append(R.styleable.KeyAttribute_android_elevation, ANDROID_ELEVATION);
            mAttrMap.append(R.styleable.KeyAttribute_android_rotation, ANDROID_ROTATION);
            mAttrMap.append(R.styleable.KeyAttribute_android_rotationX, ANDROID_ROTATION_X);
            mAttrMap.append(R.styleable.KeyAttribute_android_rotationY, ANDROID_ROTATION_Y);
            mAttrMap.append(R.styleable.KeyAttribute_android_transformPivotX, ANDROID_PIVOT_X);
            mAttrMap.append(R.styleable.KeyAttribute_android_transformPivotY, ANDROID_PIVOT_Y);
            mAttrMap.append(R.styleable.KeyAttribute_android_scaleX, ANDROID_SCALE_X);
            mAttrMap.append(R.styleable.KeyAttribute_transitionPathRotate, TRANSITION_PATH_ROTATE);
            mAttrMap.append(R.styleable.KeyAttribute_transitionEasing, TRANSITION_EASING);
            mAttrMap.append(R.styleable.KeyAttribute_motionTarget, TARGET_ID);
            mAttrMap.append(R.styleable.KeyAttribute_framePosition, FRAME_POSITION);
            mAttrMap.append(R.styleable.KeyAttribute_curveFit, CURVE_FIT);
            mAttrMap.append(R.styleable.KeyAttribute_android_scaleY, ANDROID_SCALE_Y);
            mAttrMap.append(R.styleable.KeyAttribute_android_translationX, ANDROID_TRANSLATION_X);
            mAttrMap.append(R.styleable.KeyAttribute_android_translationY, ANDROID_TRANSLATION_Y);
            mAttrMap.append(R.styleable.KeyAttribute_android_translationZ, ANDROID_TRANSLATION_Z);
            mAttrMap.append(R.styleable.KeyAttribute_motionProgress, PROGRESS);
        }

        public static void read(KeyAttributes c, TypedArray a) {
            final int N = a.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a.getIndex(i);
                switch (mAttrMap.get(attr)) {
                    case TARGET_ID:
                        if (MotionLayout.IS_IN_EDIT_MODE) {
                            c.mTargetId = a.getResourceId(attr, c.mTargetId);
                            if (c.mTargetId == -1) {
                                c.mTargetString = a.getString(attr);
                            }
                        } else {
                            if (a.peekValue(attr).type == TypedValue.TYPE_STRING) {
                                c.mTargetString = a.getString(attr);
                            } else {
                                c.mTargetId = a.getResourceId(attr, c.mTargetId);
                            }
                        }
                        break;
                    case FRAME_POSITION:
                        c.mFramePosition = a.getInt(attr, c.mFramePosition);
                        break;
                    case ANDROID_ALPHA:
                        c.mAlpha = a.getFloat(attr, c.mAlpha);
                        break;
                    case ANDROID_ELEVATION:
                        c.mElevation = a.getDimension(attr, c.mElevation);
                        break;
                    case ANDROID_ROTATION:
                        c.mRotation = a.getFloat(attr, c.mRotation);
                        break;
                    case CURVE_FIT:
                        c.mCurveFit = a.getInteger(attr, c.mCurveFit);
                        break;
                    case ANDROID_SCALE_X:
                        c.mScaleX = a.getFloat(attr, c.mScaleX);
                        break;
                    case ANDROID_ROTATION_X:
                        c.mRotationX = a.getFloat(attr, c.mRotationX);
                        break;
                    case ANDROID_ROTATION_Y:
                        c.mRotationY = a.getFloat(attr, c.mRotationY);
                        break;
                    case ANDROID_PIVOT_X:
                        c.mPivotX = a.getDimension(attr, c.mPivotX);
                        break;
                    case ANDROID_PIVOT_Y:
                        c.mPivotY = a.getDimension(attr, c.mPivotY);
                        break;
                    case TRANSITION_EASING:
                        c.mTransitionEasing = a.getString(attr);
                        break;
                    case ANDROID_SCALE_Y:
                        c.mScaleY = a.getFloat(attr, c.mScaleY);
                        break;
                    case TRANSITION_PATH_ROTATE:
                        c.mTransitionPathRotate = a.getFloat(attr, c.mTransitionPathRotate);
                        break;
                    case ANDROID_TRANSLATION_X:
                        c.mTranslationX = a.getDimension(attr, c.mTranslationX);
                        break;
                    case ANDROID_TRANSLATION_Y:
                        c.mTranslationY = a.getDimension(attr, c.mTranslationY);
                        break;
                    case ANDROID_TRANSLATION_Z:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            c.mTranslationZ = a.getDimension(attr, c.mTranslationZ);
                        }
                        break;
                    case PROGRESS:
                        c.mProgress = a.getFloat(attr, c.mProgress);
                        break;
                    default:
                        Log.e(NAME, "unused attribute 0x" + Integer.toHexString(attr) + "   " + mAttrMap.get(attr));
                        break;
                }
            }
        }
    }
}
