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

package androidx.constraintlayout.solver.state;

import androidx.constraintlayout.solver.widgets.HelperWidget;

import java.util.ArrayList;

public class HelperReference {
    protected final State mState;
    final State.Helper mType;
    protected ArrayList<Object> mReferences = new ArrayList<>();
    private HelperWidget mHelperWidget;

    public HelperReference(State state, State.Helper type) {
        mState = state;
        mType = type;
    }

    public State.Helper getType() { return mType; }

    public HelperReference add(Object... objects) {
        for (Object object : objects) {
            mReferences.add(object);
        }
        return this;
    }

    public void setHelperWidget(HelperWidget helperWidget) {
        mHelperWidget = helperWidget;
    }

    public HelperWidget getHelperWidget() { return mHelperWidget; }

    public void apply() {
        // nothing
    }
}
