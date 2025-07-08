package com.hehe.steptracker.view;

import static androidx.core.content.ContextCompat.getString;

import android.content.Context;
import android.graphics.Color;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hehe.steptracker.R;
import com.hehe.steptracker.manager.StepSensorManager;
import com.hehe.steptracker.model.entity.StepEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class StepEntryAdapter extends RecyclerView.Adapter<StepEntryAdapter.StepEntryViewHolder> {

    private List<StepEntry> stepEntries = new ArrayList<>();
    private ActionMode actionMode;
    private List<StepEntry> selectedStepEntries = new ArrayList<>();
    private MainActivity mainActivity;
    Context context;

    public StepEntryAdapter(MainActivity mainActivity, Context context, List<StepEntry> stepEntries) {
        this.context = context;
        this.mainActivity = mainActivity;
        this.stepEntries = stepEntries;
        notifyDataSetChanged();
    }

    public void setStepEntries(List<StepEntry> stepEntries) {
        this.stepEntries = stepEntries;
    }

    @NonNull
    @Override
    public StepEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_row, parent, false);
        return new StepEntryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StepEntryViewHolder holder, int position) {
        StepEntry currentEntry = stepEntries.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        holder.listDateTxt.setText("" + sdf.format(currentEntry.date));
        holder.listStepTxt.setText("" + currentEntry.stepCount);
        holder.listTitleTxt.setText("" + currentEntry.title);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionMode != null) {
                    toggleSelection(currentEntry);
                }else{
                    Toast.makeText(context, "Total of " + Math.round(currentEntry.stepCount*0.04) + " burned.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (actionMode == null) {
                    actionMode = ((AppCompatActivity) context).startActionMode(actionModeCallback);
                }
                toggleSelection(currentEntry);
                return true;
            }
        });
        holder.setSelected(selectedStepEntries.contains(currentEntry));
    }

    private void toggleSelection(StepEntry stepEntry) {
        if (selectedStepEntries.contains(stepEntry)) {
            selectedStepEntries.remove(stepEntry);
        } else {
            selectedStepEntries.add(stepEntry);
        }
        notifyDataSetChanged();
        if (actionMode != null) {
            actionMode.setTitle(selectedStepEntries.size() + " " + getString(context.getApplicationContext(), R.string.selected));
        }
    }
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.selection_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.action_delete) {
                mainActivity.delete(selectedStepEntries);
                Toast.makeText(context, getString(context.getApplicationContext(), R.string.deleted), Toast.LENGTH_SHORT).show();
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            selectedStepEntries.clear();
            notifyDataSetChanged();
        }
    };
    @Override
    public int getItemCount() {
        return stepEntries.size();
    }

    static class StepEntryViewHolder extends RecyclerView.ViewHolder {
        private final TextView listDateTxt;
        private final TextView listStepTxt;
        private final TextView listTitleTxt;

        public StepEntryViewHolder(View itemView) {
            super(itemView);
            listDateTxt = itemView.findViewById(R.id.listDateTxt);
            listStepTxt = itemView.findViewById(R.id.listStepTxt);
            listTitleTxt = itemView.findViewById(R.id.listTitleTxt);
        }
        public void setSelected(boolean isSelected) {
            itemView.setBackgroundColor(isSelected ? Color.LTGRAY : Color.TRANSPARENT);
        }
    }

}