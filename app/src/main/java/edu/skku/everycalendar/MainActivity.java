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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    GoogleCalRequest gCR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        Button btn = findViewById(R.id.btnMenu);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        //ET_TimetableRequest etR = new ET_TimetableRequest("Cookie");
        //etR.makeTimeTable();

        //gCR = new GoogleCalRequest(getApplicationContext(), this, "Account");
        //gCR.getCalendarData(new DateTime("2019-05-07T00:00:00.000+09:00"), new DateTime("2019-05-19T23:59:59.000+09:00"));
        //

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        AbsoluteLayout cl = new AbsoluteLayout(getApplicationContext());
        LayoutInflater li = (LayoutInflater)getSystemService(infService);
        cl.setBackgroundColor(Color.parseColor("#00000000"));

        //AbsoluteLayout v = (AbsoluteLayout)li.inflate(R.layout.activity_main, null);
        //v.setBackgroundColor(Color.parseColor("#99000000"));
        AbsoluteLayout.LayoutParams paramll = new AbsoluteLayout.LayoutParams
                (AbsoluteLayout.LayoutParams.MATCH_PARENT, AbsoluteLayout.LayoutParams.MATCH_PARENT, 0, 0);
        addContentView(cl, paramll);

        cl.addView(addSchedule(new TimetableData("테스트", "", "", "2", 144, 156)));
    }

    public Button addSchedule(TimetableData event){
        String title = event.getName();
        String desc = event.getDescript();
        Integer week = Integer.parseInt(event.getWeekDay());
        Integer stTime = event.getStartTime();
        Integer edTime = event.getEndTime();
        int pos[];
        int vWidth, vHeight;

        Button btnSched = new Button(getApplicationContext());

        btnSched.setText(title);
        btnSched.setTop(100);
        btnSched.setLeft(100);
        ConstraintLayout.LayoutParams btnLParam = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        //Log.d("LOGPARAM", Integer.toString(100) + " " + vWidth);
        btnLParam.leftMargin = 0;
        btnLParam.rightMargin = 0;
        btnLParam.width = 100;
        btnLParam.height = (edTime - stTime) * 144 / 12;
        btnSched.setLayoutParams(btnLParam);
        return btnSched;
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
