package edu.skku.everycalendar;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;

import static com.google.common.primitives.UnsignedInts.max;
import static com.google.common.primitives.UnsignedInts.min;

public class JoinSchedule {
    Integer stTime, edTime;
    ArrayList<TimetableData> wEvents[];

    public JoinSchedule(Integer stTime, Integer edTime) {
        this.stTime = stTime;
        this.edTime = edTime;

        wEvents = new ArrayList[7];
    }

    public void addEvents(ArrayList<TimetableData> events){
        for(TimetableData td : events){
            wEvents[Integer.parseInt(td.getWeekDay())].add(td);
        }
    }

    private void sortEvents(){
        for(int i = 0; i < 7; i++)
            Collections.sort(wEvents[i], new Comparator<TimetableData>() {
                @Override
                public int compare(TimetableData timetableData, TimetableData t1) {
                    return (Integer.parseInt(timetableData.getWeekDay()) > Integer.parseInt(t1.getWeekDay()) ? 1 :
                            timetableData.getStartTime() > t1.getStartTime() ? 1 :
                                    timetableData.getEndTime() < t1.getEndTime() ? 1 : 0);
                }
            });


    }

    public ArrayList<TimetableData> getAbleTime(){
        ArrayList<TimetableData> rslt = new ArrayList<>();

        for(int i = 0; i < 7; i++){
            rslt.addAll(findTime(i));
        }

        return rslt;
    }

    private ArrayList<TimetableData> findTime(int idx){
        ArrayList<TimetableData> wRslt = new ArrayList<>();
        Random rnd = new Random();
        Integer st = stTime, ed = edTime;

        for(TimetableData td : wEvents[idx]){
            if(st < td.getStartTime()) wRslt.add(new TimetableData("","","","",st,
                    min(ed, td.getStartTime()), Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255))));
            st = max(st, td.getEndTime());

            if(st >= ed) break;
        }

        return wRslt;
    }

}
