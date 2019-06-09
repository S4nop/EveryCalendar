package edu.skku.everycalendar.functions;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class RealTimeDBPull {
    public static void getDatatListFromDB(DatabaseReference targDB, final CallableArg<String> funcEach,
                                          final CallableArg<String> funcCng, final boolean getKeyMode) {

        targDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    Log.d("getFirebaseDatabase", "key: " + (getKeyMode ? postSnapshot.getKey() : postSnapshot.getValue()));
                    funcEach.setArg((getKeyMode ? postSnapshot.getKey() : postSnapshot.getValue()).toString());
                    funcEach.call();
                }

                if(funcCng != null) funcCng.call();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
