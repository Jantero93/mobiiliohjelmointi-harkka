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

import java.util.HashMap;

public class API_Singleton {
    private static API_Singleton instance = null;
    private RequestQueue requestQueue;
    private static Context ctx;

    private String M_apiLink = "https://opentdb.com/api.php?";
    private final String ALL_CATEGORIES_URL = "https://opentdb.com/api_category.php";

    private HashMap<String, Integer> m_categories;

    private boolean categoriesDownloaded = false;

    public static synchronized API_Singleton getInstance(Context context) {
        if (instance == null) {
            instance = new API_Singleton(context);
        }
        return instance;
    }


    private API_Singleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

        m_categories = new HashMap<>();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

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

    public boolean isCategoriesDownloaded(){
        return categoriesDownloaded;
    }

    public HashMap<String, Integer> getM_categories(){
        return m_categories;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
