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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.skku.everycalendar.friends.FriendsListItem;
import edu.skku.everycalendar.friends.FriendsSelectAdapter;
import edu.skku.everycalendar.functions.JoinSchedulReq;
import edu.skku.everycalendar.functions.JoinSchedule;
import edu.skku.everycalendar.R;
import edu.skku.everycalendar.functions.Utilities;
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

    HashMap<String, String> friends_list;
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

        friends_list = activity.friends_list_with_id;
        list_map = activity.friendList;

        final ArrayList<FriendsListItem> friends = new ArrayList<>();
        Iterator<String> iterator = friends_list.keySet().iterator();
        while(iterator.hasNext()){
            String name = iterator.next();
            FriendsListItem item = new FriendsListItem(name);
            friends.add(item);
        }
        adapter = new FriendsSelectAdapter(friends);
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
                        FriendsListItem item = friends.get(i);
                        Log.d("get_checked",item.getFriend_name());
                        checked_list.add(item);
                    }
                }

                //hour은 24시 기준 (오후 10시 -> 22시)
                start_hour = start_picker.getHour();
                start_min = start_picker.getMinute();
                end_hour = end_picker.getHour();
                end_min = end_picker.getMinute();

                //st_date, ed_date 저장되어 있음

                if(checked_list.size()==0){
                    Utilities.makeToast("친구를 선택해 주세요!");
                }
                else if (st_date==null||ed_date==null){
                    Utilities.makeToast("주를 선택해 주세요!");
                }
                else if((start_hour==end_hour)&&(start_min==end_min)){
                    Utilities.makeToast("시간 선택이 올바르지 않습니다");
                }
                else{
                    reset_btn.callOnClick();
                    Intent intent = new Intent(context, AdjustResultActivity.class);
                    startActivity(intent);
                }

            }
        });

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.clearChoices();
                selected_week.setText("");
                
                long now_time = System.currentTimeMillis();
                Date date = new Date(now_time);
                int hour = date.getHours();
                int min = date.getMinutes();

                start_picker.setHour(hour);
                start_picker.setMinute(min);
                end_picker.setHour(hour);
                end_picker.setMinute(min);

                activity.bottomBar.setSelectedItemId(4);
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
        //JoinSchedulReq js = new JoinSchedulReq();
        //ArrayList<String> tmp = new ArrayList<>();
        //tmp.add("12178141");
        //js.joinRequest("2019-06-09", "2019-06-15", tmp);
        //js.test();
        return rootView;
    }

    public void setWeek(String st_date, String ed_date){
        this.ed_date = ed_date;
        this.st_date = st_date;
        selected_week.setText(st_date+" ~ "+ed_date);
    }
}
