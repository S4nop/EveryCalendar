package edu.skku.everycalendar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MonthCalendar extends Dialog{

    GridView monthView;
    MonthAdapter monthViewAdapter;
    TextView monthText;
    int curYear;
    int curMonth;
    Context context;
    ImageButton monthPrevious;
    ImageButton monthNext;

    public MonthCalendar(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.calendar_month);

        monthView = findViewById(R.id.monthview);
        monthViewAdapter = new MonthAdapter(context);
        monthView.setAdapter(monthViewAdapter);

        monthNext = findViewById(R.id.btn_next_month);
        monthPrevious = findViewById(R.id.btn_prev_month);
        monthText = findViewById(R.id.text_month);

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

        setMonthText();

        monthPrevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setPreviousMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });

        monthNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setNextMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });
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

