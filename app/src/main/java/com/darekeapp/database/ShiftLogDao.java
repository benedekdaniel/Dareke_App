package com.darekeapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

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
     * Gets all the companies the user has previously worked for.
     * @param userUid the unique ID of the user
     * @return all companies for user has previously worked for.
     */
    @Query("SELECT company_name FROM ShiftLog WHERE user_uid = :userUid")
    List<String> getAllCompanies(String userUid);

    /**
     * Inserts one or more new `ShiftLog` objects.
     * @param shiftLog the shift log to be inserted.
     */
    @Insert(onConflict = REPLACE)
    void insertShiftLogs(ShiftLog... shiftLog);

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
    void deleteAllShiftLogs(String userUid);
}
