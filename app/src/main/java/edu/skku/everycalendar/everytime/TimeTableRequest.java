package edu.skku.everycalendar.everytime;

import android.content.ContentValues;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import edu.skku.everycalendar.functions.Http_Request;
import edu.skku.everycalendar.dataType.TimetableData;

public abstract class TimeTableRequest {
    String cookie;
    ArrayList<TimetableData> classList = new ArrayList<>();
    Boolean finished = false;
    //TimeTableRequest(){}

    public abstract void makeTimeTable();

    protected void parseTimeTable(String resv){
        String[] classes = resv.split("subject id");
        String[] dates;
        String name, prof, weekday, place;
        Integer stTime, edTime, color;
        for(int i = 1; i < classes.length; i++){
            name = classes[i].split("name value=\"")[1].split("\"")[0];
            prof = classes[i].split("professor value=\"")[1].split("\"")[0];
            dates = classes[i].split("data day");
            Random rnd = new Random();
            color = Color.rgb(rnd.nextInt(155) + 100, rnd.nextInt(155) + 100, rnd.nextInt(155) + 100);
            for(int j = 1; j < dates.length; j++){
                weekday = Integer.toString(Integer.parseInt(dates[j].split("=\"")[1].split("\"")[0]) + 1);
                place = dates[j].split("place=\"")[1].split("\"")[0];
                //Time = (Hour * 60 + Min) / 5
                stTime = Integer.parseInt(dates[j].split("starttime=\"")[1].split("\"")[0]);
                edTime = Integer.parseInt(dates[j].split("endtime=\"")[1].split("\"")[0]);
                classList.add(new TimetableData(name, place, prof, weekday, stTime, edTime, color));
                Log.d("LOG_PRSTT", name + " " + place + " " + prof + " " + weekday);
            }
        }
    }

    protected class AsyncTimeTableRequest extends AsyncTask<String, Void, String>{

        private int mod = 0; // 0 : My timetable, 1 : Friend's timetable

        private Exception exception;
        private String rslt;
        protected String doInBackground(String... urls) {
            try {
                Http_Request hr = new Http_Request();
                if(hr.request("GET", "https://everytime.kr/", null, null) == 0){

                    ContentValues queries = new ContentValues();
                    ContentValues headers = new ContentValues();
                    setHeaders(headers);

                    if(mod == 0)
                        queries.put("id", urls[0]);
                    else{
                        queries.put("identifier", urls[0]);
                        queries.put("friendInfo", "true");
                    }
                    if(hr.request("POST", "https://everytime.kr/find/timetable/table" + (mod == 1 ? "/friend" : ""), queries, headers) == 0){
                        rslt = hr.getResult();
                        Log.d("LOG_HTTP", rslt);
                        return rslt;
                    }
                    return null;
                }
            } catch (Exception e) {
                this.exception = e;

                return null;
            } finally {
                return rslt;
            }
        }

        public void setMod(int mod){
            this.mod = mod;
        }

        private void setHeaders(ContentValues headers){
            headers.put("Host", "everytime.kr");
            headers.put("Referer", "http://everytime.kr/");
            headers.put("Origin", "http://everytime.kr");
            headers.put("Upgrade-Insecure-Requests", "1");
            headers.put("Cookie", "sheet_visible=1; etsid=" + cookie);
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    };

    public ArrayList<TimetableData> getClassList() {
        return classList;
    }

    public boolean getFinished(){
        return finished;
    }
}
