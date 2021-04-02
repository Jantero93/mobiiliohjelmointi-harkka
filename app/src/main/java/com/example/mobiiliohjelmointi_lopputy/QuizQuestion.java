package com.example.mobiiliohjelmointi_lopputy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizQuestion {
    private String mCorrectAnswer;
    private String[] mWrongAnswers;
    private String mQuestion;

    public QuizQuestion(String question, String correctAnswer, String[] wrongAnswers) {
        mQuestion = question;
        mCorrectAnswer = correctAnswer;
        mWrongAnswers = wrongAnswers;
    }

    public String getmCorrectAnswer() {
        return mCorrectAnswer;
    }

    public String[] getAllAnswersMixed() {
        // convert to list and shuffle and return string[]
        List<String> allAnswers = Arrays.asList(mWrongAnswers);
        allAnswers.add(mCorrectAnswer);
        Collections.shuffle(allAnswers);
        return allAnswers.toArray(new String[allAnswers.size()]);
    }

    public String getmQuestion() {
        return mQuestion;
    }
}
