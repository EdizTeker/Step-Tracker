package com.hehe.steptracker.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface StepEntryDao {
    @Query("SELECT * FROM stepentry")
    List<StepEntry> getAllStepEntry();
    @Query("SELECT * FROM stepentry WHERE date = :date")
    StepEntry getStepEntryFromDate(Date date);
    @Insert
    void insertStepEntry(StepEntry stepEntry);
    @Update
    void updateStepEntry(StepEntry stepEntry);


}
