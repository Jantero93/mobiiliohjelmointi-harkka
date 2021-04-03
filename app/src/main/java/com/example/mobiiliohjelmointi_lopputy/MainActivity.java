package com.example.mobiiliohjelmointi_lopputy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    API_Singleton m_API;
    RequestQueue m_requestQueue;
    private final String getAllCategoriesLink = "https://opentdb.com/api_category.php";
    private HashMap<String, Integer> m_categories;
    // default link if settings not changed
    private  String quickGameApiLink = "https://opentdb.com/api.php?amount=1";
    //private String customGameApiLink = "";

    final int REQUESTCODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        API_Singleton m_api = API_Singleton.getInstance(this);
        m_requestQueue = API_Singleton.getInstance(this).getRequestQueue();
        m_categories = new HashMap<String, Integer>();

        // set button enabled when categories data is downloaded from api
        Button settings_Button = (Button)findViewById(R.id.menuSettings_Button);
        settings_Button.setEnabled(false);



        getCategoriesToSettings();

    }

    //Start game activity
    public void playButton_clicked(View view) {
        Intent openGameActivityIntent = new Intent (this, GameActivity.class );
        openGameActivityIntent.putExtra("GAME_LINK", quickGameApiLink);
        startActivity( openGameActivityIntent );
    }

    //Start settings activity
    public void settingsButton_clicked(View view) {

        Intent openSettingsActivityIntent = new Intent( this, SettingsActivity.class );
        openSettingsActivityIntent.putExtra("ALL_CATEGORIES_HASHMAP", m_categories);
        startActivityForResult(openSettingsActivityIntent, REQUESTCODE);
    }

    //Start statistics activity
    public void statisticButton_clicked(View view) {
    }

    // Get needed API data on start
    private void getCategoriesToSettings(){
        // get categories on json
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getAllCategoriesLink,
                response -> {
                 //   Toast.makeText(this.getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    parseJsonAllCategories(response);

                },
                error -> {
                    Toast.makeText(this, "Settings not downloaded",Toast.LENGTH_LONG).show();
                }
        );
        if ( m_requestQueue != null ) {
            API_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        }
    }

    private void parseJsonAllCategories(String JSONresponse) {
        // parse JSON to hash map, where name key and value correspond id
       try {
            JSONObject root = new JSONObject(JSONresponse);
            JSONArray trivia_categories = root.getJSONArray("trivia_categories");

            for (int i = 0; i < trivia_categories.length(); i++) {
                JSONObject category = trivia_categories.getJSONObject(i);
                m_categories.put(category.getString("name"), category.getInt("id"));
            }


            Button settings_Button = (Button)findViewById(R.id.menuSettings_Button);
            settings_Button.setEnabled(true);

        } catch (JSONException e) {
           e.printStackTrace();
       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUESTCODE){
            if (resultCode == RESULT_OK) {
                quickGameApiLink = data.getStringExtra("GAME_LINK");
            }
        }
        else {
            Toast.makeText(this, "Error on game link" , Toast.LENGTH_LONG).show();
        }
    }
}