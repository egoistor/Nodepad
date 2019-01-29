package com.example.wangbeimin.Nodepad.utils;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

public class DeletedNote extends LitePalSupport{
    private String message;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public DeletedNote(){
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSecond() {

        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getMonth() {

        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMinute() {

        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getHour() {

        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDay() {

        return day;
    }
}
