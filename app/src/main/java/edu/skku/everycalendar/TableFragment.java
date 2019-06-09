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
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.api.client.util.DateTime;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class TableFragment extends Fragment {

    ConstraintLayout clToTable;
    MainActivity activity;
    Context context;
    Activity thisAct;
    String cookie;
    String user_id;
    GoogleCalRequest gCR;
    MyTimeTableReq etR;
    ArrayList<TimetableData> events;
    TableView tv;
    ImageButton select_week_btn;
    TextView period;
    boolean schedFin = false;
    DatabaseReference mPostReference= FirebaseDatabase.getInstance().getReference();
    ArrayList<FirebasePost> inf = new ArrayList<FirebasePost>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_table, container, false);
        if(getArguments()!=null){
            user_id=getArguments().getString("ID");
        }

        clToTable = rootView.findViewById(R.id.clToTable);
        select_week_btn = rootView.findViewById(R.id.select_week_btn);
        period = rootView.findViewById(R.id.textView);

        activity = (MainActivity) getActivity();
        context = activity.getContext();
        thisAct = activity.getThisAct();
        cookie = activity.getCookie();

        String firstday = getCurSunday();
        String lastday = getCurSaturday();

        makeTable(firstday, lastday);
        select_week_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.callDialog();
            }
        });

        return rootView;
    }

    public void makeTable(final String stDate, final String edDate){
        new Thread(){
            @Override
            public void run(){
                etR = new MyTimeTableReq(cookie);
                etR.makeTimeTable();

                Log.d("LOG_MAKETABLE", stDate + " " + edDate);
                gCR = new GoogleCalRequest(context, thisAct, "Account");
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

                clToTable.post(new Runnable(){
                    public void run(){
                        try{
                            clToTable.removeView(tv);
                        }catch(Exception E){}

                        tv = new TableView(context, getStartTime(events), getEndTime(events) + 1);
                        tv.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
                        clToTable.addView(tv);
                        period.setText(stDate+" ~ "+edDate);
                        clToTable.post(new Runnable(){
                            public void run(){
                                tv.addEvents(events);
                                clToTable.removeView(tv);
                                clToTable.addView(tv);
                            }
                        });
                    }
                });
                schedFin = true;
                postUser(user_id,events);
            }
        }.start();
        //buildTable();
    }


    private void buildTable(){
        new Thread(){
            @Override
            public void run(){
                while(!schedFin) {
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Log.d("LOG_BUILDTB", events.toString());


                schedFin = false;

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

    void onActivityResult(Intent data) {
        gCR.pickAcc(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
    }

    public static String getCurSaturday(){
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
        return formatter.format(c.getTime());
    }

    public static String getCurSunday(){
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        return formatter.format(c.getTime());
    }
    public void postUser(String user_id,ArrayList<TimetableData> events){

        Map<String, Object> user_update = new HashMap<>();
        Map<String, Object> user = new HashMap<>();
        FirebasePost post = new FirebasePost(user_id,"",events);
        user = post.toMap();
        user_update.put("/User_information/"+user_id, user);
        Log.d("user_update", user_update.toString());
        mPostReference.updateChildren(user_update);
        Log.d("user_updated",user_update.toString());
    }
    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("onDataChange", "Data is Updated");
                Log.d("getFirebase",snapshot.getKey());
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Log.d("key",snapshot1.getKey());
                    FirebasePost get = snapshot1.getValue(FirebasePost.class);
                    FirebasePost i1 = new FirebasePost(get.getId(), get.getName(), get.getTable());
                    Log.d("getFirebase","getFirebase start");
                    Log.d("getFirebase",get.getId());
                    inf.add(i1);
                    Log.d("inf size",Integer.toString(inf.size()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("User_information").addValueEventListener(postListener);
    }
}
