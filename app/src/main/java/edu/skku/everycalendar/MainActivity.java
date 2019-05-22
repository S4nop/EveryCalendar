package edu.skku.everycalendar;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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
import android.widget.TableLayout;

import com.google.api.client.util.DateTime;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    GoogleCalRequest gCR;
    ET_TimetableRequest etR;
    ArrayList<TimetableData> events;
    TableView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ConstraintLayout clToTable = findViewById(R.id.clToTable);
        Button btn = findViewById(R.id.btnMenu);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        //etR = new ET_TimetableRequest("Cookie");
        //etR.makeTimeTable();

        //gCR = new GoogleCalRequest(getApplicationContext(), this, "Account");
        //gCR.getCalendarData(new DateTime("2019-05-12T00:00:00.000+09:00"), new DateTime("2019-05-18T23:59:59.000+09:00"));

        //tv = new TableView(getApplicationContext());
        //tv.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        //clToTable.addView(tv);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ConstraintLayout clToTable = findViewById(R.id.clToTable);
        buildTable(clToTable);
    }

    private void buildTable(final ConstraintLayout clToTable){
        new Thread(){
            @Override
            public void run(){
                while(!gCR.getFinished()) {
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                events = gCR.getEvents();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_home) {

        } else if (id == R.id.nav_friends) {

        } else if (id == R.id.nav_maketime) {

        } else if (id == R.id.nav_settings) {

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
            gCR.pickAcc(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
        }
    }
}
