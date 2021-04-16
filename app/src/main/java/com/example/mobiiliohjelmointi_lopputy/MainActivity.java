package com.example.mobiiliohjelmointi_lopputy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity
{
    RequestQueue m_requestQueue;
    // get categories from api
    private final String getAllCategoriesLink = "https://opentdb.com/api_category.php";
    // get categories from api category name - id pair
    private HashMap<String, Integer> m_all_categories;
    // default link if settings not changed, 'quick play'
    private  String gameApiLink = "https://opentdb.com/api.php?amount=10";

    // Request code acticityforresult (settingsactivity)
    final int REQUESTCODE_SETTINGS = 1;

    API_Singleton m_API;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get instance of api and download all categories on start up
        m_API = API_Singleton.getInstance(this);
        m_API.getAllCategoriesData();

        m_API.getGameData(gameApiLink);


    }

    //Start game activity
    public void playButton_clicked(View view) {
        if (m_API.isGameDataDownloaded()) {
            Intent openGameActivityIntent = new Intent(this, GameActivity.class);
            //  openGameActivityIntent.putExtra("GAME_LINK", gameApiLink);
            startActivity(openGameActivityIntent);
        } else{
            Toast.makeText(this, "Game Data not download, try again", Toast.LENGTH_SHORT).show();
        }
    }

    //Start settings activity if data is downloaded
    public void settingsButton_clicked(View view) {
        if (m_API.isCategoriesDownloaded()){
            // put categories to next activity
            m_all_categories = m_API.getM_categories();

            Intent openSettingsActivityIntent = new Intent( this, SettingsActivity.class );
            openSettingsActivityIntent.putExtra("ALL_CATEGORIES_HASHMAP", m_all_categories);
            startActivityForResult(openSettingsActivityIntent, REQUESTCODE_SETTINGS);
        }
        else {
            Toast.makeText(this, "Settings data not downloaded yet, try again", Toast.LENGTH_LONG).show();
        }
    }

    //Start weather activity
    public void weatherButton_clicked(View view) {
        Intent openWeatherActivityIntent = new Intent ( this, Weather.class );
        startActivity(openWeatherActivityIntent);
    }


    // Get result from settings activity (game link for api)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUESTCODE_SETTINGS){
            if (resultCode == RESULT_OK) {
                m_API.getGameData(data.getStringExtra("GAME_LINK"));
            }
        }
        else {
            Toast.makeText(this, "Error on game link" , Toast.LENGTH_LONG).show();
        }
    }
}