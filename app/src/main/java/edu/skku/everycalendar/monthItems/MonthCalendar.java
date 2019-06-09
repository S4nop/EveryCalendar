package edu.skku.everycalendar.monthItems;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;

import edu.skku.everycalendar.R;
import edu.skku.everycalendar.functions.Utilities;

public class MonthCalendar extends Dialog{

    GridView monthView;
    MonthAdapter monthViewAdapter;
    TextView monthText;
    int curYear;
    int curMonth;
    Context context;
    ImageButton monthPrevious;
    ImageButton monthNext;
    String stDate, edDate;
    OnDismissListener _listener;
    boolean cng = false;
    int fragment_flag; //0:table, 1:adjust

    public MonthCalendar(Context context, int flag) {
        super(context);
        this.context = context;
        this.fragment_flag = flag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.dialog_calendar_month);

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
                stDate=getFirstday(curItem);
                edDate=getLastday(curItem);
                if (day != 0) {
                    Utilities.makeToast(context, stDate + " / "+ edDate);
                    cng = true;
                    Log.d("LOG_DISMISS", _listener == null ? "NULL" : "X");
                    if(_listener != null)
                        _listener.onDismiss(MonthCalendar.this);
                    dismiss();
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
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");

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
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");

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

    public void setOnDismissListener(OnDismissListener $listener){
        _listener = $listener;
    }

    public boolean getCng() { return cng; }
    public String getStDate(){ return stDate; }
    public String getEdDate() { return edDate; }
    public int getFlag(){return fragment_flag;}
}

