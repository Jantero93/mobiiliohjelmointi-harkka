package com.example.mobiiliohjelmointi_lopputy;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Start game activity
    public void playButton_clicked(View view) {
    }

    //Start settings activity
    public void settingsButton_clicked(View view) {
    }

    //Start statistics activity
    public void statisticButton_clicked(View view) {
    }

    // Get needed API data on start
    private void dont_implement_this_yet(){}
}