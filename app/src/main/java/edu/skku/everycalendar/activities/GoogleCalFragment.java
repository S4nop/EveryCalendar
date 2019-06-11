package edu.skku.everycalendar.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.skku.everycalendar.R;
import edu.skku.everycalendar.googleCalendar.EventListAdapter;
import edu.skku.everycalendar.googleCalendar.EventListItem;
import edu.skku.everycalendar.monthItems.MonthCalendar;

public class GoogleCalFragment extends Fragment {
    ImageButton btn_add;
    ImageButton btn_month;

    TextView week_text;

    ListView listView;

    MainActivity activity;
    Context context;

    String st_date;
    String ed_date;

    ArrayList<EventListItem> list;
    EventListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_calendar, container, false);

        activity = (MainActivity) getActivity();
        context = activity.mainContext;

        btn_add = rootView.findViewById(R.id.btn_add);
        btn_month = rootView.findViewById(R.id.month_btn);

        week_text = rootView.findViewById(R.id.week_text);

        listView = rootView.findViewById(R.id.listView);

        list = new ArrayList<>();
        adapter = new EventListAdapter(list,context);

        listView.setAdapter(adapter);

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
                        String st_month = st_month_edit.getText().toString();
                        String st_date = st_date_edit.getText().toString();
                        String st_hour = st_hour_edit.getText().toString();
                        String st_min = st_min_edit.getText().toString();

                        String ed_year = ed_year_edit.getText().toString();
                        String ed_month = ed_month_edit.getText().toString();
                        String ed_date = ed_date_edit.getText().toString();
                        String ed_hour = ed_hour_edit.getText().toString();
                        String ed_min = ed_min_edit.getText().toString();

                        //형식 변환된 date, time ( yyyy-mm-dd, hh:mm )
                        String form_st_date =st_year+"-"+st_month+"-"+st_date+" "+st_hour+":"+st_min;
                        String form_ed_date =ed_year+"-"+ed_month+"-"+ed_date+" "+ed_hour+":"+ed_min;

                        //구글 캘린더에 넣을 때 date 형식이어야 하면 쓰세요
                        try {
                            Date mSt_date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(form_st_date);
                            Date mEd_date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(form_ed_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        EventListItem item = new EventListItem(name,form_st_date,form_ed_date,loca,desc);
                        list.add(item);
                        adapter.notifyDataSetChanged();
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
                String st_date = item.getEvent_st_date();
                String ed_date = item.getEvent_ed_date();
                String loca = item.getEvent_loca();
                String desc = item.getEvent_desc();

                name_text.setText(name);
                st_date_text.setText(st_date);
                ed_date_text.setText(ed_date);
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
                        list.remove(index);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.show();
            }
        });

        return rootView;
    }

    public void setWeek(String st_date, String ed_date){
        this.ed_date = ed_date;
        this.st_date = st_date;
        week_text.setText(st_date+" ~ "+ed_date);
    }
}