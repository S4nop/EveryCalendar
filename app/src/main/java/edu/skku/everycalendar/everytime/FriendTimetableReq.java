package edu.skku.everycalendar.everytime;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import edu.skku.everycalendar.dataType.TimetableData;

public class FriendTimetableReq extends TimeTableRequest {
    String key;

    public FriendTimetableReq(String cookie, String key){
        this.key = key;
        this.cookie = cookie;
    }

    @Override
    public void makeTimeTable() {
        finished = false;
        parseTimeTable(getTimeTable());
        finished = true;
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
