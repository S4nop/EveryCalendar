package edu.skku.everycalendar;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FriendTimetableReq extends TimeTableRequest {
    String key;

    FriendTimetableReq(String cookie, String key){
        this.key = key;
        this.cookie = cookie;
    }

    @Override
    public void makeTimeTable() {
        parseTimeTable(getTimeTable());
    }

    private String getTimeTable(){
        AsyncTimeTableRequest tTR = new AsyncTimeTableRequest();
        tTR.setMod(1);
        try {
            return tTR.execute(key).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public ArrayList<TimetableData> getClassList() {
        return super.getClassList();
    }
}
