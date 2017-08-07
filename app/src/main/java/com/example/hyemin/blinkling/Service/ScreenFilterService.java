package com.example.hyemin.blinkling.Service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;

//import static android.support.v4.app.ActivityCompatJB.startActivityForResult;

/**
 * Created by sunphiz on 2014. 10. 3..
 */
public class ScreenFilterService extends Service
{

    private View mView;

    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private SharedPreferences intPref;
    private SharedPreferences.Editor editor1;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mView = new MyLoadView(this);

        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT );


        mWindowManager = (WindowManager) getSystemService( WINDOW_SERVICE );
        mWindowManager.addView( mView, mParams );

        intPref = this.getSharedPreferences("mPred", Activity.MODE_PRIVATE);
        editor1 = intPref.edit();

    }

    @Override
    public IBinder onBind(Intent intent )
    {
        return null;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        ((WindowManager) getSystemService( WINDOW_SERVICE )).removeView(
                mView );
        mView = null;
    }

    public class MyLoadView extends View
    {

        private Paint mPaint;

        public MyLoadView( Context context )
        {
            super(context);
            mPaint = new Paint();
            mPaint.setTextSize( 100 );
            mPaint.setARGB( 200, 10, 10, 10 );
        }

        @Override
        protected void onDraw( Canvas canvas )
        {
            int value = intPref.getInt("bluelight_edit",5);
            int blue_value = value*255/10;
            super.onDraw( canvas );
            canvas.drawARGB( 100, 255, 212, blue_value );
        }

        @Override
        protected void onAttachedToWindow()
        {
            super.onAttachedToWindow();
        }

        @Override
        protected void onDetachedFromWindow()
        {
            super.onDetachedFromWindow();
        }

        @Override
        protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec )
        {
            super.onMeasure( widthMeasureSpec, heightMeasureSpec );
        }
    }
}