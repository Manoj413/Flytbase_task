package com.flytbasetask.core;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;



public class AppState {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "MyTask";
    private static final String PREF_TOKEN = "token";
    private static final String PREF_USER_ID = "userId";

    private SharedPreferences mSharedPreferences;

    @Inject
    public AppState(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }



    public void login(String token, String userId) {
        mSharedPreferences.edit().putString(PREF_TOKEN, token).putString(PREF_USER_ID, userId).apply();
    }

    public boolean isLoggedIn() {
        String token = mSharedPreferences.getString(PREF_TOKEN, "");
        return !token.equals("");
    }
}




