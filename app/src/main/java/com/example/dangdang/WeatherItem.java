package com.example.dangdang;

import android.graphics.drawable.Drawable;

public class WeatherItem {
    private String day;
    private String hour;
    private String temp;
    private Drawable wfkor;
    private String pop;

    public void setDay(String day){
        this.day = day;
    }

    public String getDay(){
        return day;
    }

    public void setHour(String hour){
        this.hour = hour;
    }

    public String getHour(){
        return hour;
    }

    public void setTemp(String temp){
        this.temp = temp;
    }

    public String getTemp(){
        return temp;
    }

    public void setWfkor(Drawable wfkor){
        this.wfkor = wfkor;
    }

    public Drawable getWfkor(){
        return wfkor;
    }

    public void setPop(String pop){
        this.pop = pop;
    }

    public String getPop(){
        return pop;
    }

}