//package com.hehe.steptracker.model;
//
//import static android.content.Context.MODE_PRIVATE;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//
//import com.hehe.steptracker.viewModel.MainViewModel;
//
//public class StepsModel {
//
//    SharedPreferences sharedPreferences;
//
//    public StepsModel(){
//        sharedPreferences = getSharedPreferences("steps", MODE_PRIVATE);
//    }
//
//
//    public void saveStep(int steps){
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("steps", steps);
//        editor.apply();
//    }
//    public int getStepsFromModel(){
//        if (sharedPreferences == null) {return 0;}
//        else {return sharedPreferences.getInt("steps", 0);}
//
//    }
//
//
//}
