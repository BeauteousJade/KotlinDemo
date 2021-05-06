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

package androidx.constraintlayout.motion.utils;

import java.util.Arrays;

/**
 * This provides provides a curve fit system that stitches the x,y path together with
 * quarter ellipses
 */

class ArcCurveFit extends CurveFit {
    public static final int ARC_START_VERTICAL = 1;
    public static final int ARC_START_HORIZONTAL = 2;
    public static final int ARC_START_FLIP = 3;
    public static final int ARC_START_LINEAR = 0;

    private static final int START_VERTICAL = 1;
    private static final int START_HORIZONTAL = 2;
    private static final int START_LINEAR = 3;
    private final double[] mTime;
    Arc[] mArcs;

    @Override
    public void getPos(double t, double[] v) {
        if (t < mArcs[0].mTime1) {
            t = mArcs[0].mTime1;
        }
        if (t > mArcs[mArcs.length - 1].mTime2) {
            t = mArcs[mArcs.length - 1].mTime2;
        }

        for (int i = 0; i < mArcs.length; i++) {
            if (t <= mArcs[i].mTime2) {
                if (mArcs[i].linear) {
                    v[0] = mArcs[i].getLinearX(t);
                    v[1] = mArcs[i].getLinearY(t);
                    return;
                }
                mArcs[i].setPoint(t);
                v[0] = mArcs[i].getX();
                v[1] = mArcs[i].getY();
                return;
            }
        }
    }

    @Override
    public void getPos(double t, float[] v) {
        if (t < mArcs[0].mTime1) {
            t = mArcs[0].mTime1;
        } else if (t > mArcs[mArcs.length - 1].mTime2) {
            t = mArcs[mArcs.length - 1].mTime2;
        }

        for (int i = 0; i < mArcs.length; i++) {
            if (t <= mArcs[i].mTime2) {
                if (mArcs[i].linear) {
                    v[0] = (float) mArcs[i].getLinearX(t);
                    v[1] = (float) mArcs[i].getLinearY(t);
                    return;
                }
                mArcs[i].setPoint(t);
                v[0] = (float) mArcs[i].getX();
                v[1] = (float) mArcs[i].getY();
                return;
            }
        }
    }

    @Override
    public void getSlope(double t, double[] v) {
        if (t < mArcs[0].mTime1) {
            t = mArcs[0].mTime1;
        } else if (t > mArcs[mArcs.length - 1].mTime2) {
            t = mArcs[mArcs.length - 1].mTime2;
        }

        for (int i = 0; i < mArcs.length; i++) {
            if (t <= mArcs[i].mTime2) {
                if (mArcs[i].linear) {
                    v[0] = mArcs[i].getLinearDX(t);
                    v[1] = mArcs[i].getLinearDY(t);
                    return;
                }
                mArcs[i].setPoint(t);
                v[0] = mArcs[i].getDX();
                v[1] = mArcs[i].getDY();
                return;
            }
        }
    }

    @Override
    public double getPos(double t, int j) {
        if (t < mArcs[0].mTime1) {
            t = mArcs[0].mTime1;
        } else if (t > mArcs[mArcs.length - 1].mTime2) {
            t = mArcs[mArcs.length - 1].mTime2;
        }

        for (int i = 0; i < mArcs.length; i++) {
            if (t <= mArcs[i].mTime2) {

                if (mArcs[i].linear) {
                    if (j == 0) {
                        return mArcs[i].getLinearX(t);
                    }
                    return mArcs[i].getLinearY(t);
                }
                mArcs[i].setPoint(t);

                if (j == 0) {
                    return mArcs[i].getX();
                }
                return mArcs[i].getY();
            }
        }
        return Double.NaN;
    }

    @Override
    public double getSlope(double t, int j) {
        if (t < mArcs[0].mTime1) {
            t = mArcs[0].mTime1;
        }
        if (t > mArcs[mArcs.length - 1].mTime2) {
            t = mArcs[mArcs.length - 1].mTime2;
        }

        for (int i = 0; i < mArcs.length; i++) {
            if (t <= mArcs[i].mTime2) {
                if (mArcs[i].linear) {
                    if (j == 0) {
                        return mArcs[i].getLinearDX(t);
                    }
                    return mArcs[i].getLinearDY(t);
                }
                mArcs[i].setPoint(t);
                if (j == 0) {
                    return mArcs[i].getDX();
                }
                return mArcs[i].getDY();
            }
        }
        return Double.NaN;
    }

    @Override
    public double[] getTimePoints() {
        return mTime;
    }

    public ArcCurveFit(int[] arcModes, double[] time, double[][] y) {
        mTime = time;
        mArcs = new Arc[time.length - 1];
        int mode = START_VERTICAL;
        int last = START_VERTICAL;
        for (int i = 0; i < mArcs.length; i++) {
            switch (arcModes[i]) {
                case ARC_START_VERTICAL:
                    last = mode = START_VERTICAL;
                    break;
                case ARC_START_HORIZONTAL:
                    last = mode = START_HORIZONTAL;
                    break;
                case ARC_START_FLIP:
                    mode = (last == START_VERTICAL) ? START_HORIZONTAL : START_VERTICAL;
                    last = mode;
                    break;
                case ARC_START_LINEAR:
                    mode = START_LINEAR;
            }
            mArcs[i] = new Arc(mode, time[i], time[i + 1], y[i][0], y[i][1], y[i + 1][0], y[i + 1][1]);
        }
    }

