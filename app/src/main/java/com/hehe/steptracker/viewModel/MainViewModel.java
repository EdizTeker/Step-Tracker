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

public class MainViewModel extends AndroidViewModel{


    private MutableLiveData<String> currentStepsForDisplay;
    private int currentSteps;
    private StepSensorManager stepSensorManager;
    private StepEntryRepository repository;
    private int initialStepValue = 0;
    private boolean isInitialStepValueTaken = false;
    private boolean isRecording = false;


    public MainViewModel(Application application) { //View model constructor
        super(application);
        repository = new StepEntryRepository(application);
        stepSensorManager = new StepSensorManager(application);
        currentStepsForDisplay = new MutableLiveData<>();
        currentStepsForDisplay.setValue("");
    }

    public LiveData<String> getStepForActivity(){ //Sends step count to activity
        return currentStepsForDisplay;
    }

    public void startRecording(){

        stepSensorManager.startListening();
        stepSensorManager.getCurrentSensorTotalSteps().observeForever(totalSteps ->{
            if(!isInitialStepValueTaken){
                initialStepValue = totalSteps;
                isInitialStepValueTaken = true;
            }
            currentSteps = totalSteps-initialStepValue;
            Log.d("Step Counter", "Sensor event received: " + initialStepValue);
            currentStepsForDisplay.setValue(String.valueOf(currentSteps));
        });
    }
    public void finishRecording() { //Records the step count
        stepSensorManager.stopListening();
        isInitialStepValueTaken = false;
        saveToDB();
        currentStepsForDisplay.setValue("");
    }

    public void saveToDB(){
        StepEntry stepEntry = new StepEntry();
        stepEntry.date = new Date();
        stepEntry.stepCount = currentSteps;
        stepEntry.title = "deneme";
        repository.insert(stepEntry);
        Log.d("Step Counter", "Database action");

    }

}
