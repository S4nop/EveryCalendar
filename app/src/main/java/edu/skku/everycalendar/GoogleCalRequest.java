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
    static final int REQUEST_ACC_PICK = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
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
        if (!chkGoogleServAvail()) {
            acqGoogleServ();
        } else if (mCred.getSelectedAccountName() == null) {
            chooseAcc();
        }
        googleCalTask.setModeGet(stDate, edDate);
        executeTask();
    }

    private void executeTask(){
        new Thread(){
            @Override
            public void run(){
                try {
                    List<Event> eventList = googleCalTask.execute().get();
                    parseEvents(eventList);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void parseEvents(List<Event> items){
        for (Event event : items) {
            Log.d("LOGHERE", "4");

            DateTime start = event.getStart().getDateTime();

            events.add(new TimetableData(event.getId(), event.getLocation(),
                    event.getDescription(), timeToInt(event.getStart().getDateTime()), timeToInt(event.getEnd().getDateTime())));

            Log.d("LOG_RESLT", String.format("%s \n (%s)", event.getSummary(), start));
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

        if (EasyPermissions.hasPermissions(context, Manifest.permission.GET_ACCOUNTS)) {

            String svdAccName = mainAct.getPreferences(context.MODE_PRIVATE).getString(accName, null);
            if (svdAccName == null) {
                mainAct.startActivityForResult(mCred.newChooseAccountIntent(), REQUEST_ACC_PICK);
            } else {
                mCred.setSelectedAccountName(svdAccName);
            }
        } else {
            EasyPermissions.requestPermissions(
                    mainAct,
                    "This app needs permission to access your Google Account",
                    REQUEST_PERM_GET_ACC,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    public void pickAcc(String accName){
        if (accName != null) {
            SharedPreferences settings = mainAct.getPreferences(context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(this.accName, accName);
            editor.apply();
            mCred.setSelectedAccountName(accName);
        }
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