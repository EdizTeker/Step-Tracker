package com.hehe.steptracker.model.preferences;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesManager {
    private SharedPreferences sharedPreferences;

    public SharedPreferencesManager(Application application) {
        sharedPreferences = application.getSharedPreferences("Recording",Context.MODE_PRIVATE);
    }

    public void saveInt(String key, Integer value) {
        sharedPreferences.edit().putInt(key, value).commit();
        Log.d("Step Counter", "setStepCountSHARED: " + value);
    }

    public Integer getInt(String key, Integer defaultValue) {
        Log.d("Step Counter", "getStepCountSHARED: " + sharedPreferences.getInt(key, defaultValue));
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void saveBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }
}
