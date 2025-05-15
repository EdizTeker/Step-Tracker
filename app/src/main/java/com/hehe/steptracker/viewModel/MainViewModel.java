package com.hehe.steptracker.viewModel;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.hehe.steptracker.model.StepsModel;

public class MainViewModel extends AndroidViewModel implements SensorEventListener {


    private MutableLiveData<String> stepCount;
    private SensorManager sensorManager;
    private Sensor stepSensor;

    public MainViewModel(Application application) {
        super(application);
        stepCount = new MutableLiveData<>();
        initializeSensor();
        //stepCount.setValue("0");
        registerSensor();



    }
    public LiveData<String> getStepForActivity(){
        return stepCount;
    }

    public void initializeSensor(){
        // Define sensor and sensor manager
        sensorManager = (SensorManager) getApplication().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (stepSensor != null) {
                Log.d("Step Counter", "Step Sensor Found and InitialIZED.");
            } else {
                Log.e("Step Counter", "Step Sensor IS NULL (Not found on device).");
            }
        } else {
            Log.e("Step Counter", "SensorManager IS NULL.");
        }
    }
    private void registerSensor() {
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
            Log.d("Step Counter", "Step Sensor Registered");
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event == null) return;
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepCount.setValue(String.valueOf(event.values[0]));
        }
        Log.d("Step Counter", "Sensor event received: " + event.values[0]);

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
