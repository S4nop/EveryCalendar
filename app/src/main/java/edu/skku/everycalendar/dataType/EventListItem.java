package edu.skku.everycalendar.dataType;

public class EventListItem {
    private String event_name;
    private String event_st_date;
    private String event_ed_date;
    private String event_loca;
    private String event_desc;

    public EventListItem(String name, String st_date, String ed_date, String loca, String desc){
        this.event_name = name;
        this.event_st_date = st_date;
        this.event_ed_date = ed_date;
        this.event_loca = loca;
        this.event_desc = desc;
    }

    public String getEvent_name() {
        return event_name;
    }


    public String getEvent_loca() {
        return event_loca;
    }

    public String getEvent_desc() {
        return event_desc;
    }

    public String getEvent_st_date() {
        return event_st_date;
    }

    public String getEvent_ed_date() {
        return event_ed_date;
    }
}
