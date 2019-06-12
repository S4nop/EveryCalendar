package edu.skku.everycalendar.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.api.client.util.DateTime;

import java.util.ArrayList;

import edu.skku.everycalendar.R;
import edu.skku.everycalendar.dataType.TimetableData;
import edu.skku.everycalendar.functions.Utilities;
import edu.skku.everycalendar.googleCalendar.EventListAdapter;
import edu.skku.everycalendar.googleCalendar.EventListItem;
import edu.skku.everycalendar.googleCalendar.GoogleCalRequest;
import edu.skku.everycalendar.googleCalendar.GoogleCalTask;
import edu.skku.everycalendar.monthItems.MonthCalendar;

public class GoogleCalFragment extends Fragment {
    GoogleCalRequest gcr;
    ImageButton btn_add;
    ImageButton btn_month;
    LinearLayout calLayout;
    TextView week_text;

    ListView listView;

    MainActivity activity;
    Context context;

    String stDate;
    String edDate;

    ArrayList<TimetableData> table;
    ArrayList<EventListItem> list;
    EventListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_calendar, container, false);

        activity = (MainActivity) getActivity();
        context = activity.mainContext;
        gcr = new GoogleCalRequest(context, activity.getThisAct());
        btn_add = rootView.findViewById(R.id.btn_add);
        btn_month = rootView.findViewById(R.id.month_btn);
        calLayout = rootView.findViewById(R.id.calLayout);
        week_text = rootView.findViewById(R.id.week_text);

        listView = rootView.findViewById(R.id.listView);

        list = new ArrayList<>();
        adapter = new EventListAdapter(list,context);

        listView.setAdapter(adapter);

