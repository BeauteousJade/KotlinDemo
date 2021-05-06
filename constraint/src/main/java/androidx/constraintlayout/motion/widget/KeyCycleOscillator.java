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

import android.annotation.TargetApi;
import android.os.Build;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.motion.utils.CurveFit;
import androidx.constraintlayout.motion.utils.Oscillator;
import android.util.Log;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Provide the engine for executing cycles.
 *
 * @hide
 */
public abstract class KeyCycleOscillator {
    private static final String TAG = "KeyCycleOscillator";
    private CurveFit mCurveFit;
    private CycleOscillator mCycleOscillator;
    protected ConstraintAttribute mCustom; // used if it manipulates a custom attribute
    private String mType;
    private int mWaveShape = 0;
    public int mVariesBy = 0; // 0 = position, 2=path
    ArrayList<WavePoint> mWavePoints = new ArrayList<>();

    public boolean variesByPath() {
        return mVariesBy == 1;
    }

    static class WavePoint {
        int mPosition;
        float mValue;
        float mOffset;
        float mPeriod;

        public WavePoint(int position, float period, float offset, float value) {
            mPosition = position;
            mValue = value;
            mOffset = offset;
            mPeriod = period;
        }
    }

    @Override
    public String toString() {
        String str = mType;
        DecimalFormat df = new DecimalFormat("##.##");
        for (WavePoint wp : mWavePoints) {
            str += "[" + wp.mPosition + " , " + df.format(wp.mValue) + "] ";
        }
        return str;
    }

    public void setType(String type) {
        mType = type;
    }

    public abstract void setProperty(View view, float t);

    public float get(float t) {
        return (float) mCycleOscillator.getValues(t);
    }

    public float getSlope(float position) {
        return (float) mCycleOscillator.getSlope(position);
    }

    public CurveFit getCurveFit() {
        return mCurveFit;
    }

    static KeyCycleOscillator makeSpline(String str) {
        if (str.startsWith(Key.CUSTOM)) {
            return new CustomSet();
        }
        switch (str) {
            case Key.ALPHA:
                return new AlphaSet();
            case Key.ELEVATION:
                return new ElevationSet();
            case Key.ROTATION:
                return new RotationSet();
            case Key.ROTATION_X:
                return new RotationXset();
            case Key.ROTATION_Y:
                return new RotationYset();
            case Key.TRANSITION_PATH_ROTATE:
                return new PathRotateSet();
            case Key.SCALE_X:
                return new ScaleXset();
            case Key.SCALE_Y:
                return new ScaleYset();
            case Key.WAVE_OFFSET:
                return new AlphaSet();
            case Key.WAVE_VARIES_BY:
                return new AlphaSet();
            case Key.TRANSLATION_X:
                return new TranslationXset();
            case Key.TRANSLATION_Y:
                return new TranslationYset();
            case Key.TRANSLATION_Z:
                return new TranslationZset();
            case Key.PROGRESS:
                return new ProgressSet();
            default:
                return null;
        }
    }

    /**
     * sets a oscillator wave point
     *
     * @param framePosition the position
     * @param variesBy      only varies by path supported for now
     * @param period        the period of the wave
     * @param offset        the offset value
     * @param value         the adder
     * @param custom        The ConstraintAttribute used to set the value
     */
    public void setPoint(int framePosition, int shape, int variesBy, float period, float offset,
                         float value, ConstraintAttribute custom) {
        mWavePoints.add(new WavePoint(framePosition, period, offset, value));
        if (variesBy != -1) {
            mVariesBy = variesBy;
        }
        mWaveShape = shape;
        mCustom = custom;
    }

