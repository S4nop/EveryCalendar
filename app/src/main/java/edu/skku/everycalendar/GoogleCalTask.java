package edu.skku.everycalendar;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GoogleCalTask extends AsyncTask<Void, Void, List<TimetableData>> {
    private Integer mod;
    private DateTime stDate, edDate;
    private com.google.api.services.calendar.Calendar mService;


    public GoogleCalTask(GoogleAccountCredential credential) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        mService = new com.google.api.services.calendar.Calendar
                .Builder(transport, jsonFactory, credential)
                .setApplicationName("EveryCalendar")
                .build();
    }


    @Override
    protected List<TimetableData> doInBackground(Void... params) {
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

    private List<TimetableData> getEvent() throws IOException {
        DateTime now = new DateTime(System.currentTimeMillis());
        Log.d("LOGHERE", "6");
        List<TimetableData> eventList = new ArrayList<>();
        Log.d("LOGHERE", "5");
        Events events = mService.events().list("primary")//"primary")
                .setMaxResults(10)
                .setTimeMin(stDate)
                .setTimeMax(edDate)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        Log.d("LOGHERE", "3");
        List<Event> items = events.getItems();


        for (Event event : items) {
            Log.d("LOGHERE", "4");

            DateTime start = event.getStart().getDateTime();


            eventList.add(new TimetableData(event.getId(), event.getLocation(),
                    event.getDescription(), timeToInt(event.getStart().getDateTime()), timeToInt(event.getEnd().getDateTime())));

            Log.d("LOG_RESLT", String.format("%s \n (%s)", event.getSummary(), start));
            return eventList;
        }

        return null;
    }

    public void setModeGet(DateTime stDate, DateTime edDate){
        mod = 1;
        this.stDate = stDate;
        this.edDate = edDate;
    }

    public void setModeAdd(){ mod = 2; }

    private Integer timeToInt(DateTime dt){
        String strDT = dt.toString();
        return ((Integer.parseInt(strDT.substring(11, 12)) * 60 + Integer.parseInt(strDT.substring(14,15))) / 5);
    }
}
