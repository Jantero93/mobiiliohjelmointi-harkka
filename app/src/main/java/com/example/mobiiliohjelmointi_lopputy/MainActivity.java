package com.example.mobiiliohjelmointi_lopputy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    API_Singleton m_API;
    RequestQueue m_requestQueue;
    private final String getAllCategoriesLink = "https://opentdb.com/api_category.php";
    private HashMap<String, Integer> m_categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        API_Singleton m_api = API_Singleton.getInstance(this);
        m_categories = new HashMap<String, Integer>();

        m_requestQueue = API_Singleton.getInstance(this).getRequestQueue();
        getCategoriesToSettings();

    }

    //Start game activity
    public void playButton_clicked(View view) {
    }

    //Start settings activity
    public void settingsButton_clicked(View view) {
        Intent openSettingsActivityIntent = new Intent( this, SettingsActivity.class );
        startActivity( openSettingsActivityIntent );
    }

    //Start statistics activity
    public void statisticButton_clicked(View view) {
    }

    // Get needed API data on start
    private void getCategoriesToSettings(){
        //get weather data on json format -->  string request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getAllCategoriesLink,
                response -> {
                    Toast.makeText(this.getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    parseJsonAllCategories(response);

                },
                error -> {
                    Toast.makeText(this, "vituiz meny kateforiat",Toast.LENGTH_LONG).show();
                }
        );
        if ( m_requestQueue != null ) {
            API_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        }
    }

    private void parseJsonAllCategories(String JSONresponse) {
       try {
            JSONObject root = new JSONObject(JSONresponse);

        } catch (JSONException e) {
           e.printStackTrace();
       }
    }

}