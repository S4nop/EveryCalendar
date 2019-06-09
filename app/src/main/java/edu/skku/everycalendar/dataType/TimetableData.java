package edu.skku.everycalendar.dataType;

import java.util.ArrayList;
import java.util.IdentityHashMap;

public class TimetableData {
    private String name;
    private String place;
    private String weekDay;
    private String descript;
    private Integer startTime, endTime;
    private Integer color;

    public TimetableData(String name, String place, String descript, String weekDay, Integer startTime, Integer endTime, Integer color) {
        this.name = name;
        this.place = place;
        this.descript = descript;
        this.weekDay = weekDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.color = color;
    }

    public Integer getIdNum(){
        return color;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

}
