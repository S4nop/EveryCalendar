package edu.skku.everycalendar.monthItems;


public class MonthItem {
    private int yearValue;
    private int monthValue;
    private int weekValue;
    private int dayValue;
    public MonthItem(int year, int month, int week, int day) {
        yearValue=year;
        monthValue=month;
        weekValue=week;
        dayValue = day;
    }

    public int getDay() {
        return dayValue;
    }
    public int getYear(){
        return yearValue;
    }
    public int getMonth(){
        return monthValue;
    }
    public int getWeek(){
        return weekValue;
    }
}
