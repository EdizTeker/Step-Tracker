package com.hehe.steptracker.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hehe.steptracker.manager.StepSensorManager;
import com.hehe.steptracker.model.entity.StepEntry;
import com.hehe.steptracker.model.repository.StepEntryRepository;

import java.util.Date;
import java.util.List;

public class MainViewModel extends AndroidViewModel{


    private MutableLiveData<String> currentStepsForDisplay;
    private int currentSteps;
    private StepSensorManager stepSensorManager;
    private StepEntryRepository repository;
    private int initialStepValue = 0;
    private boolean isInitialStepValueTaken = false;
    private boolean isRecording;
    private int stepBeforeShutdown;
    private String stepEntryTitle;


    public MainViewModel(Application application) { //View model constructor
        super(application);
        repository = new StepEntryRepository(application);
        stepSensorManager = new StepSensorManager(application);
        currentStepsForDisplay = new MutableLiveData<>();
        currentStepsForDisplay.setValue("");
        isRecording = repository.getIsRecording();
        if(isRecording){
            isInitialStepValueTaken = false;
            stepBeforeShutdown = repository.getStepCount();
            startRecording();
            Log.d("Step Counter", "stepBeforeShutdown: " + stepBeforeShutdown);
        }else{
            setStepBeforeShutdownForActivity(0);
            stepBeforeShutdown = 0;
        }
    }

    public List<StepEntry> getAllStepsForActivity(){return repository.getAllStepEntries();}
    public LiveData<String> getStepForActivity(){return currentStepsForDisplay;}
    public boolean getIsRecordingForActivity(){return isRecording;}
    public void getStepBeforeShutdownForActivity(){currentSteps = stepBeforeShutdown;}
    public void setIsRecordingForModel(boolean isRecording){repository.setIsRecording(isRecording);}
    public void setStepBeforeShutdownForActivity(int steps){if(steps == 1){repository.setStepCount(currentSteps);}else{repository.setStepCount(steps);}}

    public void startRecording(){

        stepSensorManager.startListening();
        stepSensorManager.getCurrentSensorTotalSteps().observeForever(totalSteps ->{
            if(!isInitialStepValueTaken){
                isInitialStepValueTaken = true;
                initialStepValue = totalSteps - stepBeforeShutdown;
            }
            currentSteps = totalSteps-initialStepValue;
            Log.d("Step Counter", "Sensor event received: " + initialStepValue);
            currentStepsForDisplay.setValue(String.valueOf(currentSteps));
        });
    }
    public void finishRecording(String title) { //Records the step count
        stepSensorManager.stopListening();
        isInitialStepValueTaken = false;
        stepEntryTitle = title;
        saveToDB();
        currentStepsForDisplay.setValue("");
        setStepBeforeShutdownForActivity(0);
        stepBeforeShutdown = 0;
    }

    public void saveToDB(){
        Log.d("Step Tracker", "title = " + stepEntryTitle);
        StepEntry stepEntry = new StepEntry();
        stepEntry.date = new Date();
        stepEntry.stepCount = currentSteps;
        stepEntry.title= stepEntryTitle;
        repository.insert(stepEntry);
        Log.d("Step Counter", "Database action");

    }

}
