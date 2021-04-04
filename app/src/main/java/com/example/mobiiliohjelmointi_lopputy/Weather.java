package com.example.mobiiliohjelmointi_lopputy;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Weather extends AppCompatActivity {
   // pro.openweathermap.org/data/2.5/forecast/hourly?lat={lat}&lon={lon}&appid={API key}
private final String MapiLink = "https://api.openweathermap.org/data/2.5/onecall?lat={LAT}&lon={LON}&appid={API_KEY}&units=metric&exclude=current,minutely,hourly";
private final String APIKEY = "881e2e4957f0b379389af22826a33532";
private final String testExample = "https://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&appid={API KEY}&units=metric&exclude=current,minutely,hourly";

private Location lastLocation;
private LocationManager locationManager;
private RequestQueue requestQueue;
private ArrayList<DayForecast> dayForecastArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        dayForecastArrayList = new ArrayList<>();

        // RequestQueue from singleton
        requestQueue = API_Singleton.getInstance(this).getRequestQueue();
        // Get location on start up
        getLocation();

    }

    private void getLocation(){
        locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        // Check permissions
        if (checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 0);
            return;
        }
        lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        getWeatherData();
     //   TextView text = (TextView)findViewById(R.id.textView);
      //  text.setText(Double.toString(lastLocation.getLatitude()));
       // TextView text2 = (TextView)findViewById(R.id.textView2);
      //  text2.setText(Double.toString(lastLocation.getLongitude()));
    }

    private void getWeatherData() {
        String url = getApiLinkRightFormat();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                parseJSONweatherData(response);
                },
                error -> {
                    Toast.makeText(this, "Ei onnistunut säähaku", Toast.LENGTH_LONG).show();
                }
        );
        if (requestQueue != null){
            API_Singleton.getInstance(this).addToRequestQueue( stringRequest );
        }

    }

    private void parseJSONweatherData(String response) {
        try {
            JSONObject root = new JSONObject(response);
            JSONArray daily = root.getJSONArray("daily");

            for (int i = 0; i < daily.length(); i++) {
                dayForecastArrayList.add(parseDailyJsonToObject(daily.getJSONObject(i)));
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private DayForecast parseDailyJsonToObject(JSONObject dailyJSON) {
        double morningTemp = 0, dayTemp = 0, eveningTemp = 0, nightTemp = 0;
        String desc = "";
        Date date = new Date();
        try {




            JSONObject tempObject = dailyJSON.getJSONObject("temp");
            morningTemp = tempObject.getDouble("morn");
            dayTemp = tempObject.getDouble("day");
            eveningTemp = tempObject.getDouble("eve");
            nightTemp = tempObject.getDouble("night");

            // get description
            JSONArray weather = dailyJSON.getJSONArray("weather");
            desc = weather.getJSONObject(0).getString("main");

            long timestamp = dailyJSON.getLong("dt");
            date.setTime(timestamp*1000);
            int i = 0;


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new DayForecast(date, morningTemp, dayTemp, eveningTemp, nightTemp, desc );
    }

    private String getApiLinkRightFormat(){
        String apiLink = MapiLink;
        apiLink = apiLink.replace("{LAT}",Double.toString(lastLocation.getLatitude()));
        apiLink = apiLink.replace("{LON}", Double.toString(lastLocation.getLongitude()));
        apiLink = apiLink.replace("{API_KEY}", APIKEY);
        return apiLink;
    }
}