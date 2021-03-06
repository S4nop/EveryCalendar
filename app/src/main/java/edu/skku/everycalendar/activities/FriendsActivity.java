package edu.skku.everycalendar.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.skku.everycalendar.R;
import edu.skku.everycalendar.dataType.TimetableData;
import edu.skku.everycalendar.everytime.FriendTimetableReq;
import edu.skku.everycalendar.functions.Utilities;
import edu.skku.everycalendar.table.TableView;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class FriendsActivity extends AppCompatActivity {
    ImageButton back_btn;
    ImageButton option_btn;
    TextView name_text;
    TextView text_excep;
    FrameLayout table_container;
    ArrayList<TimetableData> timeData;
    Context context;
    TableView tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        back_btn = findViewById(R.id.back_btn);
        option_btn = findViewById(R.id.option_btn);

        name_text = findViewById(R.id.friend_name);
        text_excep = findViewById(R.id.text_excep);

        table_container = findViewById(R.id.table_container);
        timeData = getIntent().getParcelableArrayListExtra("Timetable");
        //Log.d("LOG_FRIENDACT", ""+timeData.size());
        name_text.setText(getIntent().getStringExtra("Name"));
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        context = getApplicationContext();
        makeTable();
    }

    public void makeTable(){
        new Thread(){
            @Override
            public void run(){
                table_container.post(new Runnable(){
                    public void run(){
                        try{
                            table_container.removeView(tv);
                        }catch(Exception E){ }

                        tv = new TableView(getApplicationContext(), getStartTime(timeData), getEndTime(timeData) + 1);
                        tv.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                        table_container.addView(tv);
                        table_container.post(new Runnable(){
                            public void run(){
                                try{
                                    tv.addEvents(timeData);
                                    table_container.removeView(tv);
                                    table_container.addView(tv);
                                }catch(Exception e){
                                    catch_excep();
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        }.start();
    }

    private int getStartTime(ArrayList<TimetableData> arrTD){
        int min = 9999;
        for(TimetableData td : arrTD){
            min = min(td.getStartTime(), min);
        }
        return min / 12;
    }

    private int getEndTime(ArrayList<TimetableData> arrTD){
        int max = -1;
        for(TimetableData td : arrTD){
            max = max(td.getEndTime(), max);
        }
        return max / 12;
    }

    public void catch_excep(){
        text_excep.setVisibility(View.VISIBLE);
    }
}
