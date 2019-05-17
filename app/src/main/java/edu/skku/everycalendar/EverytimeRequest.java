package edu.skku.everycalendar;

import android.content.ContentValues;
import android.util.Log;

import java.util.ArrayList;

public class EverytimeRequest {

    EverytimeRequest(){
        final HttpRequest hr = new HttpRequest();

        new Thread(){
            public void run(){
                if(hr.request("GET", "https://everytime.kr/", null, null) == 0){

                    ContentValues queries = new ContentValues();
                    ContentValues headers = new ContentValues();
                    headers.put("Host", "everytime.kr");
                    headers.put("Referer", "http://everytime.kr/");
                    headers.put("Origin", "http://everytime.kr");
                    headers.put("Upgrade-Insecure-Requests", "1");
                    headers.put("Cookie", "s%3A599P1GVDOCDvQjCnErFswpIG_qf-EYsv.sUBCLpV4o2GM7BcOJC6rUoCeyyv2wrvX8VrT51fCiAw");
                    queries.put("year", "2019");
                    queries.put("semester", "1");
                    if(hr.request("POST", "https://everytime.kr/find/timetable/table/list/semester", queries, headers) == 0){
                        Log.d("Log_ETresult", hr.getResult());
                    }
                }
            }
        }.start();
    }
}

//    String cookie;
//    ArrayList<ETtimetable> classList;
//
//    EverytimeRequest(String cookie){
//        this.cookie = cookie;
//    }
//
//    public void getTimeTable(){
//        final String id = reqSemester();
//        final HttpRequest hr = new HttpRequest();
//        new Thread(){
//            @Override
//            public void run() {
//                ContentValues queries = new ContentValues();
//                ContentValues headers = new ContentValues();
//                headers.put("Host", "everytime.kr");
//                headers.put("Referer", "http://everytime.kr/");
//                headers.put("Origin", "http://everytime.kr");
//                headers.put("Cookie", "s%3A599P1GVDOCDvQjCnErFswpIG_qf-EYsv.sUBCLpV4o2GM7BcOJC6rUoCeyyv2wrvX8VrT51fCiAw\\t");
//
//                queries.put("id", id);
//
//                if(hr.request("POST", "https://everytime.kr/find/timetable/table", queries, headers) == 0){
//                    Log.d("Response", hr.getResult().toString());
//                }
//            }
//        }.run();
//
//    }
//
//    private String reqSemester(){
//        GetTimeTableID gti = new GetTimeTableID();
//        Thread gThrd = new Thread(gti);
//        try {
//            gThrd.run();
//            gThrd.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return gti.getTbID();
//    }
//
//    private String getTableID(String html){
//
//        return null;
//    }
//
//    private class GetTimeTableID implements Runnable {
//        private volatile String id;
//
//        @Override
//        public void run() {
//            HttpRequest hr = new HttpRequest();
//            ContentValues queries = new ContentValues();
//            ContentValues headers = new ContentValues();
//            headers.put("Host", "everytime.kr");
//            headers.put("Referer", "http://everytime.kr/");
//            headers.put("Origin", "http://everytime.kr");
//            headers.put("Cookie", "s%3A599P1GVDOCDvQjCnErFswpIG_qf-EYsv.sUBCLpV4o2GM7BcOJC6rUoCeyyv2wrvX8VrT51fCiAw\\t");
//
//            //TODO : Need to be dynamic!
//            queries.put("year", "2019");
//            queries.put("semester", "1");
//
//            if(hr.request("POST", "https://everytime.kr/find/timetable/table/list/semester", queries, headers) == 0){
//                id = getTableID(hr.getResult());
//            }
//        }
//
//        public String getTbID(){
//            return id;
//        }
//    }
//
//    public String getCookie() {
//        return cookie;
//    }
//
//    public void setCookie(String cookie) {
//        this.cookie = cookie;
//    }
//}
