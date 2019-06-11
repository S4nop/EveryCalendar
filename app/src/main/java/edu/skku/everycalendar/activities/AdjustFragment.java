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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.api.client.util.DateTime;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.skku.everycalendar.dataType.FriendInfoData;
import edu.skku.everycalendar.dataType.TimetableData;
import edu.skku.everycalendar.everytime.FriendTimetableReq;
import edu.skku.everycalendar.everytime.MyTimeTableReq;
import edu.skku.everycalendar.friends.FriendsListItem;
import edu.skku.everycalendar.friends.FriendsSelectAdapter;
import edu.skku.everycalendar.functions.CheckOurUser;
import edu.skku.everycalendar.functions.JoinSchedulReq;
import edu.skku.everycalendar.functions.JoinSchedule;
import edu.skku.everycalendar.R;
import edu.skku.everycalendar.functions.Utilities;
import edu.skku.everycalendar.googleCalendar.GoogleCalRequest;
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
    ScrollView scrollView;

    Map<String, FriendInfoData> friends_list;
    //Map<String, String> friends_list_to_tt;
    FriendsSelectAdapter adapter;


    String ed_date = null;
    String st_date = null;
    JoinSchedule js;
    JoinSchedulReq jsr;
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
        scrollView = rootView.findViewById(R.id.scroll);

        start_picker = rootView.findViewById(R.id.start_time);
        end_picker = rootView.findViewById(R.id.end_time);


        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        new Thread() {
            @Override
            public void run() {
                while (!activity.isFriendListFin())
                    try {
                        sleep(500);
                    } catch (Exception e) {
                    }
                friends_list = activity.friendList;
                adapter = new FriendsSelectAdapter(activity.friends_list);
                listView.setAdapter(adapter);

            }
        }.start();
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
                final ArrayList<String> checked_list = new ArrayList<>();

                for (int i = count-1; i >= 0; i--) {
                    if (isChecked.get(i)) {
                        FriendsListItem item = activity.friends_list.get(i);
                        Log.d("get_checked",item.getFriend_name());
                        checked_list.add(item.getFriend_name());
                    }
                }

                //hour은 24시 기준 (오후 10시 -> 22시)
                start_hour = start_picker.getHour();
                start_min = start_picker.getMinute();
                end_hour = end_picker.getHour();
                end_min = end_picker.getMinute();

                //st_date, ed_date 저장되어 있음

                if(false && checked_list.size()==0){
                    Utilities.makeToast("친구를 선택해 주세요!");
                }
                else if (st_date==null||ed_date==null){
                    Utilities.makeToast("주를 선택해 주세요!");
                }
                else if((start_hour>=end_hour)){
                    Utilities.makeToast("시간 선택이 올바르지 않습니다");
                }
                else{
                    jsr = new JoinSchedulReq();
                    ArrayList<String> fList = new ArrayList<>();
                    //fList.add(activity.getId());
                    js = new JoinSchedule(start_picker.getHour(), end_picker.getHour());
                    for(final String fname : checked_list){
                        //Log.d("LOG_CHKUSER", friends_list.get(fl.getFriend_name()));
                        if(CheckOurUser.chkUser(friends_list.get(fname)))
                            fList.add(friends_list.get(fname).getId());
                        else {
                            Utilities.makeToast("EveryCalendar를 사용하지 않는 인원이 포함되어 있습니다\n해당 인원은 Everytime시간표를 이용하여 조율합니다");
                            new Thread(){
                                @Override
                                public void run(){
                                    js.addEvents(friends_list.get(fname).getClasses());
                                    jsr.addDBNum();
                                }
                            }.start();
                        }
                        //TODO : Check friend is user of our app
                    }
                    jsr.setfNum2(fList.size());
                    jsr.setFnum(checked_list.size() + 1);
                    addMyTables(st_date, ed_date, js, jsr);
                    checked_list.add(activity.getName());
                    jsr.joinRequest(st_date, ed_date, fList, js);
                    reset_btn.callOnClick();
                }


                    new Thread(){
                        @Override
                        public void run(){
                            try {
                                while(js==null || !js.getFinished())
                                    sleep(1000);
                                if(jsr.getID() != null)
                                    FirebaseDatabase.getInstance().getReference().child("SchedJoin").child(jsr.getID()).removeValue();
                                Intent intent = new Intent(context, AdjustResultActivity.class);
                                intent.putParcelableArrayListExtra("Timetable", js.getRslt());
                                intent.putExtra("stTime", js.getStTime());
                                intent.putExtra("edTime", js.getEdTime());
                                intent.putStringArrayListExtra("friends", checked_list);
                                startActivity(intent);
                            } catch (InterruptedException e) {}
                        }
                    }.start();
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
        //js.test();
        return rootView;
    }

    public void setWeek(String st_date, String ed_date){
        this.ed_date = ed_date;
        this.st_date = st_date;
        selected_week.setText(st_date+" ~ "+ed_date);
    }
    private void addMyTables(final String stDate, final String edDate, final JoinSchedule js, final JoinSchedulReq jsr){
        new Thread(){
            @Override
            public void run(){
                ArrayList<TimetableData> events;
                MyTimeTableReq etR = new MyTimeTableReq(activity.getCookie());
                etR.makeTimeTable();

                GoogleCalRequest gCR = new GoogleCalRequest(context, activity.getThisAct(), "Account");
                gCR.getCalendarData(new DateTime(stDate + "T00:00:00.000+09:00"), new DateTime(edDate + "T23:59:59.000+09:00"));

                while(!etR.getFinished() || !gCR.getFinished()) {
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                events = etR.getClassList();
                events.addAll(gCR.getEvents());
                js.addEvents(events);
                jsr.addDBNum();
            }
        }.start();
    }
}
