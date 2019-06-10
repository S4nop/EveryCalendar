package edu.skku.everycalendar.dataType;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.IdentityHashMap;

public class TimetableData implements Parcelable {
    private String name;
    private String place;
    private String weekDay;
    private String descript;
    private Integer startTime, endTime;
    private Integer color;
    private String idNum;

    public TimetableData(String name, String place, String descript, String weekDay, Integer startTime, Integer endTime, String idNum, Integer color) {
        this.name = name;
        this.place = place;
        this.descript = descript;
        this.weekDay = weekDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.idNum = idNum;
        this.color = color;
    }

    public TimetableData(Parcel in){
        this.name = in.readString();
        this.place = in.readString();
        this.descript = in.readString();
        this.weekDay = in.readString();
        this.startTime = in.readInt();
        this.endTime = in.readInt();
        this.color = in.readInt();
    }
    public String getIdNum(){
        return idNum;
    }

    public Integer getColor() { return color; }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(place);
        dest.writeString(descript);
        dest.writeString(weekDay);
        dest.writeInt(startTime);
        dest.writeInt(endTime);
        dest.writeInt(color);
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @Override
        public Object createFromParcel(Parcel source) {
            return new TimetableData(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new TimetableData[size];
        }
    };
}
