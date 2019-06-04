package edu.skku.everycalendar;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class FriendsListRequest {

    String cookie;
    HashMap<String, String> friendList;
    ArrayList<TimetableData> fClassList = new ArrayList<>();
    boolean finished = false;

    FriendsListRequest(String cookie){
        this.cookie = cookie;
    }

    public void makeFriendList(){
            friendList = new HashMap<>();
            FriendListRequest flr = new FriendListRequest();
            try {
                String resp[] = flr.execute().get().split("friend");

                for(int i = 1; i < resp.length; i++){
                    addFriendToMap(resp[i]);
                    Log.d("LOG_FRIEND", resp[i]);
                }

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    public Map<String, String> getFriendList(){
        return friendList;
    }

    private void addFriendToMap(String line){
        String name, key;
        name = line.split("name=\"")[1].split("\"")[0];
        key = line.split("userid=\"")[1].split("\"")[0];
        friendList.put(name, key);
    }

    private class FriendListRequest extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String rslt;
        protected String doInBackground(String... urls) {
            try {
                Http_Request hr = new Http_Request();
                if(hr.request("GET", "https://everytime.kr/", null, null) == 0){

                    ContentValues headers = new ContentValues();
                    headers.put("Host", "everytime.kr");
                    headers.put("Referer", "http://everytime.kr/");
                    headers.put("Origin", "http://everytime.kr");
                    headers.put("Upgrade-Insecure-Requests", "1");
                    headers.put("Cookie", "sheet_visible=1; etsid=" + cookie);

                    if(hr.request("POST", "https://everytime.kr/find/friend/list", null, headers) == 0){
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

}
