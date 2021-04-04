package com.example.mobiiliohjelmointi_lopputy;

import java.util.Date;

public class DayForecast {
    public final Date date;
    public final double temp_morning;
    public final double temp_day;
    public final double temp_evening;
    public final double temp_night;
    public final String description;

    public DayForecast( Date date, double temp_morning, double temp_day,
        double temp_evening, double temp_night, String description )
    {
        this.date = date;
        this.temp_morning = temp_morning;
        this.temp_day = temp_day;
        this.temp_evening = temp_evening;
        this.temp_night = temp_night;
        this.description = description;
    }
}
