package edu.skku.everycalendar.everytime;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.ExecutionException;

import edu.skku.everycalendar.functions.Http_Request;

public class MyTimeTableReq extends TimeTableRequest {
    private String tNum;
    public MyTimeTableReq(String cookie){
        this.cookie = cookie;
    }

    @Override
    public void makeTimeTable() {
        finished = false;
        tNum = getTTNum();
        if(tNum == null){
            Log.d("LOG_MakeTimeTable", "getting tNum failed");
            return;
        }
        parseTimeTable(getTimeTable(tNum));
        finished = true;
    }

    private String getTimeTable(String tNum){
        AsyncTimeTableRequest tTR = new AsyncTimeTableRequest();
        tTR.setMod(0);
        try {
            return tTR.execute(tNum).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
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

    public String gettNum() {
        return tNum;
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
                    headers.put("Cookie", "sheet_visible=1; etsid=" + cookie);

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

}
