package edu.skku.everycalendar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import org.mortbay.jetty.Main;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DialogInterface.OnDismissListener {
    private ImageButton menu_btn;
    public Context context;
    private Activity thisAct;
    private String cookie;
    private String id;
    private BottomNavigationView bottomBar;
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
    Fragment active = tableFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cookie = getIntent().getStringExtra("Cookie");
        id = getIntent().getStringExtra("ID");

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
        name_text.setText(rslt.split("::")[0]);
        info_text.setText(rslt.split("::")[1]);

        menu_btn = findViewById(R.id.btnMenu);
        menu_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        context = MainActivity.this;
        thisAct = this;

        fragmentManager.beginTransaction().add(R.id.container, googleCalFragment,"4").hide(googleCalFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container, friendsFragment,"3").hide(friendsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container, adjustFragment,"2").hide(adjustFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container, tableFragment,"1").commit();
        active = tableFragment;


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

        ServiceMaker sm = new ServiceMaker();
        sm.setActivity(context, id);
        sm.startServ();
        sm.bindServ();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    public Context getContext() {
        return context;
    }

    public String getCookie() {
        return cookie;
    }

    public String getId() {
        return id;
    }

    public void callDialog(){
        MonthCalendar monthCalendar = new MonthCalendar(MainActivity.this);
        monthCalendar.setOnDismissListener(this);
        monthCalendar.show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        MonthCalendar monthCalendar = (MonthCalendar) dialog;
        Log.d("LOG_ONDISMISS", "here?");
        if(monthCalendar.getCng()) {
            tableFragment.makeTable(monthCalendar.getStDate(), monthCalendar.getEdDate());
        }
    }
}
