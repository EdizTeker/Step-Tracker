package com.hehe.steptracker.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

public class StepsModel {
    SharedPreferences sharedPreferences;

    public void setSteps(int steps){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("steps", steps);
        editor.apply();
    }
    public int getSteps(){
        return sharedPreferences.getInt("steps", 0);
    }

}
