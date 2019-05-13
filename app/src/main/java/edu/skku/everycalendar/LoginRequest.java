package edu.skku.everycalendar;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class LoginRequest{

    private String cookie = null;
    private boolean logined = false;
    private boolean finished = false;
    String uid, pwd;

    public LoginRequest(String id, String pw){
        uid = id; pwd = pw;
        Log.d("Log", "LoginRequest");
        JWRequest();
        //logined = true;
    }

    private void JWRequest(){
        final int SEND_END = 0;
        final int SEND_ERR = -1;

        final HttpRequest hr = new HttpRequest();

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
                        Log.d("Log_result", hr.getResult());

                    }
                }
            }
        }.start();

    }

    private void getUserInfo(HttpRequest hr){
        Log.d("log_rslt", hr.getResult());
        Log.d("Log_Header", hr.getHeaders().toString());
        try{
            Log.d("Log_Cookie", hr.getHeaders().get("Set-Cookie").toString());
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
