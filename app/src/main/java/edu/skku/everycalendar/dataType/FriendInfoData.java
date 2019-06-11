package edu.skku.everycalendar.dataType;

import java.util.ArrayList;
import java.util.TimerTask;

public class FriendInfoData {
    String id;
    String key;
    String name;
    ArrayList<TimetableData> classes;

    public FriendInfoData(String key, String name) {
        this.key = key;
        this.name = name;
    }


    public FriendInfoData(String id, String name, ArrayList<TimetableData> classes) {
        this.id = id;
        this.name = name;
        this.classes = classes;
    }

    public FriendInfoData(String name, ArrayList<TimetableData> classes) {
        this.name = name;
        this.classes = classes;
    }

    public boolean compare(FriendInfoData targ){
        if(name.equals(targ.getName())){
            for(TimetableData cs : classes){
                if(!targ.getClasses().contains(cs))
                    return false;
            }
            return true;
        }
        return false;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String idNum) {
        this.key = idNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<TimetableData> getClasses() {
        return classes;
    }

    public void setClassId(ArrayList<TimetableData> classes) {
        this.classes = classes;
    }
}
