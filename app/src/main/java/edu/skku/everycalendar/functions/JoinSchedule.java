package edu.skku.everycalendar.functions;

import android.graphics.Color;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

import edu.skku.everycalendar.dataType.TimetableData;

public class JoinSchedule {
    Integer stTime, edTime;
    ArrayList<TimetableData>[] wEvents = new ArrayList[7];
    ArrayList<String> uploadedFriend;
    boolean[][] ableTime = new boolean[7][24];
    int fNum = 0;

    String id;

    public JoinSchedule(String stDate, String edDate, Integer stTime, Integer edTime, ArrayList<String> friends) {
        this.stTime = stTime;
        this.edTime = edTime;
        fNum = friends.size();
        JoinSchedulReq jsr = new JoinSchedulReq();
        jsr.joinRequest(stDate, edDate, friends);
        uploadedFriend = new ArrayList<>();
        Utilities.makeToast("친구들에게 시간표 조율 요청을 전송했습니다.\n친구들의 확인이 완료되면 작업이 시작됩니다");
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

        id = jsr.getID();
        chkRequest();
    }

    public void chkRequest(){
        Log.d("LOG_SERV", "chkRequest called");
        RealTimeDBPull.getDatatListFromDB(FirebaseDatabase.getInstance().getReference().child("SchedJoin").child(id),
                new CallArgFuncE(), null, true);
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
        j+1, "", Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255))));
            }
        }
        return out;
    }

    class CallArgFuncE extends CallableArg<String> {
        @Override
        public Void call() {
            try{
                if(!uploadedFriend.contains(arg)){
                    uploadedFriend.add(arg);
                    if(uploadedFriend.size() == fNum){
                        //TODO : Receive friend's timetable data
                    }
                }
            }catch(Exception e){}
            return null;
        }
    }
}
