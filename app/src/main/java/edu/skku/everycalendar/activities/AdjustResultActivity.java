package edu.skku.everycalendar.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.api.client.util.DateTime;

import java.io.File;
import java.util.ArrayList;

import edu.skku.everycalendar.R;
import edu.skku.everycalendar.dataType.TimetableData;
import edu.skku.everycalendar.everytime.MyTimeTableReq;
import edu.skku.everycalendar.functions.CaptureScreen;
import edu.skku.everycalendar.googleCalendar.GoogleCalRequest;
import edu.skku.everycalendar.table.TableView;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class AdjustResultActivity extends AppCompatActivity {

    ImageButton btn_back;
    ImageButton btn_save;
    Button btn_share;
    FrameLayout frmResult;
    Activity thisAct;
    ArrayList<TimetableData> timeData;
    ArrayList<String> fList;
    TableView tv;
    TextView txtFriends;
    String fs = "";
    int stTime, edTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_result);
        //Log.d("LOG_RESULTACT", "Act Start");
        btn_back = findViewById(R.id.btn_back);
        btn_save = findViewById(R.id.btn_save);
        frmResult = findViewById(R.id.frmResult);
        txtFriends = findViewById(R.id.txtFriends);
        btn_share = findViewById(R.id.btnShare);
        thisAct = this;
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        timeData = getIntent().getParcelableArrayListExtra("Timetable");

        for(TimetableData td : timeData){
            //Log.d("LOG_RESULTACT", "! " + td.getName() + " " + td.getWeekDay() + " " + td.getStartTime());
        }
        stTime = getIntent().getIntExtra("stTime", stTime);
        edTime = getIntent().getIntExtra("edTime", edTime);
        fList = getIntent().getStringArrayListExtra("friends");

        for(String s : fList){
            fs += s + " ";
        }
        txtFriends.setText(fs);
        //Log.d("LOG_RESULTACT", ""+ timeData.size() + " - " + stTime + " - " + edTime);



        makeTable();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("LOG_ARA", "Click!!");
                fs = "";
                for(String s : fList){
                    fs += s;
                }
                CaptureScreen.capture(thisAct, fs);
            }

        });

        btn_share.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fs = "";
                for(String s : fList){
                    fs += s;
                }
                CaptureScreen.capture(thisAct, fs);

                Intent intent = new Intent();
                Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/EveryCalendar/"+fs+"png"));
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setType("image/*");

                startActivity(Intent.createChooser(intent, "결과 공유"));
            }
        });
    }
    public void makeTable(){
        new Thread(){
            @Override
            public void run(){
                frmResult.post(new Runnable(){
                    public void run(){
                        try{
                            frmResult.removeView(tv);
                        }catch(Exception E){}

                        tv = new TableView(getApplicationContext(), stTime, edTime + 1);
                        tv.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                        frmResult.addView(tv);
                        frmResult.post(new Runnable(){
                            public void run(){
                                tv.addEvents(timeData);
                                frmResult.removeView(tv);
                                frmResult.addView(tv);
                            }
                        });
                    }
                });
            }
        }.start();
    }
}
