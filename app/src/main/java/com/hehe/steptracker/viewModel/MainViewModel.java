package com.hehe.steptracker.viewModel;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Button;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehe.steptracker.model.StepEntry;
import com.hehe.steptracker.model.StepEntryRepository;

import java.util.Date;
import java.util.List;

public class MainViewModel extends AndroidViewModel implements SensorEventListener {


    private MutableLiveData<String> stepCount;
    private List<StepEntry> allSteps;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private StepEntryRepository repository;
    private float totalSteps = 0;
    private float initialStepValue = 0;
    private boolean isRecording = false;

    public MainViewModel(Application application) { //View model constructor
        super(application);
        stepCount = new MutableLiveData<>();
        repository = new StepEntryRepository(application);
    }

    public LiveData<String> getStepForActivity(){ //Sends step count to activity
        return stepCount;
    }


    public void initializeSensor(){ //Initializes the sensor and sensor manager
        sensorManager = (SensorManager) getApplication().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (stepSensor != null) {
                Log.d("Step Counter", "Step Sensor Found and Initialized.");
            } else {
                Log.e("Step Counter", "Step Sensor is not found.");
            }
        } else {
            Log.e("Step Counter", "SensorManager is not found.");
        }
    }
    public void registerSensor() { //Registers the sensor
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);

            Log.d("Step Counter", "Step Sensor registered");
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) { //Updates the step count
        if (event == null) return;
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (initialStepValue == 0) {initialStepValue = event.values[0];}
            if (initialStepValue != 0){
                totalSteps = event.values[0] - initialStepValue;
                stepCount.setValue(String.valueOf(totalSteps));
            }
        }
        Log.d("Step Counter", "Sensor event received: " + event.values[0]);
    }

    public void recordSteps() { //Records the step count
        if(!isRecording){
            initializeSensor();
            registerSensor();
            isRecording = true;

        }else{
            unregisterSensor();
            saveToDB();
            totalSteps = 0;
            initialStepValue = 0;
            stepCount.setValue("");
            isRecording = false;
        }

    }

    public void unregisterSensor() { //Unregisters the sensor
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            Log.d("Step Counter", "Step Sensor unregistered");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    protected void onCleared() {

        super.onCleared();
        unregisterSensor();

    }

    public void saveToDB(){
        StepEntry stepEntry = new StepEntry();
        stepEntry.date = new Date();
        stepEntry.stepCount = Math.round(totalSteps);
        stepEntry.title = "deneme";
        repository.insert(stepEntry);
        Log.d("Step Counter", "Database action");

    }

}
