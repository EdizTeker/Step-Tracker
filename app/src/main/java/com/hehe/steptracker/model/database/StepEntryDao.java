package com.hehe.steptracker.model.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hehe.steptracker.model.entity.StepEntry;

import java.util.Date;
import java.util.List;

@Dao
public interface StepEntryDao {
    @Query("SELECT * FROM stepentry ORDER BY date DESC")
    LiveData<List<StepEntry>> getAllStepEntry();
    @Query("SELECT * FROM stepentry WHERE date = :date")
    StepEntry getStepEntryFromDate(Date date);
    @Insert
    void insertStepEntry(StepEntry stepEntry);
    @Update
    void updateStepEntry(StepEntry stepEntry);


}
