package edu.skku.everycalendar.googleCalendar;

public class EventListItem {
    private String event_name;
    private String event_date;
    private String event_loca;
    private String event_desc;

    public EventListItem(String name, String date, String loca, String desc){
        this.event_name = name;
        this.event_date = date;
        this.event_loca = loca;
        this.event_desc = desc;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_date() {
        return event_date;
    }

    public String getEvent_loca() {
        return event_loca;
    }

    public String getEvent_desc() {
        return event_desc;
    }
}
