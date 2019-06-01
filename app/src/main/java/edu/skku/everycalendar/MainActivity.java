package edu.skku.everycalendar;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;

import com.google.api.client.util.DateTime;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    GoogleCalRequest gCR;
    MyTimeTableReq etR;
    ArrayList<TimetableData> events;
    TableView tv;
    ImageButton menu_btn;
    Context context;
    Activity thisAct;
    String cookie;
    BottomNavigationView bottomBar;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private TableFragment tableFragment = new TableFragment();
    private FriendsFragment friendsFragment = new FriendsFragment();
    private AdjustFragment adjustFragment = new AdjustFragment();
    private MonthCalendar monthCalendar = new MonthCalendar();
    Fragment active = tableFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomBar = findViewById(R.id.bottomNavigationView);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        cookie = getIntent().getStringExtra("Cookie");
        menu_btn = findViewById(R.id.btnMenu);
        menu_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        context = getApplicationContext();
        thisAct = this;

        fragmentManager.beginTransaction().add(R.id.container, friendsFragment,"3").hide(friendsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container, adjustFragment,"2").hide(adjustFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container, tableFragment,"1").hide(tableFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container, monthCalendar,"0").commit();


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

                    case R.id.action_adjust:{
                        fragmentManager.beginTransaction().hide(active).show(adjustFragment).commit();
                        active = adjustFragment;

                        break;
                    }
                }

                return true;
            }
        });

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
}
