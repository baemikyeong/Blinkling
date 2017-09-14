package com.example.hyemin.blinkling.Setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyemin.blinkling.Face_Activity;
import com.example.hyemin.blinkling.MainActivity;
import com.example.hyemin.blinkling.R;
import com.example.hyemin.blinkling.Setting.SetBackgroundFragment;
import com.example.hyemin.blinkling.Setting.SetBluelightFragment;
import com.example.hyemin.blinkling.Setting.SetBrightnessFragment;
import com.example.hyemin.blinkling.Setting.SetFontFragment;
import com.example.hyemin.blinkling.Setting.SetPageStyleFragment;
import com.google.android.gms.vision.CameraSource;

public class SettingFragment extends Fragment {

    private CameraSource mCameraSource;
    public static final int HDR_POS1 = 0;
    public static final int HDR_POS2 = 7;
    public static final String[] LIST = {"Viewer Setting", "배경색",
            "밝기", "글꼴", "글씨 크기", "블루라이트 조절", "페이지 넘기는 방식",
            "Personalize", "Eye Personalize"};
    public static final String[] SUBTEXTS = {null, "문서를 볼 때의 배경색을 지정합니다.",
            "블링클링 사용 시, 밝기를 조정합니다. ", "문서를 볼 때의 글꼴을 지정합니다. ", "문서를 볼 때의 글씨 크기를 지정합니다 ", "블루라이트의 정도를 조정합니다. ", "문서를 볼 때, 스크롤링 페이지와 페이저 방식 중에 선택합니다. ",
            null, "눈 크기와 눈 깜박임 시간을 개인에 맞게 조정합니다."};

    private static final Integer LIST_HEADER = 0;
    private static final Integer LIST_ITEM = 1;

    public SettingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.bookmark_btn).setVisible(false);
        menu.findItem(R.id.voice_btn).setVisible(false);
        menu.findItem(R.id.eye_btn).setVisible(false);
        menu.findItem(R.id.light_btn).setVisible(false);
        menu.findItem(R.id.notebook_add).setVisible(false);
        menu.findItem(R.id.notebook_delete).setVisible(false);
        menu.findItem(R.id.bookmark_delete).setVisible(false);
        menu.findItem(R.id.webmark_add).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        final MyListAdapter Adapter = new MyListAdapter(getActivity());

        ListView lv = (ListView) view.findViewById(R.id.listView1);
        lv.setAdapter(Adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                setting_check(position);
            }
        });

        return view;
    }

    private class MyListAdapter extends BaseAdapter {
        public MyListAdapter(Context context) {
            mContext = context;
        }


        @Override
        public int getCount() {
            return LIST.length;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final int pos = position;
            String headerText = getHeader(position);
            if (headerText != null) {

                View item = convertView;
                if (convertView == null || convertView.getTag() == LIST_ITEM) {

                    item = LayoutInflater.from(mContext).inflate(
                            R.layout.lv_header_layout, parent, false);
                    item.setTag(LIST_HEADER);

                }

                TextView headerTextView = (TextView) item.findViewById(R.id.lv_list_hdr);
                headerTextView.setText(headerText);
                return item;
            }

            View item = convertView;
            if (convertView == null || convertView.getTag() == LIST_HEADER) {
                item = LayoutInflater.from(mContext).inflate(
                        R.layout.lv_layout, parent, false);
                item.setTag(LIST_ITEM);
            }

            ImageButton img_button = (ImageButton) item.findViewById(R.id.button);

            TextView header = (TextView) item.findViewById(R.id.lv_item_header);
            header.setText(LIST[position]);

            TextView subtext = (TextView) item.findViewById(R.id.lv_item_subtext);
            subtext.setText(SUBTEXTS[position]);



            //Set last divider in a sublist invisible
            View divider = item.findViewById(R.id.item_separator);
            if (position == HDR_POS2 - 1) {
                divider.setVisibility(View.INVISIBLE);
            }

            img_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setting_check(pos);
                }
            });

            return item;
        }

        private String getHeader(int position) {

            if (position == HDR_POS1 || position == HDR_POS2) {
                return LIST[position];
            }

            return null;
        }

        private final Context mContext;
    }

    public void setting_check(int pos){

        switch(pos){
            case 1:
                SetBackgroundFragment bg_dialog = new SetBackgroundFragment();
                bg_dialog.show(getFragmentManager(), "Edit background");
                break;
            case 2:
                SetBrightnessFragment bright_dialog = new SetBrightnessFragment();
                bright_dialog.show(getFragmentManager(), "Edit brightness");
                break;
            case 3:
                SetFontFragment font_dialog = new SetFontFragment();
                font_dialog.show(getFragmentManager(), "Edit font");
                break;
            case 4:
                //블루라이트 조절 다이얼로그 띄우기
                SetTextSizeFragment size_dialog = new SetTextSizeFragment();
                size_dialog.show(getFragmentManager(), "Edit size");
                break;
            case 5:
                //블루라이트 조절 다이얼로그 띄우기
                SetBluelightFragment blue_dialog = new SetBluelightFragment();
                blue_dialog.show(getFragmentManager(), "Edit bluelight");
                break;
            case 6:
                SetPageStyleFragment page_dialog = new SetPageStyleFragment();
                page_dialog.show(getFragmentManager(), "Edit pagestyle");
                break;
            case 8:
                Intent intent = new Intent(getActivity(), Face_Activity.class);

                if (mCameraSource != null) {
                    mCameraSource.release();
                    mCameraSource = null;
                }

                Toast.makeText(getActivity(), "초기화를 시작합니다", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "눈을 감고 Blink_Size 버튼을 두 번 눌러주세요", Toast.LENGTH_SHORT).show();

                startActivity(intent);
                break;
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        if(MainActivity.bottomNavigation.getSelectedItemId() != R.id.navigation_foodbank)
            MainActivity.bottomNavigation.getMenu().findItem(R.id.navigation_foodbank).setChecked(true);

    }

}