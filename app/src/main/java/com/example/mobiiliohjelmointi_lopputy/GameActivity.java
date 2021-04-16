package com.example.mobiiliohjelmointi_lopputy;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    TextView textView_question;
    TextView textView_score;
    TextView textView_questionCount;
    Button button_confirm;
    Button button_addCalender;

    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioButton radioButton4;
    RadioGroup radioGroup;

    ArrayList<QuizQuestion> m_gameQuestions;
    RequestQueue m_requestQueue;

    int m_score = 0;
    boolean m_gameEnded = false;

    API_Singleton M_API;

    // keeps track on present question's index
    int indexOfPresentQuestion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        M_API = API_Singleton.getInstance(this);

        if (M_API.isGameDataDownloaded() && M_API.isGameDataResponseCodeOK())
            m_gameQuestions = M_API.getM_GameData();
        else {
            if (!M_API.isGameDataResponseCodeOK())
                Toast.makeText(this, "Not enought questions on category etc.", Toast.LENGTH_LONG).show();

            if (!M_API.isGameDataDownloaded())
                Toast.makeText(this, "game data not downloaded", Toast.LENGTH_LONG).show();

            this.finish();
        }

        this.initializeUI();
        // set first question on UI
        setUIforQuestion(m_gameQuestions.get(indexOfPresentQuestion));
    }

    // First question is set automatically after data is fetched
    public void addListenerOnConfirmButton() {
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_gameEnded)
                    finish();
                else
                    checkAnswer();

                indexOfPresentQuestion++;

                // next question on ui or hide ui for score
                if (indexOfPresentQuestion < m_gameQuestions.size()) {
                    setUIforQuestion(m_gameQuestions.get(indexOfPresentQuestion));
                }
                else {
                    endOfGameHideUI();
                    button_addCalender.setVisibility(View.VISIBLE);
                    m_gameEnded = true;
                }
            }
        });
    }

    private void checkAnswer(){
        // FIND SELECTED RADIO INPUT
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton)findViewById(selectedId);
        String playersAnswer= selectedRadioButton.getText().toString();

        // CHECK IS ANSWER CORRECT
        if (m_gameQuestions.get(indexOfPresentQuestion).getmCorrectAnswer().equals(playersAnswer)) {
            m_score++;
            textView_score.setText("Score: " + Integer.toString(m_score));
            Toast.makeText(GameActivity.this,"CORRECT!",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(GameActivity.this,"WRONG!\nCorrect answer:\n" + m_gameQuestions.get(indexOfPresentQuestion).getmCorrectAnswer(),Toast.LENGTH_SHORT).show();
        }
    }

    private void endOfGameHideUI(){
        radioGroup.setVisibility(View.GONE);
        String text = "Your score: " + m_score + "\n Return main menu";
        button_confirm.setText(text);
        button_confirm.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
        radioButton1.setText("");
        radioButton2.setText("");
        radioButton3.setText("");
        radioButton4.setText("");
        textView_question.setText("");
        textView_score.setText("");
    }

    public void addListenerRadioGroup() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
             button_confirm.setEnabled(true);
            }
        });
    }

    private void setUIforQuestion(QuizQuestion question) {
       /* set first radio button selected to
         avoid clicking confirm with unselected input */
        radioGroup.clearCheck();
        radioButton1.setChecked(true);

    if (question.isTrueFalseQuestion()) {
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
    textView_questionCount.setText("Question: " + (indexOfPresentQuestion + 1) + " of " + m_gameQuestions.size());
    }

    /* Initialize UI components onCreate */
    private void initializeUI() {
        textView_question = (TextView)findViewById(R.id.text_view_question);
        textView_score = (TextView)findViewById(R.id.text_view_score);
        textView_questionCount = (TextView)findViewById(R.id.text_view_question_count);
        button_confirm = (Button)findViewById(R.id.button_confirm_next);
        button_addCalender = (Button)findViewById(R.id.button_add_result_calender);
        radioButton1 = (RadioButton)findViewById(R.id.radio_button1);
        radioButton2 = (RadioButton)findViewById(R.id.radio_button2);
        radioButton3 = (RadioButton)findViewById(R.id.radio_button3);
        radioButton4 = (RadioButton)findViewById(R.id.radio_button4);
        radioGroup = (RadioGroup)findViewById(R.id.radio_group);

        button_addCalender.setVisibility(View.GONE);

        // initialize all listeners
        addListenerOnConfirmButton();
    }

    public void addResultCalender(View view) {
        Toast.makeText(this, "add calender ckilds", Toast.LENGTH_SHORT).show();

        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", true);
        intent.putExtra("title", "Game result");
        intent.putExtra("description", "Score: " + m_score + " / " + m_gameQuestions.size());
        startActivity(intent);
        finish();

    }
}