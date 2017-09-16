/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Aitor Viana Sanchez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.example.hyemin.blinkling.tracker;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.hyemin.blinkling.event.*;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

import org.greenrobot.eventbus.EventBus;

import static java.lang.Thread.sleep;


public class FaceTracker extends Tracker<Face> {

    private static final float PROB_THRESHOLD = 0.7f;
    private static final String TAG = FaceTracker.class.getSimpleName();
    private boolean leftClosed;
    private boolean rightClosed;
    private double left_thres;
    private double right_thres;
    private boolean initial_check = false;
    private long user_time;

    public void set_indi(double left, double right, long time){
        if(left>=0.6)
        left_thres = left;
        else
            left_thres = 0.6;
        if(right >= 0.6)
        right_thres = right;
        else
            right_thres = 0.6;
        initial_check = true;
        if(user_time>= 900)
        user_time = time;
        else
            user_time = 900;
    }

    @Override
    public void onUpdate(Detector.Detections<Face> detections, Face face) {

//        if(initial_check == false) {
//            if (leftClosed && face.getIsLeftEyeOpenProbability() > PROB_THRESHOLD) {
//                leftClosed = false;
//            } else if (!leftClosed && face.getIsLeftEyeOpenProbability() < PROB_THRESHOLD) {
//                leftClosed = true;
//            }
//            if (rightClosed && face.getIsRightEyeOpenProbability() > PROB_THRESHOLD) {
//                rightClosed = false;
//            } else if (!rightClosed && face.getIsRightEyeOpenProbability() < PROB_THRESHOLD) {
//                rightClosed = true;
//            }
//        }
//        else {
            if (leftClosed && face.getIsLeftEyeOpenProbability() > left_thres) {
                leftClosed = false;
            } else if (!leftClosed && face.getIsLeftEyeOpenProbability() < left_thres) {
                try {
                    sleep(user_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!leftClosed && face.getIsLeftEyeOpenProbability() < left_thres)
                leftClosed = true;
                else leftClosed = false;
            }
            if (rightClosed && face.getIsRightEyeOpenProbability() > right_thres) {
                rightClosed = false;
            } else if (!rightClosed && face.getIsRightEyeOpenProbability() < right_thres) {
                try {
                    sleep(user_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!rightClosed && face.getIsRightEyeOpenProbability() < right_thres)
                rightClosed = true;
                else rightClosed = false;
//            }
        }

        //오른쪽, 왼쪽 눈 감음 구분
//        if (leftClosed && !rightClosed) {
//            EventBus.getDefault().post(new LeftEyeClosedEvent());
//        } else if (rightClosed && !leftClosed) {
//            EventBus.getDefault().post(new RightEyeClosedEvent());
//        } else if (leftClosed && rightClosed) {
//            EventBus.getDefault().post(new NeutralFaceEvent());
//        }

        // 오른쪽 왼쪽 눈감음 따로 인식 가능 (현재는 필요하지 않기 때문에 생략)
        if (leftClosed && rightClosed) {
           EventBus.getDefault().post(new RightEyeClosedEvent());
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (!leftClosed && !rightClosed) {
            EventBus.getDefault().post(new NeutralFaceEvent());

        }
    }
}
