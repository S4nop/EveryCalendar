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

public class JoinSchedulReq {
    String id;
    public void joinRequest(final String stDate, final String edDate, final ArrayList<String> friends){
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

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getID(){ return id; }
}
