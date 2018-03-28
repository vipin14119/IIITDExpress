package com.dhcs.vipin.iiitdexpress.timetable;

/**
 * Created by Pulkit on 3/28/2018.
 */

public class Course {
    String code;
    String time;
    String day;
    String room;

    public Course(){};

    public Course(String code,String day, String time, String room) {
        this.code = code;
        this.time = time;
        this.day = day;
        this.room = room;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
