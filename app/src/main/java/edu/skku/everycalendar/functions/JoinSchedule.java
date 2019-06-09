package edu.skku.everycalendar.functions;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import edu.skku.everycalendar.dataType.TimetableData;

public class JoinSchedule {
    Integer stTime, edTime;
    ArrayList<TimetableData>[] wEvents = new ArrayList[7];
    boolean[][] ableTime = new boolean[7][24];
    public void test(){
        wEvents[1].add(new TimetableData("test1", null, null , "1", 144, 180, 1));
        wEvents[1].add(new TimetableData("test1", null, null , "1", 132, 156, 1));
        wEvents[1].add(new TimetableData("test1", null, null , "1", 108, 120, 1));
        wEvents[2].add(new TimetableData("test1", null, null , "2", 108, 240, 1));
        wEvents[3].add(new TimetableData("test1", null, null , "3", 108, 240, 1));
        wEvents[4].add(new TimetableData("test1", null, null , "4", 108, 240, 1));
        wEvents[5].add(new TimetableData("test1", null, null , "5", 108, 240, 1));
        wEvents[6].add(new TimetableData("test1", null, null , "6", 108, 240, 1));

        ArrayList<TimetableData> testres = getAbleTime();
        int i = 0;
        for(TimetableData td : testres){
            Log.d("LOG_TEST", "" + td.getStartTime() + " ~ " + td.getEndTime() + " : " + td.getWeekDay() + " && " + i++);
        }
    }

    public JoinSchedule(Integer stTime, Integer edTime) {
        this.stTime = stTime;
        this.edTime = edTime;


        for(int i = 0; i < 7; i++)
            for(int j = 0; j < 24; j++)
                ableTime[i][j] = true;

        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < stTime; j++)
                ableTime[i][j] = false;
            for(int j = edTime; j < 24; j++)
                ableTime[i][j] = false;

            wEvents[i] = new ArrayList<>();
        }
    }

    public void addEvents(ArrayList<TimetableData> events){
        for(TimetableData td : events){
            wEvents[Integer.parseInt(td.getWeekDay())].add(td);
        }
    }


    public ArrayList<TimetableData> getAbleTime(){
        ArrayList<TimetableData> rslt = new ArrayList<>();

        for(int i = 0; i < 7; i++){
            findTime(i);
        }

        return makeTimeTableList();
    }

    private void findTime(int idx){
        Integer st, ed;

        for(TimetableData td : wEvents[idx]){
            st = td.getStartTime() / 12;
            ed = td.getEndTime() / 12 + (td.getEndTime() % 12 == 0 ? 0 : 1);
            for(int i = st; i < ed; i++){
                ableTime[idx][i] = false;
            }
        }
    }

    private ArrayList<TimetableData> makeTimeTableList(){
        ArrayList<TimetableData> out = new ArrayList<>();
        Random rnd = new Random();
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 24; j++){
                Log.d("LOG_CHKABLETIME", "Abletime " + i + " " + j + " = " + ableTime[i][j]);
                if(ableTime[i][j]) out.add(new TimetableData("","","",Integer.toString(i),j,
        j+1, Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255))));
            }
        }
        return out;
    }
}
