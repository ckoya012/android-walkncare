package com.example.walkinclinic;

public class DailySchedule {
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    String day;
    String time1;
    String time2;

    public DailySchedule(String startHour, String endHour) {
        this.time1 = startHour;
        this.time2 = endHour;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }
}
