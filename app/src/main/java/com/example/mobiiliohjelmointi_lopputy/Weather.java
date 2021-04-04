package com.example.mobiiliohjelmointi_lopputy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;

public class Weather extends AppCompatActivity {
private final String apiLink = "https://api.openweathermap.org/data/2.5/weather?id=634964&appid=881e2e4957f0b379389af22826a33532&units=metric";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        RequestQueue requestQueue = API_Singleton.getInstance(this).getRequestQueue();
    }

    private void getWeatherData() {

    }

    private void parseJSONweatherData() {

    }
}