package edu.skku.everycalendar;
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
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class GoogleCalRequest implements EasyPermissions.PermissionCallbacks {

    Context context;
    Activity mainAct;
    GoogleAccountCredential mCred;
    ArrayList<TimetableData> events;
    GoogleCalTask googleCalTask;
    int gThreadRunning = 0;
    static final int REQUEST_ACC_PICK = 1000;
    static final int REQUEST_PERM_GET_ACC = 1003;


    String accName = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR};

    public GoogleCalRequest(Context context, Activity mainAct, String accName) {
        this.context = context;
        this.mainAct = mainAct;
        this.accName = accName;
        events = new ArrayList<>();

        mCred = GoogleAccountCredential.usingOAuth2(
                context,
                Arrays.asList(SCOPES)
        ).setBackOff(new ExponentialBackOff());
        googleCalTask = new GoogleCalTask(mCred);
    }

    public void getCalendarData(DateTime stDate, DateTime edDate) {
        googleCalTask.setModeGet(stDate, edDate);
        executeTask();
    }

    public void addEventToCalendar(String add_Sum, String add_Loc, String add_Desc, DateTime stDate, DateTime edDate){
        if (!chkGoogleServAvail()) {
            acqGoogleServ();
        } else if (mCred.getSelectedAccountName() == null) {
            chooseAcc();
        }
        googleCalTask.setModeAdd(add_Sum, add_Loc, add_Desc, stDate, edDate);
        executeTask();
    }

    private void executeTask(){
        Log.d("LOG_EXECUTE", "herere");
        if (!chkGoogleServAvail()) {
            acqGoogleServ();
        } else if (mCred.getSelectedAccountName() == null) {
            chooseAcc();
        }
        else{
            Log.d("LOG_EXECUTE", "ThrdRun");
            new Thread(){
                @Override
                public void run(){
                    List<Event> eventList = null;
                    try {
                        if(gThreadRunning == 0){
                            gThreadRunning = 1;
                            eventList = googleCalTask.execute().get();
                            parseEvents(eventList);
                            gThreadRunning = 0;
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }
    }

    private void parseEvents(List<Event> items){
        for (Event event : items) {

            events.add(new TimetableData(event.getSummary(), event.getLocation(),
                    event.getDescription(), timeToInt(event.getStart().getDateTime()), timeToInt(event.getEnd().getDateTime())));

            //TODO : Need to handle case - Events' start date and end date are different
            //TODO : Need to get weekday form events date

            Log.d("LOG_RESLT", String.format("%s \n (%s)", event.getSummary(), event.getId()));
        }
    }

    private Integer timeToInt(DateTime dt){
        String strDT = dt.toString();
        return ((Integer.parseInt(strDT.substring(11, 12)) * 60 + Integer.parseInt(strDT.substring(14,15))) / 5);
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
        Log.d("LOGHERE", "here1");
        if (EasyPermissions.hasPermissions(context, Manifest.permission.GET_ACCOUNTS)) {
            Log.d("LOGHERE", "here2");

            String svdAccName = mainAct.getPreferences(context.MODE_PRIVATE).getString(accName, null);
            if (svdAccName == null) {
                Log.d("LOGHERE", "here5");
                mainAct.startActivityForResult(mCred.newChooseAccountIntent(), REQUEST_ACC_PICK);
                waitUntilPermEnd(2);


            } else {
                mCred.setSelectedAccountName(svdAccName);
                waitUntilPermEnd(2);
            }
        } else {
            Log.d("LOGHERE", "here3");
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

    private void waitUntilPermEnd(final int mod){
        Log.d("LOGHERE", "here4");
        new Thread() {
            @Override
            public void run() {
                while((mod == 1 && !EasyPermissions.hasPermissions(context, Manifest.permission.GET_ACCOUNTS)) || (mod == 2 && mCred.getSelectedAccountName() == null)) {
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