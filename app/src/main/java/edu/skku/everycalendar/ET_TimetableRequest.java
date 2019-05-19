package edu.skku.everycalendar;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ET_TimetableRequest {
    String cookie;
    ArrayList<TimetableData> classList = new ArrayList<>();

    ET_TimetableRequest(String cookie){
        this.cookie = cookie;
    }


    public void makeTimeTable(){
        String tNum = getTTNum();
        if(tNum == null){
            Log.d("LOG_MakeTimeTable", "getting tNum failed");
            return;
        }
        parseTimeTable(getTimeTable(tNum));
    }

    private String getTTNum(){
        TNumRequest gTT = new TNumRequest();

        try {
            return gTT.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String parseTTNum(String resv){
        return resv.split("id=\"")[1].split("\"")[0];
    }

    private String getTimeTable(String tNum){
        TimeTableRequest tTR = new TimeTableRequest();

        try {
            return tTR.execute(tNum).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseTimeTable(String resv){
        String[] classes = resv.split("subject id");
        String[] dates;
        String name, prof, weekday, place;
        Integer stTime, edTime;
        for(int i = 1; i < classes.length; i++){
            name = classes[i].split("name value=\"")[1].split("\"")[0];
            prof = classes[i].split("professor value=\"")[1].split("\"")[0];
            dates = classes[i].split("data day");
            for(int j = 1; j < dates.length; j++){
                weekday = dates[j].split("=\"")[1].split("\"")[0];
                place = dates[j].split("place=\"")[1].split("\"")[0];
                //Time = (Hour * 60 + Min) / 5
                stTime = Integer.parseInt(dates[j].split("starttime=\"")[1].split("\"")[0]);
                edTime = Integer.parseInt(dates[j].split("endtime=\"")[1].split("\"")[0]);
                classList.add(new TimetableData(name, place, prof, weekday, stTime, edTime));
                Log.d("LOG_PRSTT", name + " " + place + " " + prof + " " + weekday);
            }
        }
    }

    class TNumRequest extends AsyncTask<Void, Void, String> {

        private Exception exception;
        private String rslt;

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Http_Request hr = new Http_Request();
                if(hr.request("GET", "https://everytime.kr/", null, null) == 0){

                    ContentValues queries = new ContentValues();
                    ContentValues headers = new ContentValues();
                    headers.put("Host", "everytime.kr");
                    headers.put("Referer", "http://everytime.kr/");
                    headers.put("Origin", "http://everytime.kr");
                    headers.put("Upgrade-Insecure-Requests", "1");
                    headers.put("Cookie", "sheet_visible=1; etsid=s%3A665adlx48aBt4GSTVvBmF4VBHBtZceoC.l5muTrEoFUF6fdIlM9vzq6Kl9v1T6oXBpPTkoRGcS8o");

                    queries.put("year", "2019");
                    queries.put("semester", "1");
                    if(hr.request("POST", "https://everytime.kr/find/timetable/table/list/semester", queries, headers) == 0){
                        return rslt = parseTTNum(hr.getResult());
                    }
                    //Case failure
                    return null;
                }
            } catch (Exception e) {
                this.exception = e;

                return null;
            } finally {
                return rslt;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

    class TimeTableRequest extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String rslt;
        protected String doInBackground(String... urls) {
            try {
                Http_Request hr = new Http_Request();
                if(hr.request("GET", "https://everytime.kr/", null, null) == 0){

                    ContentValues queries = new ContentValues();
                    ContentValues headers = new ContentValues();
                    headers.put("Host", "everytime.kr");
                    headers.put("Referer", "http://everytime.kr/");
                    headers.put("Origin", "http://everytime.kr");
                    headers.put("Upgrade-Insecure-Requests", "1");
                    headers.put("Cookie", "sheet_visible=1; etsid=s%3A665adlx48aBt4GSTVvBmF4VBHBtZceoC.l5muTrEoFUF6fdIlM9vzq6Kl9v1T6oXBpPTkoRGcS8o");

                    queries.put("id", urls[0]);
                    if(hr.request("POST", "https://everytime.kr/find/timetable/table", queries, headers) == 0){
                        rslt = hr.getResult();
                        Log.d("LOG_HTTP", rslt);
                        return rslt;
                    }
                    //Case failure
                    return null;
                }
            } catch (Exception e) {
                this.exception = e;

                return null;
            } finally {
                return rslt;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

    public ArrayList<TimetableData> getClassList() {
        return classList;
    }
}