    private static class Arc {
        private static final String TAG = "Arc";
        private static double[] ourPercent = new double[91];
        double[] mLut;
        double mArcDistance;
        double mTime1;
        double mTime2;
        double mX1, mX2, mY1, mY2;
        double mOneOverDeltaTime;
        double mEllipseA;
        double mEllipseB;
        double mEllipseCenterX; // also used to cache the slope in the unused center
        double mEllipseCenterY; // also used to cache the slope in the unused center
        double mArcVelocity;
        double mTmpSinAngle;
        double mTmpCosAngle;
        boolean mVertical;
        boolean linear = false;
        private static final double EPSILON = 0.001;

        Arc(int mode, double t1, double t2, double x1, double y1, double x2, double y2) {
            mVertical = mode == START_VERTICAL;
            mTime1 = t1;
            mTime2 = t2;
            mOneOverDeltaTime = 1 / (mTime2 - mTime1);
            if (START_LINEAR == mode) {
                linear = true;
            }
            double dx = x2 - x1;
            double dy = y2 - y1;
            if (linear || Math.abs(dx) < EPSILON || Math.abs(dy) < EPSILON) {
                linear = true;
                mX1 = x1;
                mX2 = x2;
                mY1 = y1;
                mY2 = y2;
                mArcDistance = Math.hypot(dy, dx);
                mArcVelocity = mArcDistance * mOneOverDeltaTime;
                mEllipseCenterX = dx / (mTime2 - mTime1); // cache the slope in the unused center
                mEllipseCenterY = dy / (mTime2 - mTime1); // cache the slope in the unused center
                return;
            }
            mLut = new double[101];
            mEllipseA = (dx) * ((mVertical) ? -1 : 1);
            mEllipseB = (dy) * ((mVertical) ? 1 : -1);
            mEllipseCenterX = (mVertical) ? x2 : x1;
            mEllipseCenterY = (mVertical) ? y1 : y2;
            buildTable(x1, y1, x2, y2);
            mArcVelocity = mArcDistance * mOneOverDeltaTime;
        }

        void setPoint(double time) {
            double percent = (mVertical ? (mTime2 - time) : (time - mTime1)) * mOneOverDeltaTime;
            double angle = Math.PI * 0.5 * lookup(percent);

            mTmpSinAngle = Math.sin(angle);
            mTmpCosAngle = Math.cos(angle);
        }

        double getX() {
            return mEllipseCenterX + mEllipseA * mTmpSinAngle;
        }

        double getY() {
            return mEllipseCenterY + mEllipseB * mTmpCosAngle;
        }

        double getDX() {
            double vx = mEllipseA * mTmpCosAngle;
            double vy = -mEllipseB * mTmpSinAngle;
            double norm = mArcVelocity / Math.hypot(vx, vy);
            return mVertical ? -vx * norm : vx * norm;
        }

        double getDY() {
            double vx = mEllipseA * mTmpCosAngle;
            double vy = -mEllipseB * mTmpSinAngle;
            double norm = mArcVelocity / Math.hypot(vx, vy);
            return mVertical ? -vy * norm : vy * norm;
        }

        public double getLinearX(double t) {
            t = (t - mTime1) * mOneOverDeltaTime;
            return mX1 + t * (mX2 - mX1);
        }

        public double getLinearY(double t) {
            t = (t - mTime1) * mOneOverDeltaTime;
            return mY1 + t * (mY2 - mY1);
        }

        public double getLinearDX(double t) {
            return mEllipseCenterX;
        }

        public double getLinearDY(double t) {
            return mEllipseCenterY;
        }

        double lookup(double v) {
            if (v <= 0) {
                return 0;
            }
            if (v >= 1) {
                return 1;
            }
            double pos = v * (mLut.length - 1);
            int iv = (int) (pos);

            double off = pos - (int) (pos);

            return mLut[iv] + (off * (mLut[iv + 1] - mLut[iv]));
        }

        private void buildTable(double x1, double y1, double x2, double y2) {
            double a = x2 - x1;
            double b = y1 - y2;
            double lx = 0, ly = 0;
            double dist = 0;
            for (int i = 0; i < ourPercent.length; i++) {
                double angle = Math.toRadians(90.0 * i / (ourPercent.length - 1));
                double s = Math.sin(angle);
                double c = Math.cos(angle);
                double px = a * s;
                double py = b * c;
                if (i > 0) {
                    dist += Math.hypot(px - lx, py - ly);
                    ourPercent[i] = dist;
                }
                lx = px;
                ly = py;
            }

            mArcDistance = dist;

            for (int i = 0; i < ourPercent.length; i++) {
                ourPercent[i] /= dist;
            }
            for (int i = 0; i < mLut.length; i++) {
                double pos = i / (double) (mLut.length - 1);
                int index = Arrays.binarySearch(ourPercent, pos);
                if (index >= 0) {
                    mLut[i] = index / (ourPercent.length - 1);
                } else if (index == -1) {
                    mLut[i] = 0;
                } else {
                    int p1 = -index - 2;
                    int p2 = -index - 1;

                    double ans =
                            (p1 + (pos - ourPercent[p1]) / (ourPercent[p2] - ourPercent[p1])) / (ourPercent.length - 1);
                    mLut[i] = ans;
                }
            }
        }
    }
}
