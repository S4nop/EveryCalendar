package edu.skku.everycalendar.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import edu.skku.everycalendar.friends.FriendsListItem;
import edu.skku.everycalendar.friends.FriendsSelectAdapter;
import edu.skku.everycalendar.functions.JoinSchedule;
import edu.skku.everycalendar.R;
import edu.skku.everycalendar.monthItems.MonthCalendar;

public class AdjustFragment extends Fragment {
    Button result_btn;
    Button reset_btn;
    Button month_btn;

    TimePicker start_picker;
    TimePicker end_picker;

    MainActivity activity;
    Context context;
    ListView listView;
    TextView selected_week;

    ArrayList<FriendsListItem> friends_list;
    FriendsSelectAdapter adapter;

    Map<String, String> list_map;

    String ed_date = null;
    String st_date = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_adjust, container, false);
        inflater.inflate(R.layout.fragment_adjust, container, false);

        activity = (MainActivity) getActivity();
        context = activity.mainContext;

        selected_week = rootView.findViewById(R.id.selected_week);
        listView = rootView.findViewById(R.id.friends_list);
        result_btn = rootView.findViewById(R.id.btn_result);
        reset_btn = rootView.findViewById(R.id.btn_reset);
        month_btn = rootView.findViewById(R.id.btn_month);

        start_picker = rootView.findViewById(R.id.start_time);
        end_picker = rootView.findViewById(R.id.end_time);

        friends_list = activity.friends_list;
        list_map = activity.friendList;

        adapter = new FriendsSelectAdapter(friends_list);
        listView.setAdapter(adapter);

        result_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray isChecked = listView.getCheckedItemPositions();
                int count = adapter.getCount();
                int start_hour;
                int start_min;
                int end_hour;
                int end_min;

                //check된 친구 item들이 들어있는 list 
                ArrayList<FriendsListItem> checked_list = new ArrayList<>();

                for (int i = count-1; i >= 0; i--) {
                    if (isChecked.get(i)) {
                        FriendsListItem item = friends_list.get(i);
                        checked_list.add(item);
                    }
                }

                listView.clearChoices();

                //hour은 24시 기준 (오후 10시 -> 22시)
                start_hour = start_picker.getHour();
                start_min = start_picker.getMinute();
                end_hour = end_picker.getHour();
                end_min = end_picker.getMinute();

                //st_date, ed_date 저장되어 있음

                if(checked_list.size()==0){
                    Toast.makeText(context, "친구를 선택해 주세요!",Toast.LENGTH_LONG).show();
                }
                else if (st_date==null||ed_date==null){
                    Toast.makeText(context, "주를 선택해 주세요!",Toast.LENGTH_LONG).show();
                }
                else if((start_hour==end_hour)&&(start_min==end_min)){
                    Toast.makeText(context, "시간 선택이 올바르지 않습니다",Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(context, AdjustResultActivity.class);
                    startActivity(intent);
                }

            }
        });

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        month_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthCalendar monthCalendar = new MonthCalendar(context,1);
                monthCalendar.setOnDismissListener((DialogInterface.OnDismissListener) getActivity());
                monthCalendar.show();
            }
        });
        JoinSchedule js = new JoinSchedule(9, 20);
        //js.test();
        return rootView;
    }

    public void setWeek(String st_date, String ed_date){
        this.ed_date = ed_date;
        this.st_date = st_date;
        selected_week.setText(st_date+" ~ "+ed_date);
    }
}