//        setWeek(firstday, lastday);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayoutView = inflater.inflate(R.layout.dialog_add_event, null);

                final EditText name_edit = alertLayoutView.findViewById(R.id.name_edit);
                final EditText desc_edit = alertLayoutView.findViewById(R.id.desc_edit);
                final EditText st_year_edit = alertLayoutView.findViewById(R.id.st_year);
                final EditText st_month_edit = alertLayoutView.findViewById(R.id.st_month);
                final EditText st_date_edit = alertLayoutView.findViewById(R.id.st_date);
                final EditText ed_year_edit = alertLayoutView.findViewById(R.id.ed_year);
                final EditText ed_month_edit = alertLayoutView.findViewById(R.id.ed_month);
                final EditText ed_date_edit = alertLayoutView.findViewById(R.id.ed_date);
                final EditText st_hour_edit = alertLayoutView.findViewById(R.id.st_hour);
                final EditText st_min_edit = alertLayoutView.findViewById(R.id.st_min);
                final EditText ed_hour_edit = alertLayoutView.findViewById(R.id.ed_hour);
                final EditText ed_min_edit = alertLayoutView.findViewById(R.id.ed_min);
                final EditText loca_edit = alertLayoutView.findViewById(R.id.loca_edit);

                AlertDialog.Builder builder = new AlertDialog.Builder(context); // mainContext 변경 (-Activity.this -> this)
                builder.setTitle("일정 추가하기");
                builder.setView(alertLayoutView);
                builder.setCancelable(false); // 바깥 클릭해도 안꺼지게


                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = name_edit.getText().toString();
                        String desc = desc_edit.getText().toString();
                        String loca = loca_edit.getText().toString();

                        String st_year = st_year_edit.getText().toString();
                        String st_month = addZero(st_month_edit.getText().toString());
                        String st_date = addZero(st_date_edit.getText().toString());
                        String st_hour = addZero(st_hour_edit.getText().toString());
                        String st_min = addZero(st_min_edit.getText().toString());

                        String ed_year = ed_year_edit.getText().toString();
                        String ed_month = addZero(ed_month_edit.getText().toString());
                        String ed_date = addZero(ed_date_edit.getText().toString());
                        String ed_hour = addZero(ed_hour_edit.getText().toString());
                        String ed_min = addZero(ed_min_edit.getText().toString());

                        try {
                            Log.d("LOG_ADDEVENT", "here");
                            DateTime mSt_date = new DateTime(st_year + "-" + st_month + "-" + st_date + "T" + st_hour + ":" + st_min + ":00.000+09:00");
                            DateTime mEd_date = new DateTime(ed_year + "-" + ed_month + "-" + ed_date + "T" + ed_hour + ":" + ed_min + ":00.000+09:00");
                            gcr.setModeAdd(name, loca, desc, mSt_date, mEd_date);
                            gcr.addEventToCalendar();
                            setWeek(stDate, edDate);
                            activity.getTableFragment().makeTable(stDate, edDate);
                            Log.d("LOG_ADDEVENT", "" + mSt_date + "\n" + mEd_date);
                        }catch(Exception e){
                            Utilities.makeToast("입력을 확인해 주세요");
                        }

                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });

        btn_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthCalendar monthCalendar = new MonthCalendar(context,2);
                monthCalendar.setOnDismissListener((DialogInterface.OnDismissListener) getActivity());
                monthCalendar.show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int index = position;
                LayoutInflater inflater = getLayoutInflater();
                View alertLayoutView = inflater.inflate(R.layout.dialog_event_info, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("일정 정보");
                builder.setView(alertLayoutView);

                TextView name_text = alertLayoutView.findViewById(R.id.name);
                TextView st_date_text = alertLayoutView.findViewById(R.id.st_date);
                TextView ed_date_text = alertLayoutView.findViewById(R.id.ed_date);
                TextView loca_text = alertLayoutView.findViewById(R.id.loca);
                TextView desc_text = alertLayoutView.findViewById(R.id.desc);

                EventListItem item = list.get(position);

                String name = item.getEvent_name();

                String loca = item.getEvent_loca();
                String desc = item.getEvent_desc();

                name_text.setText(name);
                loca_text.setText(loca);
                desc_text.setText(desc);

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final String calendarId = list.get(index).getEvent_id();

                            new Thread(){
                                @Override
                                public void run(){

                                    try{
                                        gcr = new GoogleCalRequest(context, activity.getThisAct());
                                        gcr.setModeRemove(calendarId);
                                        gcr.removeCalendarData();
                                        activity.getTableFragment().makeTable(stDate, edDate);
                                    }catch(Exception e){
                                        e.printStackTrace();
                                        Log.d("Delete","100");
                                    }
                                    setWeek(stDate, edDate);
                                }
                            }.start();
                        list.remove(index);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.show();
            }
        });

        return rootView;
    }


    private String addZero(String s){
        if(s.length() == 1) return "0" + s;
        else return s;
    }

    public void setWeek(final String stDate, final String edDate){
        new Thread(){
            @Override
            public void run(){
                gcr = new GoogleCalRequest(context, activity.getThisAct());
                gcr.setModeGet(new DateTime(stDate + "T00:00:00.000+09:00"), new DateTime(edDate + "T23:59:59.000+09:00"));
                gcr.getCalendarData();

                while(!gcr.getFinished()) {
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                setTable(gcr.getEvents(), stDate, edDate);
            }
        }.start();
    }

    public void setTable(ArrayList<TimetableData> events, final String stDate, final String edDate){
        this.stDate = stDate;
        this.edDate = edDate;
        table = events;
        list.clear();
        if(table!=null){
            for(TimetableData td : table){
                EventListItem item = new EventListItem(td.getName(), td.getStartTime().toString(), td.getEndTime().toString(), td.getPlace(),td.getDescript(), td.getIdNum());
                list.add(item);
            }
        }
        calLayout.post(new Runnable(){
            public void run() {
                listView.setAdapter(adapter);
                week_text.setText(stDate + " ~ " + edDate);
            }
        });
    }
}
