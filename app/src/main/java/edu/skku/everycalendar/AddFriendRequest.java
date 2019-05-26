package edu.skku.everycalendar;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class AddFriendRequest {
    String cookie;

    AddFriendRequest(String cookie){
        this.cookie = cookie;
    }

    public void addFriend(String fid){
        AsyncAddFriendRequest afr = new AsyncAddFriendRequest();
        try {
            String result = afr.execute(fid).get();
            Log.d("LOG_ADDFRIEND", result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private class AsyncAddFriendRequest extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String rslt;
        protected String doInBackground(String... urls) {
            try {
                Http_Request hr = new Http_Request();
                if(hr.request("GET", "https://everytime.kr/", null, null) == 0){

                    ContentValues headers = new ContentValues();
                    ContentValues queries = new ContentValues();

                    queries.put("data", urls[0]);
                    setHeaders(headers);
                    if(hr.request("POST", "https://everytime.kr/save/friend/request", queries, headers) == 0){
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
    }
}
