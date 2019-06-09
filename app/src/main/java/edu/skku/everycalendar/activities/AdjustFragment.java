package edu.skku.everycalendar.activities;

import android.content.Context;
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
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Map;

import edu.skku.everycalendar.friends.FriendsListItem;
import edu.skku.everycalendar.friends.FriendsSelectAdapter;
import edu.skku.everycalendar.functions.JoinSchedule;
import edu.skku.everycalendar.R;

public class AdjustFragment extends Fragment {
    Button result_btn;
    Button reset_btn;
    Button month_btn;

    TimePicker start_picker;
    TimePicker end_picker;

    MainActivity activity;
    Context context;
    ListView listView;

    ArrayList<FriendsListItem> friends_list;
    FriendsSelectAdapter adapter;

    Map<String, String> list_map;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_adjust, container, false);
        inflater.inflate(R.layout.fragment_adjust, container, false);

        activity = (MainActivity) getActivity();
        context = activity.mainContext;

        listView = rootView.findViewById(R.id.friends_list);
        result_btn = rootView.findViewById(R.id.btn_result);
        reset_btn = rootView.findViewById(R.id.btn_reset);

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
                        checked_list.add(friends_list.get(i));
                    }
                }

                listView.clearChoices();

                //hour은 24시 기준 (오후 10시 -> 22시)
                start_hour = start_picker.getHour();
                start_min = start_picker.getMinute();
                end_hour = end_picker.getHour();
                end_min = end_picker.getMinute();

                Intent intent = new Intent(context, AdjustResultActivity.class);
                startActivity(intent);
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
                //activity.callDialog();
            }
        });
        JoinSchedule js = new JoinSchedule(9, 20);
        //js.test();
        return rootView;
    }
}
