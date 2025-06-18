package com.hehe.steptracker.model.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private SharedPreferences sharedPreferences;

    public void SharedPreferences(Context context){
        sharedPreferences = context.getSharedPreferences("Recording",Context.MODE_PRIVATE);
    }
    public void saveInt(String key, Integer value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public Integer getInt(String key, Integer defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void saveBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }
}
