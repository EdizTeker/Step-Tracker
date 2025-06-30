package com.hehe.steptracker.model.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.hehe.steptracker.model.database.AppDatabase;
import com.hehe.steptracker.model.database.StepEntryDao;
import com.hehe.steptracker.model.entity.StepEntry;
import com.hehe.steptracker.model.preferences.SharedPreferencesManager;

import java.util.Date;
import java.util.List;

public class StepEntryRepository {
    private StepEntryDao stepEntryDao;
    private LiveData<List<StepEntry>> allStepEntries;
    private StepEntry currentStepEntry;
    private SharedPreferencesManager sharedPreferencesManager;

    public StepEntryRepository(Application application){
        AppDatabase database = AppDatabase.getDatabase(application);
        stepEntryDao = database.stepEntryDao();
        Date currentTime = new Date();
        currentStepEntry = stepEntryDao.getStepEntryFromDate(currentTime);
        sharedPreferencesManager = new SharedPreferencesManager(application);
        allStepEntries = stepEntryDao.getAllStepEntry();


    }
    //Room Database
    public LiveData<List<StepEntry>> getAllStepEntries(){
        return stepEntryDao.getAllStepEntry();
    }
    public StepEntry getStep(){
        return currentStepEntry;
    }
    public void insert(StepEntry stepEntry) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            stepEntryDao.insertStepEntry(stepEntry);
        });
    }

    //Shared Preferences
    public Integer getStepCount() {
        return sharedPreferencesManager.getInt("step_count", 0);
    }
    public void setStepCount(int step) {
        sharedPreferencesManager.saveInt("step_count", step);
        Log.d("Step Counter", "setStepCountRepo: " + step);
    }

    public void setIsRecording(boolean isRecording) {
        sharedPreferencesManager.saveBoolean("is_recording", isRecording);
    }

    public boolean getIsRecording() {
        return sharedPreferencesManager.getBoolean("is_recording", false);
    }

}
