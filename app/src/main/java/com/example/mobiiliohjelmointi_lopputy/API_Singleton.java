package com.example.mobiiliohjelmointi_lopputy;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class API_Singleton {
    private static API_Singleton instance = null;
    private RequestQueue requestQueue;
    private static Context ctx;

    private String M_apiLink = "https://opentdb.com/api.php?";


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


    private void parseJsonAllCategories(String jsonResponse ) {
        try {

            JSONObject rootObject = new JSONObject(jsonResponse);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getApiLink(){
        return M_apiLink;
    }


}
