package edu.skku.everycalendar.friends;

import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import edu.skku.everycalendar.dataType.TimetableData;
import edu.skku.everycalendar.functions.CallableArg;
import edu.skku.everycalendar.functions.RealTimeDBPull;

public class RecommendFriend {

    ArrayList<TimetableData> classes;
    HashMap<String, String> friendList;
    String id;

    public void recommend(){
        friendList = new HashMap<>();
        for(TimetableData td : classes) {
            if(td.getIdNum() != "")
                RealTimeDBPull.getDatatListFromDB(FirebaseDatabase.getInstance().getReference().child("ClassData").child(td.getIdNum()),
                    new CallArgFuncE(), null, false);
        }
    }

    class CallArgFuncE extends CallableArg<String> {
        @Override
        public Void call() {
            try{
                if(!id.equals(arg.split("name=")[1].split(",")[0]))
                    friendList.put(arg.split("name=")[1].split(",")[0], arg.split("id=")[1].split("\\}")[0]);
                    //Log.d("LOG_RECOMMEND", "put!!");
            }catch(Exception e){}
            return null;
        }
    }

    public void setClasses(ArrayList<TimetableData> classes){
        this.classes = classes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, String> getFriendList() {
        return friendList;
    }
}
