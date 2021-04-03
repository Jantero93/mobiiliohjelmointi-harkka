package com.example.mobiiliohjelmointi_lopputy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    RequestQueue m_requestQueue;
    ArrayList<QuizQuestion> m_gameQuestions;

    TextView textView_question;
    TextView textView_score;
    TextView textView_questionCount;
    Button button_confirm;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioButton radioButton4;
    RadioGroup radioGroup;

    int m_score = 0;
    int m_question_count = 0;

    // keeps track on present question's index
    int indexOfPresentQuestion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        API_Singleton m_api = API_Singleton.getInstance(this);
        m_requestQueue = API_Singleton.getInstance(this).getRequestQueue();
        m_gameQuestions = new ArrayList<>();
        this.initializeUI();


        Intent intent = getIntent();
        // fetch game data, start game after data is fetched
        getGameData(intent.getStringExtra("GAME_LINK"));
    }

    private void playGame() {

        if (indexOfPresentQuestion < m_gameQuestions.size()) {
            setUIforQuestion(m_gameQuestions.get(indexOfPresentQuestion));
        } else {
            // tee joku loppu juttu endActivityFunction();
        }

    }


    public void confirmButtonClicked(View view) {
      indexOfPresentQuestion++;
    }

    private void setUIforQuestion(QuizQuestion question) {
    if (question.ismIsTrueFalseQuestion()) {
        // show only 2 answer option
        radioButton3.setVisibility(View.GONE);
        radioButton4.setVisibility(View.GONE);
        radioButton1.setText("True");
        radioButton2.setText("False");
    } else {
        // show all options
        radioButton3.setVisibility(View.VISIBLE);
        radioButton4.setVisibility(View.VISIBLE);
        // get all answers mixed
        String[] answersMixed = question.getAllAnswersMixed();
        radioButton1.setText(answersMixed[0]);
        radioButton2.setText(answersMixed[1]);
        radioButton3.setText(answersMixed[2]);
        radioButton4.setText(answersMixed[3]);
    }

    textView_question.setText(question.getmQuestion());
        textView_questionCount.setText("Question: " + m_question_count + "/" + m_gameQuestions.size());
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
        playGame();
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

    /* Initialize UI components onCreate */
    private void initializeUI() {


        textView_question = (TextView)findViewById(R.id.text_view_question);
        textView_score = (TextView)findViewById(R.id.text_view_score);
        textView_questionCount = (TextView)findViewById(R.id.text_view_question_count);
        button_confirm = (Button)findViewById(R.id.button_confirm_next);
        radioButton1 = (RadioButton)findViewById(R.id.radio_button1);
        radioButton2 = (RadioButton)findViewById(R.id.radio_button2);
        radioButton3 = (RadioButton)findViewById(R.id.radio_button3);
        radioButton4 = (RadioButton)findViewById(R.id.radio_button4);
        radioGroup = (RadioGroup)findViewById(R.id.radio_group);

        // hide ui until data is fetched
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.gameLayout);
        layout.setVisibility(View.GONE);

        // add listener to confirm button
        addListenerOnRadioButton();
    }

    public void addListenerOnRadioButton() {
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = (RadioButton)findViewById(selectedId);
                Toast.makeText(GameActivity.this, selectedRadioButton.getText().toString(), Toast.LENGTH_LONG).show();
                String test = selectedRadioButton.getText().toString();
                int i = 0;
            }
        });
    }



}