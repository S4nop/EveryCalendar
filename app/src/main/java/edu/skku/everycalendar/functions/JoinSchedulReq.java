package edu.skku.everycalendar.functions;

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
    String id;
    int fNum, dbNum;
    public void joinRequest(final String stDate,final String edDate, Integer stTime, Integer edTime, final ArrayList<String> friends){
        js = new JoinSchedule(stTime, edTime, friends);
        fNum = friends.size();
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
        dbNum = 0;
        Log.d("LOG_SERV", "chkRequest called");
        RealTimeDBPull.getDatatListFromDB(FirebaseDatabase.getInstance().getReference().child("SchedJoin").child(id),
                new CallArgFuncE_Req(), null, true);
    }

    class CallArgFuncE_Req extends CallableArg<String> {
        @Override
        public Void call() {
            try{
                if(!uploadedFriend.contains(arg)){
                    uploadedFriend.add(arg);
                    if(uploadedFriend.size() == fNum){
                        getFriendsTT();
                    }
                }
            }catch(Exception e){}
            return null;
        }
    }

    public void getFriendsTT(){
        Log.d("LOG_SERV", "chkRequest called");
        for(String s : uploadedFriend)
            RealTimeDBPull.getDatatListFromDB(FirebaseDatabase.getInstance().getReference().child("SchedJoin").child(s),
                    new CallArgFuncE_Join(), new CallArgFuncC_Join(), false);
    }

    class CallArgFuncE_Join extends CallableArg<String> {
        @Override
        public Void call() {
            try{
                String stTime = arg.split("startTime=\"")[1].split(",")[0];
                String edTime = arg.split("endTime=\"")[1].split(",")[0];
                String week = arg.split("weekDay=\"")[1].split("\\}")[0];
                js.addEvents(new TimetableData("","","",week,
                        Integer.parseInt(stTime), Integer.parseInt(edTime), "", 0), Integer.parseInt(week));
            }catch(Exception e){}
            return null;
        }
    }

    class CallArgFuncC_Join extends CallableArg<String> {
        @Override
        public Void call() {
            try{
                if(++dbNum == fNum){
                    js.makeTableView();
                }
            }catch(Exception e){}
            return null;
        }
    }
    public String getID(){ return id; }
}
