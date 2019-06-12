package edu.skku.everycalendar.everytime;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.ExecutionException;

import edu.skku.everycalendar.functions.Http_Request;

public class GetNameRequest {
    String cookie;

    public GetNameRequest(String cookie){
        this.cookie = cookie;
    }

    public String getName(){
        NameRequest flr = new NameRequest();
        try {
            String resp = flr.execute().get();
            return resp.split("<p>")[4].split("<em>")[1].split("</em>")[0] + "::" + resp.split("<p>")[7].split("<em>")[1].split("</em>")[0];
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class NameRequest extends AsyncTask<Void, Void, String> {

        private String rslt;
        protected String doInBackground(Void ...urls) {
            try {
                Http_Request hr = new Http_Request();
                if(hr.request("GET", "https://everytime.kr/", null, null) == 0){

                    ContentValues headers = new ContentValues();
                    headers.put("Host", "everytime.kr");
                    headers.put("Referer", "http://everytime.kr/");
                    headers.put("Origin", "http://everytime.kr");
                    headers.put("Upgrade-Insecure-Requests", "1");
                    headers.put("Cookie", "etsid=" + cookie);

                    if(hr.request("POST", "https://everytime.kr/my", null, headers) == 0){
                        rslt = hr.getResult();
                        //Log.d("LOG_HTTP", rslt);
                        return rslt;
                    }
                    //Case failure
                    return null;
                }
            } catch (Exception e) {
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
