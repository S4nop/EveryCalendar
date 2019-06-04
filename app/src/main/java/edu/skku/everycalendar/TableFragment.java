package edu.skku.everycalendar;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class TableFragment extends Fragment {

    ConstraintLayout clToTable;
    MainActivity activity;
    Context context;
    Activity thisAct;
    String cookie;
    GoogleCalRequest gCR;
    MyTimeTableReq etR;
    ArrayList<TimetableData> events;
    TableView tv;
    ImageButton select_week_btn;
    TextView period;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_table, container, false);

        clToTable = rootView.findViewById(R.id.clToTable);
        select_week_btn = rootView.findViewById(R.id.select_week_btn);

        activity = (MainActivity) getActivity();
        context = activity.context;
        thisAct = activity.thisAct;
        cookie = activity.cookie;

        tv = new TableView(context);
        tv.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        clToTable.addView(tv);

        buildTable(clToTable);

        select_week_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.callDialog();
            }
        });

        String firstday = getCurSunday();
        String lastday = getCurSaturday();
        period = rootView.findViewById(R.id.textView);
        period.setText(firstday+" ~ "+lastday);

        return rootView;
    }

    private void buildTable(final ConstraintLayout clToTable){
        new Thread(){
            @Override
            public void run(){
                etR = new MyTimeTableReq(cookie);
                etR.makeTimeTable();

//                gCR = new GoogleCalRequest(context, thisAct, "Account");
//                gCR.getCalendarData(new DateTime("2019-05-12T00:00:00.000+09:00"), new DateTime("2019-05-18T23:59:59.000+09:00"));

                while(!etR.getFinished()/* || !gCR.getFinished()*/) {
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                events = etR.getClassList();
                //events.addAll(gCR.getEvents());
                Log.d("LOG_BUILDTB", events.toString());

                clToTable.post(new Runnable(){
                    public void run(){
                        tv.addEvents(events);
                        clToTable.removeView(tv);
                        clToTable.addView(tv);
                    }
                });

            }
        }.start();
    }

    void onActivityResult(Intent data) {
        gCR.pickAcc(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
    }

    public static String getCurSaturday(){
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy.MM.dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
        return formatter.format(c.getTime());
    }

    public static String getCurSunday(){
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy.MM.dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        return formatter.format(c.getTime());
    }
    public void setPeriod(String string){
        period.setText(string);
    }
}
