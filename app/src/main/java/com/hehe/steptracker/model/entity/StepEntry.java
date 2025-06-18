package com.hehe.steptracker.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class StepEntry {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "date")
    public Date date;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "step_count")
    public int stepCount;
}