    /**
     * sets a oscillator wave point
     *
     * @param framePosition the position
     * @param variesBy      only varies by path supported for now
     * @param period        the period of the wave
     * @param offset        the offset value
     * @param value         the adder
     */
    public void setPoint(int framePosition, int shape, int variesBy, float period, float offset, float value) {
        mWavePoints.add(new WavePoint(framePosition, period, offset, value));
        if (variesBy != -1) {
            mVariesBy = variesBy;
        }
        mWaveShape = shape;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setup(float pathLength) {
        int count = mWavePoints.size();
        if (count == 0) {
            return;
        }
        Collections.sort(mWavePoints, new Comparator<WavePoint>() {
            @Override
            public int compare(WavePoint lhs, WavePoint rhs) {
                return Integer.compare(lhs.mPosition, rhs.mPosition);
            }
        });
        double[] time = new double[count];
        double[][] values = new double[count][2];
        mCycleOscillator = new CycleOscillator(mWaveShape, mVariesBy, count);
        int i = 0;
        for (WavePoint wp : mWavePoints) {
            time[i] = wp.mPeriod * 1E-2;
            values[i][0] = wp.mValue;
            values[i][1] = wp.mOffset;
            mCycleOscillator.setPoint(i, wp.mPosition, wp.mPeriod, wp.mOffset, wp.mValue);
            i++;
        }
        mCycleOscillator.setup(pathLength);
        mCurveFit = CurveFit.get(CurveFit.SPLINE, time, values);
    }

    static class ElevationSet extends KeyCycleOscillator {
        @Override
        public void setProperty(View view, float t) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setElevation(get(t));
            }
        }
    }

    static class AlphaSet extends KeyCycleOscillator {
        @Override
        public void setProperty(View view, float t) {
            view.setAlpha(get(t));
        }
    }

    static class RotationSet extends KeyCycleOscillator {
        @Override
        public void setProperty(View view, float t) {
            view.setRotation(get(t));
        }
    }

    static class RotationXset extends KeyCycleOscillator {
        @Override
        public void setProperty(View view, float t) {
            view.setRotationX(get(t));
        }
    }

    static class RotationYset extends KeyCycleOscillator {
        @Override
        public void setProperty(View view, float t) {
            view.setRotationY(get(t));
        }
    }

    static class PathRotateSet extends KeyCycleOscillator {
        @Override
        public void setProperty(View view, float t) {
        }

        public void setPathRotate(View view, float t, double dx, double dy) {
            view.setRotation(get(t) + (float) Math.toDegrees(Math.atan2(dy, dx)));
        }
    }

    static class ScaleXset extends KeyCycleOscillator {
        @Override
        public void setProperty(View view, float t) {
            view.setScaleX(get(t));
        }
    }

    static class ScaleYset extends KeyCycleOscillator {
        @Override
        public void setProperty(View view, float t) {
            view.setScaleY(get(t));
        }
    }

    static class TranslationXset extends KeyCycleOscillator {
        @Override
        public void setProperty(View view, float t) {
            view.setTranslationX(get(t));
        }
    }

    static class TranslationYset extends KeyCycleOscillator {
        @Override
        public void setProperty(View view, float t) {
            view.setTranslationY(get(t));
        }
    }

    static class TranslationZset extends KeyCycleOscillator {
        @Override
        public void setProperty(View view, float t) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setTranslationZ(get(t));
            }
        }
    }

    static class CustomSet extends KeyCycleOscillator {
        float[] value = new float[1];

        @Override
        public void setProperty(View view, float t) {
            value[0] = get(t);
            mCustom.setInterpolatedValue(view, value);
        }
    }

    static class ProgressSet extends KeyCycleOscillator {
        boolean mNoMethod = false;

        @Override
        public void setProperty(View view, float t) {
            if (view instanceof MotionLayout) {
                ((MotionLayout) view).setProgress(get(t));
            } else {
                if (mNoMethod) {
                    return;
                }
                Method method = null;
                try {
                    method = view.getClass().getMethod("setProgress", Float.TYPE);
                } catch (NoSuchMethodException e) {
                    mNoMethod = true;
                }
                if (method != null) {
                    try {
                        method.invoke(view, get(t));
                    } catch (IllegalAccessException e) {
                        Log.e(TAG, "unable to setProgress", e);
                    } catch (InvocationTargetException e) {
                        Log.e(TAG, "unable to setProgress", e);
                    }
                }
            }
        }
    }

    private static class IntDoubleSort {
        static void sort(int[] key, float[] value, int low, int hi) {
            int[] stack = new int[key.length + 10];
            int count = 0;
            stack[count++] = hi;
            stack[count++] = low;
            while (count > 0) {
                low = stack[--count];
                hi = stack[--count];
                if (low < hi) {
                    int p = partition(key, value, low, hi);
                    stack[count++] = p - 1;
                    stack[count++] = low;
                    stack[count++] = hi;
                    stack[count++] = p + 1;
                }
            }
        }

        private static int partition(int[] array, float[] value, int low, int hi) {
            int pivot = array[hi];
            int i = low;
            for (int j = low; j < hi; j++) {
                if (array[j] <= pivot) {
                    swap(array, value, i, j);
                    i++;
                }
            }
            swap(array, value, i, hi);
            return i;
        }

        private static void swap(int[] array, float[] value, int a, int b) {
            int tmp = array[a];
            array[a] = array[b];
            array[b] = tmp;
            float tmpv = value[a];
            value[a] = value[b];
            value[b] = tmpv;
        }
    }

    private static class IntFloatFloatSort {
        static void sort(int[] key, float[] value1, float[] value2, int low, int hi) {
            int[] stack = new int[key.length + 10];
            int count = 0;
            stack[count++] = hi;
            stack[count++] = low;
            while (count > 0) {
                low = stack[--count];
                hi = stack[--count];
                if (low < hi) {
                    int p = partition(key, value1, value2, low, hi);
                    stack[count++] = p - 1;
                    stack[count++] = low;
                    stack[count++] = hi;
                    stack[count++] = p + 1;
                }
            }
        }

        private static int partition(int[] array, float[] value1, float[] value2, int low, int hi) {
            int pivot = array[hi];
            int i = low;
            for (int j = low; j < hi; j++) {
                if (array[j] <= pivot) {
                    swap(array, value1, value2, i, j);
                    i++;
                }
            }
            swap(array, value1, value2, i, hi);
            return i;
        }

        private static void swap(int[] array, float[] value1, float[] value2, int a, int b) {
            int tmp = array[a];
            array[a] = array[b];
            array[b] = tmp;
            float tmpFloat = value1[a];
            value1[a] = value1[b];
            value1[b] = tmpFloat;
            tmpFloat = value2[a];
            value2[a] = value2[b];
            value2[b] = tmpFloat;
        }
    }

    static class CycleOscillator {
        static final int UNSET = ConstraintLayout.LayoutParams.UNSET;
        private static final String TAG = "CycleOscillator";
        private final int mVariesBy;
        Oscillator mOscillator = new Oscillator();
        float[] mValues;
        double[] mPosition;
        float[] mPeriod;
        float[] mOffset; // offsets will be spline interpolated
        float[] mScale; // scales will be spline interpolated
        int mWaveShape;
        CurveFit mCurveFit;
        double[] mSplineValueCache; // for the return value of the curve fit
        double[] mSplineSlopeCache; // for the return value of the curve fit
        float mPathLength;

        CycleOscillator(int waveShape, int variesBy, int steps) {
            mWaveShape = waveShape;
            mVariesBy = variesBy;
            mOscillator.setType(waveShape);
            mValues = new float[steps];
            mPosition = new double[steps];
            mPeriod = new float[steps];
            mOffset = new float[steps];
            mScale = new float[steps];
        }

        public double getValues(float time) {
            if (mCurveFit != null) {
                mCurveFit.getPos(time, mSplineValueCache);
            } else { // only one value no need to interpolate
                mSplineValueCache[0] = mOffset[0];
                mSplineValueCache[1] = mValues[0];
            }
            double offset = mSplineValueCache[0];
            double waveValue = mOscillator.getValue(time);
            return offset + waveValue * mSplineValueCache[1];
        }

        public double getSlope(float time) {
            if (mCurveFit != null) {
                mCurveFit.getSlope(time, mSplineSlopeCache);
                mCurveFit.getPos(time, mSplineValueCache);
            } else { // only one value no need to interpolate
                mSplineSlopeCache[0] = 0;
                mSplineSlopeCache[1] = 0;
            }
            double waveValue = mOscillator.getValue(time);
            double waveSlope = mOscillator.getSlope(time);
            return mSplineSlopeCache[0] + waveValue * mSplineSlopeCache[1] + waveSlope * mSplineValueCache[1];
        }

        public HashMap<String, ConstraintAttribute> mCustomConstraints = new HashMap<>();

        private ConstraintAttribute get(String attributeName, ConstraintAttribute.AttributeType attributeType) {
            ConstraintAttribute ret;
            if (mCustomConstraints.containsKey(attributeName)) {
                ret = mCustomConstraints.get(attributeName);
                if (ret.getType() != attributeType) {
                    throw new IllegalArgumentException(
                            "ConstraintAttribute is already a " + ret.getType().name());
                }
            } else {
                ret = new ConstraintAttribute(attributeName, attributeType);
                mCustomConstraints.put(attributeName, ret);
            }
            return ret;
        }

        /**
         * @param index
         * @param framePosition
         * @param wavePeriod
         * @param offset
         * @param values
         */
        public void setPoint(int index, int framePosition, float wavePeriod, float offset, float values) {
            mPosition[index] = framePosition / 100.0;
            mPeriod[index] = wavePeriod;
            mOffset[index] = offset;
            mValues[index] = values;
        }

        public void setup(float pathLength) {
            mPathLength = pathLength;
            double[][] splineValues = new double[mPosition.length][2];
            mSplineValueCache = new double[1 + mValues.length];
            mSplineSlopeCache = new double[1 + mValues.length];
            if (mPosition[0] > 0) {
                mOscillator.addPoint(0, mPeriod[0]);
            }
            int last = mPosition.length - 1;
            if (mPosition[last] < 1.0f) {
                mOscillator.addPoint(1, mPeriod[last]);
            }
            for (int i = 0; i < splineValues.length; i++) {
                splineValues[i][0] = mOffset[i];
                for (int j = 0; j < mValues.length; j++) {
                    splineValues[j][1] = mValues[j];
                }
                mOscillator.addPoint(mPosition[i], mPeriod[i]);
            }
            // TODO: add mVariesBy and get total time and path length
            mOscillator.normalize();
            if (mPosition.length > 1) {
                mCurveFit = CurveFit.get(CurveFit.SPLINE, mPosition, splineValues);
            } else {
                mCurveFit = null;
            }
        }
    }
}