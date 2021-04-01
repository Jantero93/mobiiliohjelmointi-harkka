package com.example.mobiiliohjelmointi_lopputy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

public class GameActivity extends AppCompatActivity {
    API_Singleton m_API;
    RequestQueue m_requestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        API_Singleton m_api = API_Singleton.getInstance(this);
        m_requestQueue = API_Singleton.getInstance(this).getRequestQueue();

        Intent intent = getIntent();
        getGameData(intent.getStringExtra("GAME_LINK"));


    }

    private void getGameData(String apiLink) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiLink,
                response -> {

            parseJSON(response);
            },
                error -> {
                    Toast.makeText(this, "Game akt error link", Toast.LENGTH_LONG).show();
                }
        );
        if (m_requestQueue != null){
            API_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        }
    }

    private void parseJSON(String response) {
        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
    }


}