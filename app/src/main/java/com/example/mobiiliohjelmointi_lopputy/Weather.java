package com.example.mobiiliohjelmointi_lopputy;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;
import java.util.List;

public class Weather extends AppCompatActivity {
   // pro.openweathermap.org/data/2.5/forecast/hourly?lat={lat}&lon={lon}&appid={API key}
private final String DEFAULT_API_LINK = "https://api.openweathermap.org/data/2.5/onecall?lat={LAT}&lon={LON}&appid={API_KEY}&units=metric&exclude=current,minutely,hourly";
private final String APIKEY = "881e2e4957f0b379389af22826a33532";

private Location lastLocation;
private LocationManager locationManager;
private RequestQueue requestQueue;
private ArrayList<DayForecast> dayForecastArrayList;
private SimpleAdapter simpleAdapter;

private List<HashMap<String, String>> dayForecasthashMapList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        dayForecastArrayList = new ArrayList<>();

        // RequestQueue from singleton
        requestQueue = API_Singleton.getInstance(this).getRequestQueue();
    }

    // get last location when enter on app
    @Override
    public void onStart(){
        super.onStart();
        getLastLocation();
    }

    // get fetched data off when leave app
    @Override
    public void onPause(){
        super.onPause();
        dayForecasthashMapList.clear();
        dayForecastArrayList.clear();
    }

    private void getLastLocation(){
        locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        // Check permissions
        if (checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 0);
            return;
        }
        lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            Toast.makeText(this, "Pls turn GPS on", Toast.LENGTH_LONG).show();

        if (lastLocation == null)
            Toast.makeText(this, "No last location on phone", Toast.LENGTH_LONG).show();
        else
            getWeatherData();
    }

    private void getWeatherData() {
        // add coordinates to link
        String url = getApiLinkRightFormat();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                parseJSONweatherData(response);
                },
                error -> {
                    Toast.makeText(this, "Ei onnistunut s????haku", Toast.LENGTH_LONG).show();
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

            // parse from JSON to Dayforecast and add to dayforecast ha
            for (int i = 0; i < daily.length(); i++) {
                dayForecastArrayList.add(parseDailyJsonToObject(daily.getJSONObject(i)));
            }

            for( DayForecast dayForecast : dayForecastArrayList) {
                HashMap<String,String> dayForecastItemHash = new HashMap<>();
                dayForecastItemHash.put("description", dayForecast.description);
                dayForecastItemHash.put("morningTemp", "Morning:\n" + (int)round(dayForecast.temp_morning, 0) + " ??C");
                dayForecastItemHash.put("dayTemp", "Day:\n" + (int)round(dayForecast.temp_day, 0) + " ??C");
                dayForecastItemHash.put("eveningTemp", "Evening:\n" + (int)round(dayForecast.temp_evening, 0) + " ??C");
                dayForecastItemHash.put("nightTemp", "Night:\n" + (int)round(dayForecast.temp_night, 0) + " ??C");
                dayForecastItemHash.put("date", dayForecast.stringDate);
                dayForecasthashMapList.add ( dayForecastItemHash );
            }

            // add adapter, corresponding ui elements and hash map keys
            simpleAdapter = new SimpleAdapter( this,  dayForecasthashMapList,
                    R.layout.weather_list_item_layout,
                    new String[] {"description", "morningTemp", "dayTemp", "eveningTemp", "nightTemp", "date"},
                    new int[] {R.id.textView_description, R.id.textView_morningTemp, R.id.textView_dayTemp,
                    R.id.textView_evenTemp, R.id.textView_nightTemp, R.id.textView_Date}
                    );

            ListView dayforecast = (ListView)findViewById(R.id.forecastListView);
            dayforecast.setAdapter( simpleAdapter );
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private DayForecast parseDailyJsonToObject(JSONObject dailyJSON) {
        double morningTemp = -99, dayTemp = -99, eveningTemp = -99, nightTemp = -99;
        String desc = "empty";
        Date date = new Date();
        try {
            JSONObject tempObject = dailyJSON.getJSONObject("temp");
            morningTemp = tempObject.getDouble("morn");
            dayTemp = tempObject.getDouble("day");
            eveningTemp = tempObject.getDouble("eve");
            nightTemp = tempObject.getDouble("night");

            // get description
            JSONArray weather = dailyJSON.getJSONArray("weather");
            desc = weather.getJSONObject(0).getString("description");

            long timestamp = dailyJSON.getLong("dt");
            // time in ms for java date class
            date.setTime(timestamp*1000);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new DayForecast(date, morningTemp, dayTemp, eveningTemp, nightTemp, desc );
    }

    private String getApiLinkRightFormat(){
        String apiLink = DEFAULT_API_LINK;
        apiLink = apiLink.replace("{LAT}", Double.toString(lastLocation.getLatitude()));
        apiLink = apiLink.replace("{LON}", Double.toString(lastLocation.getLongitude()));
        apiLink = apiLink.replace("{API_KEY}", APIKEY);
        return apiLink;
    }

}