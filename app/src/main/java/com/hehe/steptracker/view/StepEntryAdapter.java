package com.hehe.steptracker.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hehe.steptracker.R;
import com.hehe.steptracker.model.entity.StepEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class StepEntryAdapter extends RecyclerView.Adapter<StepEntryAdapter.StepEntryViewHolder> {

    private List<StepEntry> stepEntries = new ArrayList<>();

    public StepEntryAdapter(List<StepEntry> stepEntries) {
        this.stepEntries = stepEntries;
        notifyDataSetChanged();
    }

    public void updateRV(List<StepEntry> stepEntries){
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
    }

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
    }

}