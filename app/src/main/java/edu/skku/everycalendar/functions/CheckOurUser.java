package edu.skku.everycalendar.functions;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.skku.everycalendar.dataType.FriendInfoData;
import edu.skku.everycalendar.dataType.TimetableData;

public class CheckOurUser {
    public static ArrayList<FriendInfoData> userList = new ArrayList<>();

    public static void getUserList(){
        FirebaseDatabase.getInstance().getReference().child("User_information").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String id = postSnapshot.getKey();
                    String data = postSnapshot.getValue().toString();
                    Log.d("LOG_CHKOURUSRE", id + " : " + data);
                    userList.add(new FriendInfoData(id, data.split("Name=")[1].split("\\}")[0], makeTD(data)));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static ArrayList<TimetableData> makeTD(String s){
        ArrayList<TimetableData> tdList = new ArrayList<>();
        String[] tmp = s.split("descript=");
        for(int i = 1; i < tmp.length; i++){
            Log.d("LOG_MAKETD", tmp[i]);
            try {
                tdList.add(new TimetableData(tmp[i].split("name=")[1].split(",")[0], tmp[i].split("place=")[1].split("\\}")[0], tmp[i].split(",")[0], tmp[i].split("weekDay=")[1].split(",")[0],
                        Integer.parseInt(tmp[i].split("startTime=")[1].split(",")[0]), Integer.parseInt(tmp[i].split("endTime=")[1].split(",")[0]), tmp[i].split("idNum=")[1].split(",")[0], Integer.parseInt(tmp[i].split("color=")[1].split(",")[0])));
            }catch(Exception e){}
        }
        return tdList;
    }
    public static boolean chkUser(FriendInfoData fr){
        Log.d("LOG_CHKUSER", fr.getName() + "-" + fr.getId());
        boolean result = false;
        for(FriendInfoData fInfo : userList){
            if(fInfo.getName().equals(fr.getName())){
                Log.d("LOG_CHKUSER" , "Same name");
                result = true;
                for(TimetableData td : fInfo.getClasses()){
                    for(TimetableData ftd : fr.getClasses()){
                        result = false;
                        if(ftd.getIdNum().equals(td.getIdNum())) {
                            result = true;
                            break;
                        }
                    }
                    if(!result) break;
                }
                if(result){
                    fr.setId(fInfo.getId());
                    return true;
                }
            }
            result = false;
        }
        return result;
    }
}
