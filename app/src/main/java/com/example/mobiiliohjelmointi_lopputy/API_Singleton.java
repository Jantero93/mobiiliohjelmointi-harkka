package com.example.mobiiliohjelmointi_lopputy;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class API_Singleton {
    private static API_Singleton instance = null;
    private RequestQueue requestQueue;
    private static Context ctx;

    private final String ALL_CATEGORIES_URL = "https://opentdb.com/api_category.php";


    // for settings activity
    private HashMap<String, Integer> m_categories = new HashMap<>();
    private boolean categoriesDownloaded = false;

    // for game activity
    private ArrayList<QuizQuestion> m_GameData = new ArrayList<>();
    private boolean gameDataDownloaded = false;
    private boolean gameDataResponseCodeOK = false;

    public static synchronized API_Singleton getInstance(Context context) {
        if (instance == null) {
            instance = new API_Singleton(context);
        }
        return instance;
    }

    private API_Singleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    // all categories hash map for settings activity
    public void getAllCategoriesData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_CATEGORIES_URL,
                response -> {
                    parseJSONCategories(response);
                },
                error -> {
                    Toast.makeText(ctx, "Error on downloading categories", Toast.LENGTH_LONG).show();
                }
        );
        if (requestQueue != null) {
            API_Singleton.getInstance(ctx).addToRequestQueue(stringRequest);
        }
    }

    private void parseJSONCategories(String JSONresponse) {

        // parse JSON to hash map, where name key and value correspond id
        try {
            JSONObject root = new JSONObject(JSONresponse);
            JSONArray trivia_categories = root.getJSONArray("trivia_categories");

            for (int i = 0; i < trivia_categories.length(); i++) {
                JSONObject category = trivia_categories.getJSONObject(i);
                m_categories.put(category.getString("name"), category.getInt("id"));
            }

            categoriesDownloaded = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getGameData(String customGameApiLink) {
        // remove previous questions on new api call
        m_GameData.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, customGameApiLink,
                response -> {
                    parseJSONGameData(response);
                },
                error -> {
                    Toast.makeText(ctx, "Error on downloading categories", Toast.LENGTH_LONG).show();
                }
        );
        if (requestQueue != null) {
            API_Singleton.getInstance(ctx).addToRequestQueue(stringRequest);
        }
    }

    private void parseJSONGameData(String response){
        try {
            JSONObject root = new JSONObject(response);

            if (root.getInt("response_code") != 0)
                gameDataResponseCodeOK = false;
            else
                gameDataResponseCodeOK = true;


            if (gameDataResponseCodeOK){
                JSONArray questionsArray = root.getJSONArray("results");
                JSONObject question = questionsArray.getJSONObject(0);

                for (int i = 0; i < questionsArray.length(); i++) {
                    parseQuestionAndAddToList(questionsArray.getJSONObject(i));
                }

            }
            gameDataDownloaded = true;
        }   catch (JSONException e) {
            e.printStackTrace();
        }
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

            m_GameData.add(new QuizQuestion(question, correctAnswer, incorrect_answers));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // getters
    public boolean isCategoriesDownloaded(){ return categoriesDownloaded; }
    public boolean isGameDataDownloaded() { return gameDataDownloaded; }

    public boolean isGameDataResponseCodeOK(){ return gameDataResponseCodeOK; }
    public HashMap<String, Integer> getM_categories(){ return m_categories; }

    public ArrayList<QuizQuestion> getM_GameData(){ return m_GameData; }

}
