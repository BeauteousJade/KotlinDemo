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
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.motion.utils.CurveFit;
import android.util.AttributeSet;

import java.util.HashMap;
import java.util.HashSet;

/**
 *  Base class in an element in a KeyFrame
 *  @hide
 */

public abstract class Key {
    public static int UNSET = -1;
    int mFramePosition = UNSET;
    int mTargetId = UNSET;
    String mTargetString = null;
    protected int mType;
    abstract void load(Context context, AttributeSet attrs);
    HashMap<String, ConstraintAttribute> mCustomConstraints;
    abstract void getAttributeNames(HashSet<String> attributes);
    static final String ALPHA = "alpha";
    static final String ELEVATION = "elevation";
    static final String ROTATION = "rotation";
    static final String ROTATION_X = "rotationX";
    static final String ROTATION_Y = "rotationY";
    static final String PIVOT_X = "transformPivotX";
    static final String PIVOT_Y = "transformPivotY";
    static final String TRANSITION_PATH_ROTATE = "transitionPathRotate";
    static final String SCALE_X = "scaleX";
    static final String SCALE_Y = "scaleY";
    static final String WAVE_PERIOD = "wavePeriod";
    static final String WAVE_OFFSET = "waveOffset";
    static final String WAVE_VARIES_BY = "waveVariesBy";
    static final String TRANSLATION_X = "translationX";
    static final String TRANSLATION_Y = "translationY";
    static final String TRANSLATION_Z = "translationZ";
    static final String PROGRESS = "progress";
    static final String CUSTOM = "CUSTOM";

    boolean matches(String constraintTag) {
        if (mTargetString == null || constraintTag == null) return false;
        return constraintTag.matches(mTargetString);
    }
    /**
     * Defines method to add a a view to splines derived form this key frame.
     * The values are written to the spline
     * @hide
     * @param splines splines to write values to
     */
    public abstract void addValues(HashMap<String, SplineSet> splines);

    /**
     * Set the value associated with this tag
     * @hide
     * @param tag
     * @param value
     */
    public abstract void setValue(String tag, Object value);

    /**
     * Return the float given a value. If the value is a "Float" object it is casted
     * @hide
     * @param value
     * @return
     */
    float toFloat(Object value) {
        return (value instanceof Float) ? (Float) value : Float.parseFloat(value.toString());
    }

    /**
     * Return the int version of an object if the value is an Integer object it is casted.
     * @hide
     * @param value
     * @return
     */
    int toInt(Object value) {
        return (value instanceof Integer) ? (Integer) value : Integer.parseInt(value.toString());
    }

    /**
     * Return the boolean version this object if the object is a Boolean it is casted.
     * @hide
     * @param value
     * @return
     */
    boolean toBoolean(Object value) {
        return (value instanceof Boolean) ? (Boolean) value : Boolean.parseBoolean(value.toString());
    }

    /**
     * Key frame can speify the type of interpolation it wants on various attributes
     * For each string it set it to -1, CurveFit.LINEAR or  CurveFit.SPLINE
     * @param interpolation
     */
    public void setInterpolation(HashMap<String, Integer> interpolation) {
    }
}
