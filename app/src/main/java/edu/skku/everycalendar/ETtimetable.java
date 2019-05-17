package edu.skku.everycalendar;

import java.util.ArrayList;

public class ETtimetable {
    String name;
    String place;
    String prof;
    String weekDay;
    Integer startTime, endTime;

    public ETtimetable(String name, String place, String prof, String weekDay, Integer startTime, Integer endTime) {
        this.name = name;
        this.place = place;
        this.prof = prof;
        this.weekDay = weekDay;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
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
