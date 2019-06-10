package edu.skku.everycalendar.functions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckOurUser {
    public static ArrayList<String> userList = new ArrayList<>();

    public static void getUserList(){
        FirebaseDatabase.getInstance().getReference().child("User_information").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    userList.add(key);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static boolean chkUser(String id){
        return userList.contains(id);
    }
}
