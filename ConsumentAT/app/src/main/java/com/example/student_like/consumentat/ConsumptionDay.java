package com.example.student_like.consumentat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsumptionDay {

    String _dayTime;
    String _bier;
    String _wein;
    String _ziga;
    String _shot;

    public ConsumptionDay(String dayTime, String bier, String wein, String ziga, String shot){
        this._dayTime = dayTime;
        this._bier = bier;
        this._wein = wein;
        this._ziga = ziga;
        this._shot = shot;
    }

    public String getDayTime(){
        return this._dayTime;
    }

    public void setDayTime(String dayTime){
        this._dayTime = dayTime;
    }

    public String getBier(){
        return this._bier;
    }

    public void SetBier(String bier){
        this._bier = bier;
    }
}
