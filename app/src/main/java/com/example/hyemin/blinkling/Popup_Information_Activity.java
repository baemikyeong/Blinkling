package com.example.hyemin.blinkling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;

public class Popup_Information_Activity extends AppCompatActivity {
    ImageView iv;
    Button goTo_Initialization;
    private CameraSource mCameraSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_information);

        iv = (ImageView) findViewById(R.id.img1);
        goTo_Initialization = (Button) findViewById(R.id.button1);

        iv.setImageResource(R.drawable.initial);
        goTo_Initialization.setOnClickListener(new MyListener());




    }

    class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Popup_Information_Activity.this,Face_Activity.class);

            if (mCameraSource != null) {
                mCameraSource.release();
                mCameraSource = null;
            }

         //   Toast.makeText(Popup_Information_Activity.this, "초기화를 시작합니다", Toast.LENGTH_SHORT).show();
            // Toast.makeText(Popup_Information_Activity.this, "눈을 감고 Blink_Size 버튼을 두 번 눌러주세요", Toast.LENGTH_SHORT).show();

            startActivity(intent);
        }
    }
}
