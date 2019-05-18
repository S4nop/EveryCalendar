package edu.skku.everycalendar;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoogleCalRequest {
    Context context;
    GoogleAccountCredential mCredential;
    Calendar mServ;
    ArrayList<TimetableData> todos;
    private static final String[] SCOPES = { CalendarScopes.CALENDAR_READONLY };

    public GoogleCalRequest(Context context) {
        this.context = context;
        mServ = new com.google.api.services.calendar.Calendar
                .Builder(AndroidHttp.newCompatibleTransport(), JacksonFactory.getDefaultInstance(), mCredential)
                .setApplicationName("EveryCalendar")
                .build();
        mCredential.usingOAuth2(context, Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
        todos = new ArrayList<>();
    }

    public void getCalendarSchedule(DateTime stDate, DateTime edDate) {

        if (!chkGoogleSvc()) {
            reqGoogleSvc();
        } else {
            // Google Calendar API 호출
            GetCalendarData gcd = new GetCalendarData(stDate, edDate);
            gcd.execute();
        }
    }
    private boolean chkGoogleSvc() {

        GoogleApiAvailability apiAvail = GoogleApiAvailability.getInstance();
        int sCode = apiAvail.isGooglePlayServicesAvailable(context);

        return sCode == ConnectionResult.SUCCESS;
    }

    private void reqGoogleSvc() {

        GoogleApiAvailability apiAvail = GoogleApiAvailability.getInstance();
        int sCode = apiAvail.isGooglePlayServicesAvailable(context);

        if (apiAvail.isUserResolvableError(sCode)) {
            //TODO : Error case handling
        }
    }
    private class GetCalendarData extends AsyncTask<Void, Void, String> {
        DateTime stDate, edDate;

        public GetCalendarData(DateTime stDate,DateTime edDate) {
            super();
            this.stDate = stDate;
            this.edDate = edDate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                getEvent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String getEvent() throws IOException {
            String name, place, weekDay;
            Integer startTime, endTime;

            Events events = mServ.events().list("primary")//"primary")
                    //.setTimeMin(stDate)
                    //.setTimeMax(edDate)
                    .setMaxResults(1)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            List<Event> items = events.getItems();


            for (Event event : items) {
                //DateTime start = event.getStart().getDateTime();
                name = event.getId();
                place = event.getLocation();
                Log.d("LOG_GOOAPI", name  + " " + place);
                //eventStrings.add(String.format("%s \n (%s)", event.getSummary(), start));
            }

            return "0";
            //return eventStrings.size() + "개의 데이터를 가져왔습니다.";
        }
    }


}
