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

package androidx.constraintlayout.solver.state.helpers;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.state.ConstraintReference;
import androidx.constraintlayout.solver.state.State;

public class HorizontalChainReference extends ChainReference {

    private Object mStartToStart;
    private Object mStartToEnd;
    private Object mEndToStart;
    private Object mEndToEnd;

    public HorizontalChainReference(State state) {
        super(state, State.Helper.HORIZONTAL_CHAIN);
    }

    public void apply() {
        ConstraintReference first = null;
        ConstraintReference previous = null;
        for (Object key : mReferences) {
            ConstraintReference reference = mState.constraints(key);
            reference.clearHorizontal();
        }

        for (Object key : mReferences) {
            ConstraintReference reference = mState.constraints(key);
            if (first == null) {
                first = reference;
                if (mStartToStart != null) {
                    first.startToStart(mStartToStart);
                } else if (mStartToEnd != null) {
                    first.startToEnd(mStartToEnd);
                } else {
                    first.startToStart(State.PARENT);
                }
            }
            if (previous != null) {
                previous.endToStart(reference.getKey());
                reference.startToEnd(previous.getKey());
            }
            previous = reference;
        }

        if (previous != null) {
            if (mEndToStart != null) {
                previous.endToStart(mEndToStart);
            } else if (mEndToEnd != null) {
                previous.endToEnd(mEndToEnd);
            } else {
                previous.endToEnd(State.PARENT);
            }
        }

        if (first != null && mBias != 0.5f) {
            first.horizontalBias(mBias);
        }

        switch (mStyle) {
            case SPREAD: {
                first.setHorizontalChainStyle(ConstraintWidget.CHAIN_SPREAD);
            } break;
            case SPREAD_INSIDE: {
                first.setHorizontalChainStyle(ConstraintWidget.CHAIN_SPREAD_INSIDE);
            } break;
            case PACKED: {
                first.setHorizontalChainStyle(ConstraintWidget.CHAIN_PACKED);
            }
        }
    }

    public void startToStart(Object target) { mStartToStart = target; }
    public void startToEnd(Object target) { mStartToEnd = target; }
    public void endToStart(Object target) { mEndToStart = target; }
    public void endToEnd(Object target) { mEndToEnd = target; }
}
