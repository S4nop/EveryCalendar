package edu.skku.everycalendar;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleCalTask extends AsyncTask<Void, Void, List<Event>> {
    private Integer mod;
    private DateTime stDate, edDate;
    private com.google.api.services.calendar.Calendar mServ;


    public GoogleCalTask(GoogleAccountCredential credential) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFtry = JacksonFactory.getDefaultInstance();

        mServ = new com.google.api.services.calendar.Calendar
                .Builder(transport, jsonFtry, credential)
                .setApplicationName("EveryCalendar")
                .build();
    }


    @Override
    protected List<Event> doInBackground(Void... params) {
        try {
            if ( mod == 1) {
                return getEvent();
            }
            else if (mod == 2) {
                //return addEvent();
            }
        } catch (Exception e) {
            cancel(true);
            return null;
        }

        return null;
    }
    @Override
    protected void onCancelled() {

    }

    private List<Event> getEvent() throws IOException {

        Log.d("LOGHERE", edDate.toString());
        Events events = mServ.events().list("primary")//"primary")
                .setTimeMin(stDate)
                .setTimeMax(edDate)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        Log.d("LOGHERE", Integer.toString(items.size()));

        return items;
    }

    public void setModeGet(DateTime stDate, DateTime edDate){
        mod = 1;
        this.stDate = stDate;
        this.edDate = edDate;
    }

    public void setModeAdd(){ mod = 2; }

}
