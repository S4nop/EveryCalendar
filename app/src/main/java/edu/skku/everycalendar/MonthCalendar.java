package edu.skku.everycalendar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MonthCalendar extends Fragment {

    GridView monthView;
    MonthAdapter monthViewAdapter;
    TextView monthText;
    int curYear;
    int curMonth;
    MainActivity activity;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.calendar_month, container, false);

        activity=(MainActivity)getActivity();
        context=activity.getApplicationContext();
        monthView = rootView.findViewById(R.id.monthview);
        monthViewAdapter = new MonthAdapter(context);
        monthView.setAdapter(monthViewAdapter);

        monthView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MonthItem curItem = (MonthItem) monthViewAdapter.getItem(position);
                int day = curItem.getDay();
                String firstday=getFirstday(curItem);
                String lastday=getLastday(curItem);
                if (day != 0) {
                    Toast.makeText(context, ""+firstday+" / "+lastday, Toast.LENGTH_SHORT).show();
                }
            }
        });

        monthText = rootView.findViewById(R.id.text_month);
        setMonthText();

        ImageButton monthPrevious = rootView.findViewById(R.id.btn_prev_month);
        monthPrevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setPreviousMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });

        ImageButton monthNext = rootView.findViewById(R.id.btn_next_month);
        monthNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setNextMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });
        return rootView;
    }

    private void setMonthText() {
        curYear = monthViewAdapter.getCurYear();
        curMonth = monthViewAdapter.getCurMonth();

        monthText.setText(curYear + "년 " + (curMonth + 1) + "월");
    }
    public String getFirstday(MonthItem item) {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy.MM.dd");

        Calendar c = Calendar.getInstance();

        int y = item.getYear();
        int m = item.getMonth();
        int w = item.getWeek();

        c.set(Calendar.YEAR,y);
        c.set(Calendar.MONTH,m);
        c.set(Calendar.WEEK_OF_MONTH, w);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        return formatter.format(c.getTime());
    }
    public String getLastday(MonthItem item) {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy.MM.dd");

        Calendar c = Calendar.getInstance();

        int y = item.getYear();
        int m = item.getMonth();
        int w = item.getWeek();

        c.set(Calendar.YEAR,y);
        c.set(Calendar.MONTH,m);
        c.set(Calendar.WEEK_OF_MONTH, w);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

        return formatter.format(c.getTime());
    }
}

