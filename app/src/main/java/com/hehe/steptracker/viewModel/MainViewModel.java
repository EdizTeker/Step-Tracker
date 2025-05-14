package com.hehe.steptracker.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.hehe.steptracker.model.StepsModel;

public class MainViewModel extends ViewModel {


    private MutableLiveData<String> stepCount;
    public MainViewModel() {
        stepCount = new MutableLiveData<>();
        stepCount.setValue("deneme");
    }
    
    public LiveData<String> getStepForActivity(){
        return stepCount;
    }
}
