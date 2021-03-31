package com.example.mobiiliohjelmointi_lopputy;

public class DataAPI {
    private static DataAPI single_instance = null;

    private String M_apiLink = "https://opentdb.com/api.php?";


    private DataAPI(){

    }

    public static DataAPI getInstance() {
        if (single_instance == null) {
            single_instance = new DataAPI();
        }
        return single_instance;
    }


    public String[] getAllCategorysAPI() {
        String[] test = {"nmu","test"};

        return test;
    }

    public String getApiLink(){
        return M_apiLink;
    }



}
