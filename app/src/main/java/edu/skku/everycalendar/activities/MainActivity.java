package edu.skku.everycalendar.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import android.widget.ImageButton;
import android.widget.TextView;

import com.google.api.client.util.DateTime;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.skku.everycalendar.dataType.FriendInfoData;
import edu.skku.everycalendar.dataType.TimetableData;
import edu.skku.everycalendar.everytime.FriendsListRequest;
import edu.skku.everycalendar.everytime.MyTimeTableReq;
import edu.skku.everycalendar.friends.FriendsListItem;
import edu.skku.everycalendar.friends.recommendFriend;
import edu.skku.everycalendar.functions.BackButtonHandler;
import edu.skku.everycalendar.functions.CallableArg;
import edu.skku.everycalendar.R;
import edu.skku.everycalendar.functions.CheckOurUser;
import edu.skku.everycalendar.functions.Utilities;
import edu.skku.everycalendar.service.ServiceMaker;
import edu.skku.everycalendar.everytime.GetNameRequest;
import edu.skku.everycalendar.googleCalendar.GoogleCalRequest;
import edu.skku.everycalendar.monthItems.MonthCalendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DialogInterface.OnDismissListener {
    private ImageButton menu_btn;
    public Context mainContext;
    private Activity thisAct;
    private String cookie;
    private String id;
    private String name;
    private String info;
    public BottomNavigationView bottomBar;
    private NavigationView nav_view;
    private View nav_header;
    private DrawerLayout drawer;

    private TextView name_text;
    private TextView info_text;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private TableFragment tableFragment = new TableFragment();
    private FriendsFragment friendsFragment = new FriendsFragment();
    private AdjustFragment adjustFragment = new AdjustFragment();
    private GoogleCalFragment googleCalFragment = new GoogleCalFragment();
    private recommendFriend rcmFrnd = new recommendFriend();
    private boolean friendListFin = false;
    Fragment active = tableFragment;
    ServiceMaker sm = new ServiceMaker();

    public ArrayList<FriendsListItem> friends_list;
    public HashMap<String, String> friends_list_with_id;
    public Map<String, FriendInfoData> friendList;
    private BackButtonHandler backButtonHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-------------TESTING----------------
//        ArrayList<String> tmp = new ArrayList<>();
//        tmp.add("182812");
//        tmp.add("12178141");
//        JoinSchedulReq jsr = new JoinSchedulReq();
//        jsr.joinRequest("2019-06-09", "2019-06-15", tmp);
        //------------------------------------
        cookie = getIntent().getStringExtra("Cookie");
        id = getIntent().getStringExtra("ID");
        backButtonHandler = new BackButtonHandler(this);

        bottomBar = findViewById(R.id.bottomNavigationView);
        drawer = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);

        nav_view.setNavigationItemSelectedListener(this);
        nav_header = nav_view.getHeaderView(0);

        name_text = nav_header.findViewById(R.id.name_text);
        info_text = nav_header.findViewById(R.id.info_text);

        //set nav_header's info
        GetNameRequest gnr = new GetNameRequest(cookie);

        String rslt = gnr.getName();
        name_text.setText(name = rslt.split("::")[0]);
        info_text.setText(info = rslt.split("::")[1]);
        rcmFrnd.setId(name);
        Bundle bundle = new Bundle();
        bundle.putString("ID", id);
        bundle.putString("Name",name);
        tableFragment.setArguments(bundle);

        menu_btn = findViewById(R.id.btnMenu);
        menu_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        mainContext = MainActivity.this;
        thisAct = this;

        Utilities.setContext(mainContext);
        //get friends list
        friends_list = new ArrayList<>();

        final FriendsListRequest friendsListRequest = new FriendsListRequest(getCookie());
        friendsListRequest.makeFriendList();

        new Thread(){
            @Override
            public void run(){
                while(!friendsListRequest.isFinished())
                    try{sleep(500);}
                    catch(Exception e){}
                friendList = friendsListRequest.getFriendList();
                Iterator<String> iterator = friendList.keySet().iterator();
                Log.d("LOG_MAINACT", "" + friendList.size());
                while(iterator.hasNext()){
                    String name = iterator.next();
                    //Log.d("LOG_MAINACT_FR", friendList.get(name).getClasses().toString());
                    String key = friendList.get(name).getKey();
                    FriendsListItem item = new FriendsListItem(name);
                    friends_list.add(item);
                }
                friendListFin = true;
            }
        }.start();


        fragmentManager.beginTransaction().add(R.id.container, googleCalFragment,"4").hide(googleCalFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container, friendsFragment,"3").hide(friendsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container, adjustFragment,"2").hide(adjustFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container, tableFragment,"1").commit();
        active = tableFragment;

        CheckOurUser.getUserList();

        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.acton_schedules:{
                        fragmentManager.beginTransaction().hide(active).show(tableFragment).commit();
                        active = tableFragment;

                        break;
                    }

                    case R.id.action_friends:{
                        fragmentManager.beginTransaction().hide(active).show(friendsFragment).commit();
                        active = friendsFragment;

                        break;
                    }

                    case R.id.action_calendar:{
                        fragmentManager.beginTransaction().hide(active).show(googleCalFragment).commit();
                        active = googleCalFragment;

                        break;
                    }

                    case R.id.action_adjust:{
                        fragmentManager.beginTransaction().hide(active).show(adjustFragment).commit();
                        active = adjustFragment;

                        break;
                    }
                }

                return true;
            }
        });

        startService();
    }

    private void startService(){
        if(isServiceRunningCheck()) {
            sm.stopServ();
        }
            Log.d("ID", id);
            sm.setActivity(mainContext, id);
            sm.startServ();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("RcvData");
            final String stDate = message.split("stDate=")[1].split("\\}")[0];
            final String edDate = message.split("edDate=")[1].split(",")[0];
            final String reqID = message.split("reqID=")[1].split(",")[0];
            Log.d("receiver", "Got message");
            makeAlert("시간표 조율 요청", "친구가 시간표 조율을 요청했습니다.\n친구에게 시간표를 전송하시겠습니까?",
                    "전송", true,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sendTables(stDate, edDate, reqID);
                            FirebaseDatabase.getInstance().getReference().child("SchedJoinReq").child(id).removeValue();
                            //Utilities.makeToast(mainContext, "시간표가 전송되었습니다");
                        }
                    }
             );
        }
    };

    private void sendTables(final String stDate, final String edDate, final String reqID){
        new Thread(){
            @Override
            public void run(){
                ArrayList<TimetableData> events;
                MyTimeTableReq etR = new MyTimeTableReq(cookie);
                etR.makeTimeTable();

                GoogleCalRequest gCR = new GoogleCalRequest(mainContext, thisAct, "Account");
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

                Map<String, Object> upd = new HashMap<>();
                Map<String, ArrayList<TimetableData>> pack = new HashMap<>();
                pack.put(id, events);
                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                upd.put("/SchedJoin/" + reqID + "/" + id, events);
                mRef.updateChildren(upd);
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver( mMessageReceiver, new IntentFilter("DataReceiver"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver( mMessageReceiver);
    }

    private void makeAlert(String title, String msg, String posBtn, boolean setCancelable, DialogInterface.OnClickListener listener){
        AlertDialog.Builder adb = new AlertDialog.Builder(mainContext);

        adb.setTitle(title);

        adb
                .setMessage(msg)
                .setPositiveButton(posBtn, listener)
                .setNegativeButton("취소", null)
                .setCancelable(setCancelable);
        AlertDialog ad = adb.create();
        ad.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            backButtonHandler.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_setting) {
            // Handle the camera action
        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("LOG_ACRESLT", "Here");

        if (requestCode == GoogleCalRequest.REQUEST_ACC_PICK && resultCode == RESULT_OK && data != null && data.getExtras() != null) {
            tableFragment.onActivityResult(data);
        }
    }

    public Activity getThisAct() {
        return thisAct;
    }

    public Context getMainContext() {
        return mainContext;
    }

    public String getCookie() {
        return cookie;
    }

    public String getId() {
        return id;
    }

    public void callDialog(){
        MonthCalendar monthCalendar = new MonthCalendar(MainActivity.this,0);
        monthCalendar.setOnDismissListener(this);
        monthCalendar.show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        MonthCalendar monthCalendar = (MonthCalendar) dialog;
        Log.d("LOG_ONDISMISS", "here?");
        if(monthCalendar.getFlag()==1){
            adjustFragment.setWeek(monthCalendar.getStDate(), monthCalendar.getEdDate());
        }
        else if(monthCalendar.getFlag()==2){
            googleCalFragment.setWeek(monthCalendar.getStDate(), monthCalendar.getEdDate());
        }
        else if(monthCalendar.getCng()) {
            tableFragment.makeTable(monthCalendar.getStDate(), monthCalendar.getEdDate());
        }
    }

    public boolean isServiceRunningCheck() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("edu.skku.everycalendar.service".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public recommendFriend getRcmFrnd() {
        return rcmFrnd;
    }

    public boolean isFriendListFin() {
        return friendListFin;
    }

    public Map<String, FriendInfoData> getFriendList() {
        return friendList;
    }

}
