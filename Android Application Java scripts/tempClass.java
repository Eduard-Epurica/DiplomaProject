package com.example.ver1;

import java.sql.Timestamp;
import java.util.Timer;

public class tempClass {

    private int id;
    private double value;
    private String time;

    public tempClass(int id, double value, String time1) {

        this.id = id;
        this.value = value;
        this.time = time1;
    }

    //toString method

    @Override
    public String toString() {
        return "id=" + id + ", Temperature=" + value + ", time=" + time ;
    }

    //getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
