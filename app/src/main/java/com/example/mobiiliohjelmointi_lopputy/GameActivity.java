package com.example.mobiiliohjelmointi_lopputy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    API_Singleton m_API;
    RequestQueue m_requestQueue;
    ArrayList<QuizQuestion> m_gameQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        API_Singleton m_api = API_Singleton.getInstance(this);
        m_requestQueue = API_Singleton.getInstance(this).getRequestQueue();
        m_gameQuestions = new ArrayList<>();

        // hide ui until data is fetched
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.gameLayout);
        layout.setVisibility(View.GONE);

        Intent intent = getIntent();
        // fetch game data, start game after data is fetched
        getGameData(intent.getStringExtra("GAME_LINK"));
    }

    private void startGame() {
        TextView question = (TextView)findViewById(R.id.text_view_question);
        question.setText(m_gameQuestions.get(0).getmQuestion());
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
        if (m_requestQueue != null) {
            API_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        }
    }

    private void parseJSON(String JSONresponse) {
        try {
            JSONObject root = new JSONObject(JSONresponse);
            JSONArray questionsArray = root.getJSONArray("results");
            JSONObject question = questionsArray.getJSONObject(0);

            for (int i = 0; i < questionsArray.length(); i++) {
                parseQuestionAndAddToList(questionsArray.getJSONObject(i));
            }

            int i = 0;
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "halp parsiminen vituiz", Toast.LENGTH_LONG).show();
        }

        //show layout
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.gameLayout);
        layout.setVisibility(View.VISIBLE);
        startGame();
    }

    private void parseQuestionAndAddToList(JSONObject JSONQuestion) {
        try {
            String correctAnswer = JSONQuestion.getString("correct_answer");
            String question = JSONQuestion.getString("question");

            // incorrect answers to list --> string []
            ArrayList<String> wrongAnswersList = new ArrayList<>();
            JSONArray wrongAnswersArray = JSONQuestion.getJSONArray("incorrect_answers");
            for (int i = 0; i < wrongAnswersArray.length(); i++) {
                wrongAnswersList.add(wrongAnswersArray.getString(i));
            }

            String[] incorrect_answers = wrongAnswersList.toArray(new String[wrongAnswersList.size()]);

            m_gameQuestions.add(new QuizQuestion(question, correctAnswer, incorrect_answers));
            int a = 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}