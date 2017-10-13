package com.example.hyemin.blinkling.Book_Viewer;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hyemin.blinkling.R;
import com.google.android.gms.vision.text.Text;

public class PageContentsFragment extends PageContentsFragmentBase {
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //((ViewGroup)TextViewFragment.textviewPage.getParent()).removeAllViews();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment, container, false);
        TextView tv = (TextView) rootView.findViewById(R.id.mText);

        Typeface typeFace;

        switch (TextViewFragment.font % 10) {
            case 0: // 나눔바른고딕
                typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/NanumBarunGothic.otf");
                tv.setTypeface(typeFace);
                break;
            case 1: // 나눔손글씨
                typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/NanumPen.otf");
                tv.setTypeface(typeFace);
                break;
            case 2: // 나눔바른펜
                typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/NanumBarunpenRegular.otf");
                tv.setTypeface(typeFace);
                break;
            case 3: // 나눔명조체
                typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/NanumMyeongjoBold.otf");
                tv.setTypeface(typeFace);
                break;
            case 4: // 나눔스퀘어체
                typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/NanumSquareOTFRegular.otf");
                tv.setTypeface(typeFace);
                break;
            case 5: // 나눔손글씨붓
                typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/NanumBrush.otf");
                tv.setTypeface(typeFace);
                break;
        }

        switch(TextViewFragment.textsize%10) {
            case 0:
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                break;
            case 1:
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                break;
            case 2:
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                break;
            case 3:
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 23);
                break;
        }

		String contents = (TextViewFragment.getContents(mPageNumber));
		tv.setText(contents);
        tv.setMovementMethod(new ScrollingMovementMethod());
        return rootView;
    }

}
