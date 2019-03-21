package com.darekeapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.darekeapp.utils.Converter;

@Database(entities = {ShiftLog.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class ShiftLogDatabase extends RoomDatabase {
    public abstract ShiftLogDao shiftLogDao();
}
