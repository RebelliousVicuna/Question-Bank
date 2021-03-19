package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class Save {
    public static void saveUserInfo(Context context, String time, String single, String judge,String mutichoice) {
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("time", time);
        edit.putString("single", single);
        edit.putString("judge", judge);
        edit.putString("mutichoice", mutichoice);
        edit.apply();
    }

    public static Map<String, String> getUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String time = sp.getString("time", null);
        String single = sp.getString("single", null);
        String judge = sp.getString("judge", null);
        String mutichoice = sp.getString("mutichoice", null);
        Map<String, String> userMap = new HashMap<String, String>();
        userMap.put("time", time);
        userMap.put("single", single);
        userMap.put("judge", judge);
        userMap.put("mutichoice", mutichoice);
        return userMap;
    }
}

