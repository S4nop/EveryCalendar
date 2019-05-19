package edu.skku.everycalendar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.services.calendar.CalendarScopes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;



public class GoogleCalRequest implements EasyPermissions.PermissionCallbacks {

    GoogleAccountCredential mCred;
    Context context;
    Activity mainAct;
    String accName;
    int chkLogin = 0;
    List<TimetableData> events;

    GoogleCalTask mrt;
    public static final int REQUEST_ACC_PICK = 1000;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String[] PERMS = {CalendarScopes.CALENDAR};

    public GoogleCalRequest(Context context, Activity mainAct, String accName) {
        this.context = context;
        this.mainAct = mainAct;
        this.accName = accName;

        mCred = GoogleAccountCredential.usingOAuth2(
                context,
                Arrays.asList(PERMS)
        ).setBackOff(new ExponentialBackOff());

    }


    public void getCalendarData(DateTime stDate, DateTime edDate) {
        mrt = new GoogleCalTask(mCred);
        mrt.setModeGet(stDate, edDate);
        if (!chkGoogleAvail()) {
            reqGoogleSrv();
        }
        if (mCred.getSelectedAccountName() == null) {
            chooseAcc();
        }
        else{
            executeTasker();
        }
    }

    private void executeTasker(){
        try {
            events = mrt.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (CancellationException e) {
            e.printStackTrace();
        }
    }

    private boolean chkGoogleAvail() {

        GoogleApiAvailability apiAvail = GoogleApiAvailability.getInstance();

        return apiAvail.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS;
    }


    private void reqGoogleSrv() {
        GoogleApiAvailability apiAvail = GoogleApiAvailability.getInstance();
        int conStatCode = apiAvail.isGooglePlayServicesAvailable(context);

        if (apiAvail.isUserResolvableError(conStatCode)) {
            //TODO : Error handling
        }
    }



    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAcc() {

        if (EasyPermissions.hasPermissions(context, Manifest.permission.GET_ACCOUNTS)) {

            String accName = mainAct.getPreferences(context.MODE_PRIVATE)
                    .getString(this.accName, null);
            if (accName != null) {
                mCred.setSelectedAccountName(accName);
                executeTasker();
            } else {

                mainAct.startActivityForResult(
                        mCred.newChooseAccountIntent(),
                        REQUEST_ACC_PICK);
                executeTasker();
            }
        } else {

            EasyPermissions.requestPermissions(
                    mainAct,
                    "This app needs permission to access your Google Account",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
            executeTasker();
        }
        chkLogin = 1;
    }

    public void pickAcc(String accName){
        if (accName != null) {
            SharedPreferences settings = mainAct.getPreferences(context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(this.accName, accName);
            editor.apply();
            mCred.setSelectedAccountName(accName);
            executeTasker();
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
