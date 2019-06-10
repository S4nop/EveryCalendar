package edu.skku.everycalendar.everytime;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.ExecutionException;

import edu.skku.everycalendar.functions.Http_Request;

public class AddFriendRequest {
    String cookie;

    public AddFriendRequest(String cookie){
        this.cookie = cookie;
    }

    public String addFriend(String fid){
        AsyncAddFriendRequest afr = new AsyncAddFriendRequest();
        try {
            String result = afr.execute(fid).get();
            Log.d("LOG_ADDFRIEND", result);
            return result.split("<response>")[1].split("</response>")[0].equals("-1") ? "친구 요청을 실패했습니다" : "친구 요청을 전송했습니다";
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
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
