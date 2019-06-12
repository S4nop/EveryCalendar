package edu.skku.everycalendar.googleCalendar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import edu.skku.everycalendar.dataType.TimetableData;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class GoogleCalRequest implements EasyPermissions.PermissionCallbacks {

    Context context;
    Activity mainAct;
    public GoogleAccountCredential mCred;
    ArrayList<TimetableData> events;
    GoogleCalTask googleCalTask;
    boolean finished = false;
    int gThreadRunning = 0;
    int mod = 0;
    DateTime stDate, edDate;
    String add_Sum, add_loc, add_Desc, sum;
    public static final int REQUEST_ACC_PICK = 1000;
    public static final int REQUEST_PERM_GET_ACC = 1003;


    String accName = null;
    private static final String[] SCOPES = {CalendarScopes.CALENDAR};

    public GoogleCalRequest(Context context, Activity mainAct) {
        this.context = context;
        this.mainAct = mainAct;
        events = new ArrayList<>();

        mCred = GoogleAccountCredential.usingOAuth2(
                context,
                Arrays.asList(SCOPES)
        ).setBackOff(new ExponentialBackOff());
    }

    public boolean getFinished(){
        return finished;
    }

    public void setModeGet(DateTime stDate, DateTime edDate){
        this.mod = 1;
        this.stDate = stDate;
        this.edDate = edDate;
    }

    public void setModeAdd(String add_Sum, String add_Loc, String add_Desc, DateTime stDate, DateTime edDate){
        this.mod = 2;
        this.stDate = stDate;
        this.edDate = edDate;
        this.add_Desc = add_Desc;
        this.add_loc = add_Loc;
        this.add_Sum = add_Sum;
    }

    public void setModeRemove(String name){
        this.mod = 3;
        this.sum = name;
    }
    public void getCalendarData() {
        googleCalTask = new GoogleCalTask(mCred);
        googleCalTask.setModeGet(stDate, edDate);
        finished = false;
        executeTask();
    }

    public void addEventToCalendar(){
        if (!chkGoogleServAvail()) {
            acqGoogleServ();
        } else if (mCred.getSelectedAccountName() == null) {
            chooseAcc();
        }
        googleCalTask = new GoogleCalTask(mCred);
        googleCalTask.setModeAdd(add_Sum, add_loc, add_Desc, stDate, edDate);

        pickAcc(null);
    }

    private void executeTask(){
        //Log.d("LOG_EXECUTE", "herere");
        if (!chkGoogleServAvail()) {
            acqGoogleServ();
        } else if (mCred.getSelectedAccountName() == null) {
            chooseAcc();
        }
        else{
            //Log.d("LOG_EXECUTE", "ThrdRun");
            new Thread(){
                @Override
                public void run(){
                    List<Event> eventList = null;
                    try {
                        if(gThreadRunning == 0){
                            gThreadRunning = 1;
                            Log.d("LOG_GTHREAD", "1");
                            eventList = googleCalTask.execute().get();
                            if(eventList != null)
                                parseEvents(eventList);
                            googleCalTask = null;
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (CancellationException e){
                        e.printStackTrace();
                    }
                    gThreadRunning = 0;
                    Log.d("LOG_GTHREAD", "0");
                    finished = true;
                }
            }.start();

        }
    }

    private void parseEvents(List<Event> items){
        for (Event event : items) {

            compData(event.getStart().getDateTime(), event.getEnd().getDateTime(), event);


            //TODO : Need to handle case - Events' start date and end date are in different week

            //Log.d("LOG_RESLT", String.format("%s \n (%s)", event.getSummary(), event.getStart().getDateTime().toString()));
            DateTime tmp;
        }
    }

    public void compData(DateTime stDate, DateTime edDate, Event event)
    {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date std, edd;
            try {
                std = format.parse(stDate.toString().substring(0, 10));
                edd = format.parse(edDate.toString().substring(0, 10));
                long calDateDays = (edd.getTime() - std.getTime()) / ( 24*60*60*1000);
                Calendar c = Calendar.getInstance();
                c.setTime(std);
                //Log.d("LOG_CAL", c.toString());
                int week = c.get(Calendar.DAY_OF_WEEK) - 1;
                Random rnd = new Random();
                int color = Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
                //Log.d("LOG_COMPDATA", Long.toString(calDateDays));
                for(long i = 0; i <= calDateDays; i++){
                    events.add(new TimetableData(event.getSummary(), event.getLocation(),
                            event.getDescription(), Integer.toString(week), timeToInt(stDate), timeToInt(edDate), "", color));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

    }


    private Integer timeToInt(DateTime dt){
        String strDT = dt.toString();
        Integer tmp = ((Integer.parseInt(strDT.substring(11, 13)) * 60 + Integer.parseInt(strDT.substring(14,16))) / 5);
        //Log.d("LOG_TIMETOINT", strDT + " " + Integer.toString(tmp));
        return ((Integer.parseInt(strDT.substring(11, 13)) * 60 + Integer.parseInt(strDT.substring(14,16))) / 5);
    }


    private boolean chkGoogleServAvail() {

        GoogleApiAvailability apiAvail = GoogleApiAvailability.getInstance();

        return apiAvail.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS;
    }

    private void acqGoogleServ() {
        GoogleApiAvailability apiAvail = GoogleApiAvailability.getInstance();
        final int connStatCode = apiAvail.isGooglePlayServicesAvailable(context);

        if (apiAvail.isUserResolvableError(connStatCode)) {
            //TODO : Err case handling
        }
    }

    @AfterPermissionGranted(REQUEST_PERM_GET_ACC)
    private void chooseAcc() {
        //Log.d("LOGHERE", "here1");
        if (EasyPermissions.hasPermissions(context, Manifest.permission.GET_ACCOUNTS)) {
            //Log.d("LOGHERE", "here2");

            String svdAccName = mainAct.getPreferences(context.MODE_PRIVATE).getString(accName, null);
            if (svdAccName == null) {
                //Log.d("LOGHERE", "here5");
                mainAct.startActivityForResult(mCred.newChooseAccountIntent(), REQUEST_ACC_PICK);
                waitUntilPermEnd(2);


            } else {
                mCred.setSelectedAccountName(svdAccName);
                waitUntilPermEnd(2);
            }
        } else {
            //Log.d("LOGHERE", "here3");
            EasyPermissions.requestPermissions(
                    mainAct,
                    "This app needs permission to access your Google Account",
                    REQUEST_PERM_GET_ACC,
                    Manifest.permission.GET_ACCOUNTS);
            waitUntilPermEnd(1);
        }
    }

    public void pickAcc(String accName){
        if (accName != null) {
            SharedPreferences settings = mainAct.getPreferences(context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(this.accName, accName);
            editor.apply();
            mCred.setSelectedAccountName(accName);
            waitUntilPermEnd(2);
        }
    }

    private void waitUntilPermEnd(final int chkMod){
        //Log.d("LOGHERE", "here4");
        new Thread() {
            @Override
            public void run() {
                while((chkMod == 1 && !EasyPermissions.hasPermissions(context, Manifest.permission.GET_ACCOUNTS)) || (chkMod == 2 && mCred.getSelectedAccountName() == null)) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                executeTask();
            }
        }.start();
    }

    public ArrayList<TimetableData> getEvents(){
        return events;
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> requestPermissionList) {

    }


    @Override
    public void onPermissionsDenied(int requestCode, List<String> requestPermissionList) {

    }

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strings, @NonNull int[] ints) {

    }

}