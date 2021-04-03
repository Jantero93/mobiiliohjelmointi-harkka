package com.example.mobiiliohjelmointi_lopputy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class QuizQuestion {
    private String mCorrectAnswer;
    private String[] mWrongAnswers;
    private String mQuestion;
    private boolean mIsTrueFalseQuestion;

    public QuizQuestion(String question, String correctAnswer, String[] wrongAnswers) {
        mQuestion = question;
        mCorrectAnswer = correctAnswer;
        mWrongAnswers = wrongAnswers;

        if (mWrongAnswers.length > 1) {
            mIsTrueFalseQuestion = false;
        } else {
            mIsTrueFalseQuestion = true;
        }
    }

    public boolean isTrueFalseQuestion() {
        return mIsTrueFalseQuestion;
    }

    public String getmCorrectAnswer() {
        return mCorrectAnswer;
    }

    public String[] getAllAnswersMixed() {
        // convert to list and shuffle and return string[]
        ArrayList<String> allAnswers = new ArrayList<>();

        allAnswers.addAll(Arrays.asList(mWrongAnswers));
        allAnswers.add(mCorrectAnswer);
        Collections.shuffle(allAnswers);

        return allAnswers.toArray(new String[allAnswers.size()]);
    }

    public String getmQuestion() {
        return mQuestion;
    }
}
