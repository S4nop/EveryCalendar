package edu.skku.everycalendar.joinSchedule;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import edu.skku.everycalendar.activities.AdjustFragment;
import edu.skku.everycalendar.activities.AdjustResultActivity;
import edu.skku.everycalendar.dataType.TimetableData;
import edu.skku.everycalendar.functions.Utilities;

public class JoinSchedule {
    Integer stTime, edTime;
    ArrayList<TimetableData>[] wEvents = new ArrayList[7];
    ArrayList<TimetableData> rslt;
    boolean[][] ableTime = new boolean[7][24];
    int fNum = 0, dbNum = 0;
    boolean finished = false;
    String id;

    public JoinSchedule(Integer stTime, Integer edTime) {
        this.stTime = stTime;
        this.edTime = edTime;
        finished = false;
        dbNum = 0;
        Utilities.makeToast("친구들에게 시간표 조율 요청을 전송했습니다.\n친구들의 확인이 완료되면 작업이 시작됩니다");
        for(int i = 0; i < 7; i++)
            for(int j = 0; j < 24; j++)
                ableTime[i][j] = true;

        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < stTime; j++)
                ableTime[i][j] = false;
            for(int j = edTime + 1; j < 24; j++)
                ableTime[i][j] = false;

            wEvents[i] = new ArrayList<>();
        }
    }

    public boolean getFinished(){ return finished; }

    public Integer getStTime() {
        return stTime;
    }

    public Integer getEdTime() {
        return edTime;
    }

    public ArrayList<TimetableData> getRslt() {
        return rslt;
    }

    public void makeTableView(){
        rslt = getAbleTime();
        finished = true;
        //TODO : Show timetable
    }

    public void addEvent(TimetableData event, int week){
            wEvents[week].add(event);
    }
    public void addEvents(ArrayList<TimetableData> event){
        for(TimetableData td : event){
            //Log.d("LOG4", "LOOP");
            wEvents[Integer.parseInt(td.getWeekDay())].add(td);
            //Log.d("LOG_ADDEVENTS", td.getName());
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
            //Log.d("LOG5", td.getName());

            st = td.getStartTime() / 12;
            ed = td.getEndTime() / 12 + (td.getEndTime() % 12 == 0 ? 0 : 1);
            for(int i = st; i < ed; i++){
                ableTime[idx][i] = false;
            }
        }
    }

    private ArrayList<TimetableData> makeTimeTableList(){
        ArrayList<TimetableData> out = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 24; j++){
                ////Log.d("LOG_CHKABLETIME", "Abletime " + i + " " + j + " = " + ableTime[i][j]);
                if(ableTime[i][j]) out.add(new TimetableData("","","",Integer.toString(i),j * 12,
                        (j+1)*12, "", Color.parseColor("#FF9B9B")));
            }
        }
        return out;
    }

}
