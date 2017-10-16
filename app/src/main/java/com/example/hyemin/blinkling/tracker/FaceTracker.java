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
import android.util.Log;

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
    private float pre_left = 0.6f;
    private float cur_left = 0.5f;
    private int a = 1;

    public void set_indi(double left, double right, long time){
        //if(left>=0.5)
        left_thres = left;
       // else
         //   left_thres = 0.5;
        //if(right >= 0.5)
        right_thres = right;
        //else
          //  right_thres = 0.5;
        initial_check = true;
       // if(user_time>= 1000)
        user_time = time;
        //else
          //  user_time = 1000;
    }

    @Override
    public void onUpdate(Detector.Detections<Face> detections, Face face) {

        cur_left = face.getIsLeftEyeOpenProbability();

            if (leftClosed && cur_left > left_thres) {
                leftClosed = false;
            } else if (!leftClosed && cur_left < left_thres && cur_left != -1) {
                try {
                    sleep(user_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(cur_left != pre_left &&!leftClosed && cur_left < left_thres && cur_left != -1) {

                    leftClosed = true;
                    Log.d("check", cur_left+"현재1");
                    Log.d("check", pre_left+"예전1");
                    pre_left = cur_left;
                    a=1;
                }
                else leftClosed = false;
            }
            if (rightClosed && face.getIsRightEyeOpenProbability() > right_thres) {
                rightClosed = false;
            } else if (!rightClosed && face.getIsRightEyeOpenProbability() < right_thres && face.getIsRightEyeOpenProbability() != -1) {
                try {
                    sleep(user_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!rightClosed && face.getIsRightEyeOpenProbability() < right_thres && face.getIsRightEyeOpenProbability() != -1)
                rightClosed = true;
                else rightClosed = false;
//            }
        }

        //오른쪽, 왼쪽 눈 감음 구분
//        if (leftClosed && !rightClosed) {
//            EvedntBus.getDefault().post(new LeftEyeClosedEvent());
//        } else if (rightClosed && !leftClosed) {
//            EventBus.getDefault().post(new RightEyeClosedEvent());
//        } else if (leftClosed && rightClosed) {
//            EventBus.getDefault().post(new NeutralFaceEvent());
//        }

        // 오른쪽 왼쪽 눈감음 따로 인식 가능 (현재는 필요하지 않기 때문에 생략)
        if (leftClosed && rightClosed && a==1) {
           EventBus.getDefault().post(new RightEyeClosedEvent());
//            try {
//                sleep(1300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Log.d("check", face.getIsLeftEyeOpenProbability()+"왼쪽");
//            Log.d("check", face.getIsRightEyeOpenProbability()+"오른쪽");
            a++;
            Log.d("check", cur_left+"현재2");
            Log.d("check", pre_left+"예전2");
        } else if (!leftClosed && !rightClosed) {
            EventBus.getDefault().post(new NeutralFaceEvent());

        }
    }
}
