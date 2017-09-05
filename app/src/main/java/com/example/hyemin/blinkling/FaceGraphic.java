/*
 * Copyright (C) The Android Open Source Project
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
package com.example.hyemin.blinkling;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Toast;

import com.example.hyemin.blinkling.camera.GraphicOverlay;
import com.example.hyemin.blinkling.event.EyeClosedEvent;
import com.example.hyemin.blinkling.event.EyeOpenEvent;
import com.example.hyemin.blinkling.event.NeutralFaceEvent;
import com.example.hyemin.blinkling.event.RightEyeClosedEvent;
import com.google.android.gms.vision.face.Face;

import org.greenrobot.eventbus.EventBus;

import static android.app.PendingIntent.getActivities;
import static android.app.PendingIntent.getActivity;

/**
 * Graphic instance for rendering face position, orientation, and landmarks within an associated
 * graphic overlay view.
 */
class FaceGraphic extends GraphicOverlay.Graphic {
    private static final float FACE_POSITION_RADIUS = 10.0f;
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = -50.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;
    private boolean leftClosed;
    private boolean rightClosed;

    private static final int COLOR_CHOICES[] = {
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.WHITE,
            Color.YELLOW
    };
    private static int mCurrentColorIndex = 0;

    private Paint mFacePositionPaint;
    private Paint mIdPaint;
    private Paint mBoxPaint;

    private volatile Face mFace;
    private int mFaceId;
    private float mFaceHappiness;


    public static float left_thred;
    public static float right_thred;

    public static double leftClosed_size = 0;
    public static double rightClosed_size = 0;

    private int check_time = 0;

    public void set_closed_size(double l, double r) {
        leftClosed_size = l;
        rightClosed_size = r;
        check_time = 1;
    }

    FaceGraphic(GraphicOverlay overlay) {
        super(overlay);

        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int selectedColor = COLOR_CHOICES[mCurrentColorIndex];

        mFacePositionPaint = new Paint();
        mFacePositionPaint.setColor(selectedColor);

        mIdPaint = new Paint();
        mIdPaint.setColor(selectedColor);
        mIdPaint.setTextSize(ID_TEXT_SIZE);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(selectedColor);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    void setId(int id) {
        mFaceId = id;
    }

    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    void updateFace(Face face) {
        mFace = face;
        postInvalidate();
    }

    public float return_left() {
        return left_thred;
    }

    public float return_right() {
        return right_thred;
    }

    /**
     * Draws the face annotations for position on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;
        if (face == null) {
            return;
        }

        // Draws a circle at the position of the detected face, with the face's track id below.
        float x = translateX(face.getPosition().x + face.getWidth() / 2);
        float y = translateY(face.getPosition().y + face.getHeight() / 2);
        canvas.drawCircle(x, y, FACE_POSITION_RADIUS, mFacePositionPaint);
        canvas.drawText("id: " + mFaceId, x + ID_X_OFFSET, y + ID_Y_OFFSET, mIdPaint);
        canvas.drawText("happiness: " + String.format("%.2f", face.getIsSmilingProbability()), x - ID_X_OFFSET, y - ID_Y_OFFSET, mIdPaint);
        canvas.drawText("right eye: " + String.format("%.2f", face.getIsRightEyeOpenProbability()), x + ID_X_OFFSET * 2, y + ID_Y_OFFSET * 2, mIdPaint);
        canvas.drawText("left eye: " + String.format("%.2f", face.getIsLeftEyeOpenProbability()), x - ID_X_OFFSET * 2, y - ID_Y_OFFSET * 2, mIdPaint);

        // Draws a bounding box around the face.
        float xOffset = scaleX(face.getWidth() / 2.0f);
        float yOffset = scaleY(face.getHeight() / 2.0f);
        float left = x - xOffset;
        float top = y - yOffset;
        float right = x + xOffset;
        float bottom = y + yOffset;
        canvas.drawRect(left, top, right, bottom, mBoxPaint);

        right_thred = face.getIsRightEyeOpenProbability();
        left_thred = face.getIsLeftEyeOpenProbability();

        // 눈 크기 초기화 없이 시간 초기화
        if (check_time != 1) {
            if (leftClosed && face.getIsLeftEyeOpenProbability() > 0.8) {
                leftClosed = false;
            } else if (!leftClosed && face.getIsLeftEyeOpenProbability() < 0.5) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!leftClosed && face.getIsLeftEyeOpenProbability() < 0.5)
                    leftClosed = true;
                else leftClosed = false;
            }
            if (rightClosed && face.getIsRightEyeOpenProbability() > 0.5) {
                rightClosed = false;
            } else if (!rightClosed && face.getIsRightEyeOpenProbability() < 0.5) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!rightClosed && face.getIsRightEyeOpenProbability() < 0.5)
                    rightClosed = true;
                else rightClosed = false;
            }
        } else { // 눈 크기 초기화 이후 시간 초기화
            if (leftClosed && face.getIsLeftEyeOpenProbability() > leftClosed_size) {
                leftClosed = false;
            } else if (!leftClosed && face.getIsLeftEyeOpenProbability() < leftClosed_size) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!leftClosed && face.getIsLeftEyeOpenProbability() < leftClosed_size)
                    leftClosed = true;
                else leftClosed = false;
            }
            if (rightClosed && face.getIsRightEyeOpenProbability() > rightClosed_size) {
                rightClosed = false;
            } else if (!rightClosed && face.getIsRightEyeOpenProbability() < rightClosed_size) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!rightClosed && face.getIsRightEyeOpenProbability() < rightClosed_size)
                    rightClosed = true;
                else rightClosed = false;
            }
        }

        if (leftClosed && rightClosed) {
            EventBus.getDefault().post(new EyeClosedEvent());
        } else if (!leftClosed && !rightClosed) {
            EventBus.getDefault().post(new EyeOpenEvent());
        }
    }
}
