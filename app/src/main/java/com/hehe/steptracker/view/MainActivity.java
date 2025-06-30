package com.hehe.steptracker.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.content.pm.PackageManager;

import android.os.Build;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.hehe.steptracker.R;
import com.hehe.steptracker.model.entity.StepEntry;
import com.hehe.steptracker.viewModel.MainViewModel;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private TextView stepTxt;
    private MaterialButton recordBtn;
    private RecyclerView recyclerView;
    private StepEntryAdapter adapter;
    private static final int PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 100;
    private boolean isRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        permissionRequest();

        innitComponents();

        setupRecyclerView();

        viewModel.getStepForActivity().observe(this, currentStepsForDisplay -> {
            stepTxt.setText(currentStepsForDisplay);
        });

        isRecording = viewModel.getIsRecordingForActivity();
        if(isRecording){
            viewModel.getStepBeforeShutdownForActivity();
            recordBtn.setText("Finish");
            recordBtn.setIcon(ContextCompat.getDrawable(this,R.drawable.stop_button_vector));
        }else {
            recordBtn.setText("Record");
            recordBtn.setIcon(ContextCompat.getDrawable(this,R.drawable.record_button_vector));
        }

        recordBtn.setOnClickListener(v -> {
            if(!isRecording){
                recordBtn.setText("Finish");
                recordBtn.setIcon(ContextCompat.getDrawable(this,R.drawable.stop_button_vector));
                viewModel.startRecording();
                isRecording = true;
            }else {
                recordBtn.setText("Record");
                recordBtn.setIcon(ContextCompat.getDrawable(this,R.drawable.record_button_vector));
                showDialogAndSaveToDb();
                isRecording = false;
            }
            viewModel.setIsRecordingForModel(isRecording);

        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_ACTIVITY_RECOGNITION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        viewModel.setIsRecordingForModel(isRecording);
       if(isRecording){viewModel.setStepBeforeShutdownForActivity(1);}
    }

    public void innitComponents(){
        stepTxt = findViewById(R.id.stepTxt);
        recordBtn = findViewById(R.id.recordBtn);
        recyclerView = findViewById(R.id.stepRecyclerView);
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(MainViewModel.class);
    }
    private void setupRecyclerView(){
        adapter = new StepEntryAdapter(Collections.emptyList());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel.getAllStepsForActivity().observe(this, new Observer<List<StepEntry>>() {
            @Override
            public void onChanged(List<StepEntry> stepEntries) {
                adapter.setStepEntries(stepEntries);
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void permissionRequest(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(android.Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION}, PERMISSION_REQUEST_ACTIVITY_RECOGNITION);
            }
        }
    }


    public void showDialogAndSaveToDb() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter your journey's title.");

        // Creates the layout
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(50, 0, 50, 0);

        // Creates the EditText
        EditText input = new EditText(MainActivity.this);
        input.setLayoutParams(params);
        input.setHint("Enter the title here");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(input);

        builder.setView(layout);

        builder.setMessage("If you want to discard this journey please select the no option.");

        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            String inputText;
            if (input.getText().toString().trim().length() > 12) {inputText = input.getText().toString().trim().substring(0,12);}
            else {inputText = input.getText().toString().trim();}
            if (inputText.isEmpty()) {
                inputText = "Unnamed"; // Başlık boşsa varsayılan bir değer atayın
                Toast.makeText(MainActivity.this, "Exiting without a reason. Saved as 'Unnamed Journey'.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Successfully saved the journey. Journey Title: " + inputText, Toast.LENGTH_LONG).show();
            }
            // Başlığı doğrudan finishRecording metoduna gönderin
            viewModel.finishRecording(inputText);
        });

        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            Toast.makeText(MainActivity.this, "Discarded the journey.", Toast.LENGTH_SHORT).show();
            dialog.cancel();

        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}