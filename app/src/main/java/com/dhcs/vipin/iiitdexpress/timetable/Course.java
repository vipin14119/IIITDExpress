package com.dhcs.vipin.iiitdexpress.timetable;

import java.io.Serializable;

/**
 * Created by Pulkit on 3/28/2018.
 */

public class Course implements Serializable {
    String code;
    String name;
    String time;
    String day;
    String room;
    boolean isAdded;

    public Course(){};

    public Course(String code, String name, String day, String time, String room, boolean isAdded) {
        this.code = code;
        this.name = name;
        this.time = time;
        this.day = day;
        this.room = room;
        this.isAdded = isAdded;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }
}
