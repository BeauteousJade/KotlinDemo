/*
 * Copyright (C) 2019 The Android Open Source Project
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

import java.util.Arrays;
import java.util.HashMap;

/**
 * Used by KeyTimeCycles (and any future time dependent behaviour) to cache its current parameters
 * to maintain consistency across requestLayout type rebuilds.
 */
public class KeyCache {

    HashMap<Object, HashMap<String, float[]>> map = new HashMap<>();

    void setFloatValue(Object view, String type, int element, float value) {
        if (!map.containsKey(view)) {
            HashMap<String, float[]> array = new HashMap();
            float[] vArray = new float[element + 1];
            vArray[element] = value;
            array.put(type, vArray);
            map.put(view, array);
        } else {
            HashMap<String, float[]> array = map.get(view);
            if (!array.containsKey(type)) {
                float[] vArray = new float[element + 1];
                vArray[element] = value;
                array.put(type, vArray);
                map.put(view, array);
            } else {
                float[] vArray = array.get(type);
                if (vArray.length <= element) {
                    vArray = Arrays.copyOf(vArray, element + 1);
                }
                vArray[element] = value;
                array.put(type, vArray);
            }
        }
    }

    float getFloatValue(Object view, String type, int element) {
        if (!map.containsKey(view)) {
            return Float.NaN;
        } else {
            HashMap<String, float[]> array = map.get(view);
            if (!array.containsKey(type)) {
                return Float.NaN;
            }
            float[] vArray = array.get(type);
            if (vArray.length > element) {
                return vArray[element];
            }
            return Float.NaN;
        }
    }
}
