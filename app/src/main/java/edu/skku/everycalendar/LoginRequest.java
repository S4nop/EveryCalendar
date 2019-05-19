package edu.skku.everycalendar;

import android.content.ContentValues;

public class LoginRequest{

    private String cookie = null;
    private boolean logined = false;
    private boolean finished = false;
    String uid, pwd;

    public LoginRequest(String id, String pw){
        uid = id; pwd = pw;
        JWRequest();
        //logined = true;
    }

    private void JWRequest(){
        final int SEND_END = 0;
        final int SEND_ERR = -1;

        final Http_Request hr = new Http_Request();

        new Thread(){
            public void run(){
                if(hr.request("GET", "https://everytime.kr/login", null, null) == 0){

                    ContentValues queries = new ContentValues();
                    ContentValues headers = new ContentValues();
                    headers.put("Host", "everytime.kr");
                    headers.put("Referer", "http://everytime.kr/");
                    headers.put("Origin", "http://everytime.kr");
                    headers.put("Upgrade-Insecure-Requests", "1");
                    queries.put("userid", uid);
                    queries.put("password", pwd);
                    queries.put("redirect", "/");
                    if(hr.request("POST", "https://everytime.kr/user/login", queries, headers) == 0){
                        getUserInfo(hr);
                    }
                }
            }
        }.start();

    }

    private void getUserInfo(Http_Request hr){
        try{
            cookie = hr.getHeaders().get("Set-Cookie").toString();
            logined = true;
        }catch(Exception e){
            logined = false;
        }
        finished = true;
    }

    public String getCookie() {return cookie;}
    public boolean getLogined() {return logined;}
    public boolean getFinished() {return finished;}
}
