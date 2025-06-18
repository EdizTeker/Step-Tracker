package com.hehe.steptracker.manager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class StepSensorManager implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepCounterSensor;

    // Sensörden gelen toplam değeri tutan MutableLiveData
    private final MutableLiveData<Integer> totalSteps = new MutableLiveData<>();
    public LiveData<Integer> getCurrentSensorTotalSteps() {return totalSteps;}
    public StepSensorManager(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (stepCounterSensor == null) {
                Log.e("StepSensorManager", "TYPE_STEP_COUNTER sensor not found.");
            }
        } else {
            Log.e("StepSensorManager", "SensorManager not available.");
        }
    }

    public void startListening() {
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
            Log.d("StepSensorManager", "STEP_COUNTER sensor registered.");
        } else {
            // Sensör yoksa hata veya uyarı verebilirsin
            totalSteps.postValue(-1); // Hata durumu için bir değer
        }
    }

    public void stopListening() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            Log.d("StepSensorManager", "STEP_COUNTER sensor unregistered.");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            // Sensörden gelen değeri LiveData'ya post et
            totalSteps.postValue((int) event.values[0]);
            Log.d("Step Counter", "Sensor event received: " + event.values[0]);
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Hassasiyet değişikliklerini yönet
    }
}