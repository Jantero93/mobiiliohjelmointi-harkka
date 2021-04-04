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
private final String MapiLink = "https://api.openweathermap.org/data/2.5/onecall?lat={LAT}&lon={LON}&appid={API_KEY}&units=metric&exclude=current,minutely,hourly";
private final String APIKEY = "881e2e4957f0b379389af22826a33532";
private final String testExample = "https://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&appid={API KEY}&units=metric&exclude=current,minutely,hourly";

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

            // parse from JSON to Dayforecast and add to dayforecast ha
            for (int i = 0; i < daily.length(); i++) {
                dayForecastArrayList.add(parseDailyJsonToObject(daily.getJSONObject(i)));
            }

            for( DayForecast dayForecast : dayForecastArrayList) {
                HashMap<String,String> dayForecastItemHash = new HashMap<>();
                dayForecastItemHash.put("description", dayForecast.description);
                dayForecastItemHash.put("morningTemp", "Morning:\n" + dayForecast.temp_morning + " °C");
                dayForecastItemHash.put("dayTemp", "Day:\n" + dayForecast.temp_day + " °C");
                dayForecastItemHash.put("eveningTemp", "Evening:\n" + dayForecast.temp_evening + " °C");
                dayForecastItemHash.put("nightTemp", "Night:\n" + dayForecast.temp_night + " °C");
                dayForecastItemHash.put("date", dayForecast.stringDate);
                dayForecasthashMapList.add ( dayForecastItemHash );
            }

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
            desc = weather.getJSONObject(0).getString("main");

            long timestamp = dailyJSON.getLong("dt");
            date.setTime(timestamp*1000);

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