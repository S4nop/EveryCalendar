package edu.skku.everycalendar.functions;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import edu.skku.everycalendar.dataType.TimetableData;

public class JoinSchedulReq {
    JoinSchedule js;
    ArrayList<String> uploadedFriend;
    ArrayList<String> uList = new ArrayList<>();
    String id;
    Context context;
    int fNum, fNum2, dbNum;

    public void setFnum(int n){
        fNum = n;
    }

    public void setfNum2(int fNum2) {
        this.fNum2 = fNum2;
    }

    public synchronized void addDBNum(){
        Log.d("LOG_DBNUM", "" + (dbNum + 1));
        if(++dbNum == fNum){
            js.makeTableView();
        }
    }

    public void joinRequest(final String stDate,final String edDate, final ArrayList<String> friends, JoinSchedule js){
        this.js = js;
        FirebaseDatabase.getInstance().getReference().child("SchedJoinReq").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

                Random rnd = new Random();
                id = Integer.toString(rnd.nextInt(50000));
                while(child.hasNext())
                {
                    if(id.equals(child)){
                        id = Integer.toString(rnd.nextInt(50000));
                        child = dataSnapshot.getChildren().iterator();
                    }
                }

                Map<String, Object> out = new HashMap<>();
                for(String nid : friends){
                    Map<String, String> val = new HashMap<>();
                    Map<String, Object> pack = new HashMap<>();
                    val.put("edDate", edDate);
                    val.put("stDate", stDate);
                    val.put("reqID", id);
                    pack.put(nid, val);
                    out.put("/SchedJoinReq/" + nid + "/request", val);
                }
                FirebaseDatabase.getInstance().getReference().updateChildren(out);
                chkRequest();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void chkRequest(){
        uploadedFriend = new ArrayList<>();
        Log.d("LOG_SERV", "chkRequest called with id : " + id);
        RealTimeDBPull.getDatatListFromDB(FirebaseDatabase.getInstance().getReference().child("SchedJoin").child(id),
                new CallArgFuncE_Req(), null, true);
    }

    class CallArgFuncE_Req extends CallableArg<String> {
        @Override
        public Void call() {
            try{
                if(!uploadedFriend.contains(arg)){
                    uploadedFriend.add(arg);
                    if(uploadedFriend.size() == fNum2){
                        getFriendsTT();
                    }
                }
            }catch(Exception e){}
            return null;
        }
    }

    public void getFriendsTT(){
        for(String s : uploadedFriend) {
            Log.d("LOG_SERV", "getFriendsTT called with id : " + s);
            RealTimeDBPull.getDatatListFromDB(FirebaseDatabase.getInstance().getReference().child("SchedJoin").child(id).child(s),
                    new CallArgFuncE_Join(), new CallArgFuncC_Join(), false);
        }
    }

    class CallArgFuncE_Join extends CallableArg<String> {
        @Override
        public Void call() {
            try{
                Log.d("LOG_CALLARGEJOIN", arg);
                String stTime = arg.split("startTime=")[1].split(",")[0];
                String edTime = arg.split("endTime=")[1].split(",")[0];
                String week = arg.split("weekDay=")[1].split(",")[0];
                js.addEvent(new TimetableData("","","",week,
                        Integer.parseInt(stTime), Integer.parseInt(edTime), "", 0), Integer.parseInt(week));
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    class CallArgFuncC_Join extends CallableArg<String> {
        @Override
        public Void call() {
            try{
                Log.d("LOGCALL", "HERE");
                addDBNum();
            }catch(Exception e){}
            return null;
        }
    }
    public String getID(){ return id; }
}
