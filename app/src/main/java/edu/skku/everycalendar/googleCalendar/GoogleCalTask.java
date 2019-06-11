package edu.skku.everycalendar.googleCalendar;

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
import java.util.List;

public class GoogleCalTask extends AsyncTask<Void, Void, List<Event>> {
    private Integer mod;
    private DateTime stDate, edDate;
    private String add_Summary, add_Location, add_Description;
    private com.google.api.services.calendar.Calendar mServ;
    private boolean addResult = false;

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
                addEvent();
            }
        } catch (Exception e) {
            //Log.d("LOG_DOINBACK", e.toString());
            cancel(true);
            return null;
        }

        return null;
    }
    @Override
    protected void onCancelled() {

    }

    private List<Event> getEvent() throws IOException {

        Events events = mServ.events().list("primary")//"primary")
                .setTimeMin(stDate)
                .setTimeMax(edDate)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        return items;
    }

    private void addEvent() {
        Event event = new Event()
                .setSummary(add_Summary)
                .setLocation(add_Location)
                .setDescription(add_Description);

        EventDateTime start = new EventDateTime()
                .setDateTime(stDate)
                .setTimeZone("Asia/Seoul");
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(edDate)
                .setTimeZone("Asia/Seoul");
        event.setEnd(end);

        try {
            event = mServ.events().insert("primary", event).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        addResult = event.getHtmlLink() != null;
    }

    public void setModeGet(DateTime stDate, DateTime edDate){
        mod = 1;
        this.stDate = stDate;
        this.edDate = edDate;
    }

    public void setModeAdd(String add_Sum, String add_Loc, String add_Desc, DateTime stDate, DateTime edDate){
        mod = 2;
        add_Summary = add_Sum;
        add_Location = add_Loc;
        add_Description = add_Desc;
        this.stDate = stDate;
        this.edDate = edDate;
        addResult = false;
    }

    public boolean getAddResult(){
        return addResult;
    }
}
