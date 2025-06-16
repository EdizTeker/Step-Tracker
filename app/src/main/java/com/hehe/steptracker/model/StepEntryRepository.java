package com.hehe.steptracker.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

public class StepEntryRepository {
    private StepEntryDao stepEntryDao;
    private List<StepEntry> allStepEntries;
    private StepEntry currentStepEntry;

    public StepEntryRepository(Application application){
        AppDatabase database = AppDatabase.getDatabase(application);
        stepEntryDao = database.stepEntryDao();
        allStepEntries = stepEntryDao.getAllStepEntry();
        Date currentTime = new Date();
        currentStepEntry = stepEntryDao.getStepEntryFromDate(currentTime);
    }

    public List<StepEntry> getAllStepEntries(){
        return allStepEntries;
    }
    public StepEntry getStep(){
        return currentStepEntry;
    }
    public void insert(StepEntry stepEntry) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            stepEntryDao.insertStepEntry(stepEntry);
        });
    }

}
