package com.darekeapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ShiftLogDao {
    /**
     * Gets all the shift logs for the specific user.
     * @param userUid the unique ID of the user
     * @return all shift logs for the specific user
     */
    @Query("SELECT * FROM ShiftLog WHERE user_uid = :userUid")
    List<ShiftLog> getAllShiftLogs(String userUid);

    /**
     * Inserts one or more new `ShiftLog` objects.
     * @param shiftLog the shift log to be inserted.
     */
    @Insert
    void insert(ShiftLog... shiftLog);

    /**
     * Deletes a particular shift log.
     * @param shiftLog the shift log to be deleted
     */
    @Delete
    void deleteShiftLog(ShiftLog shiftLog);

    /**
     * Deletes all shift logs for the specific user.
     * @param userUid the unique ID of the user
     */
    @Query("DELETE FROM ShiftLog WHERE user_uid = :userUid")
    void deleteAllShiftLogs(int userUid);
}
