package com.darekeapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

@Entity
public class ShiftLog {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "shift_log_id")
    private int shiftLogId;

    @NonNull
    @ColumnInfo(name = "user_uid")
    private String userUid;

    @ColumnInfo(name = "company_name")
    private String companyName;

    @ColumnInfo(name = "worked_for_agent")
    private boolean workedForAgent;

    @ColumnInfo(name = "agent_name")
    private String agentName;

    @ColumnInfo(name = "shift_start")
    private Date shiftStart;

    @ColumnInfo(name = "shift_end")
    private Date shiftEnd;

    @ColumnInfo(name = "break_taken")
    private boolean breakTaken;

    @ColumnInfo(name = "break_start")
    private Date breakStart;

    @ColumnInfo(name = "break_end")
    private Date breakEnd;

    @ColumnInfo(name = "governed_by_driver_hours")
    private boolean governedByDriverHours;

    @ColumnInfo(name = "vehicle_registration")
    private String vehicleRegistration;

    @ColumnInfo(name = "poa_time")
    private Long poaTime;

    private ShiftLog() {
        this.userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public ShiftLog(String companyName, boolean workedForAgent, String agentName, Date shiftStart,
                    Date shiftEnd, boolean breakTaken, Date breakStart, Date breakEnd,
                    boolean governedByDriverHours, String vehicleRegistration, Long poaTime) {
        this();
        this.companyName = companyName;
        this.workedForAgent = workedForAgent;
        this.agentName = agentName;
        this.shiftStart = shiftStart;
        this.shiftEnd = shiftEnd;
        this.breakTaken = breakTaken;
        this.breakStart = breakStart;
        this.breakEnd = breakEnd;
        this.governedByDriverHours = governedByDriverHours;
        this.vehicleRegistration = vehicleRegistration;
        this.poaTime = poaTime;
    }

    public static class Builder {
        private final ShiftLog shiftLog = new ShiftLog();

        public Builder setCompanyName(String companyName) {
            shiftLog.companyName = companyName;
            return this;
        }

        public Builder setWorkedForAgent(boolean workedForAgent) {
            shiftLog.workedForAgent = workedForAgent;
            return this;
        }

        public Builder setAgentName(String agentName) {
            shiftLog.agentName = agentName;
            return this;
        }

        public Builder setShiftStart(Date shiftStart) {
            shiftLog.shiftStart = shiftStart;
            return this;
        }

        public Builder setShiftEnd(Date shiftEnd) {
            shiftLog.shiftEnd = shiftEnd;
            return this;
        }

        public Builder setBreakTaken(boolean breakTaken) {
            shiftLog.breakTaken = breakTaken;
            return this;
        }

        public Builder setBreakStart(Date breakStart) {
            shiftLog.breakStart = breakStart;
            return this;
        }

        public Builder setBreakEnd(Date breakEnd) {
            shiftLog.breakEnd = breakEnd;
            return this;
        }

        public Builder setGovernedByDriverHours(boolean governedByDriverHours) {
            shiftLog.governedByDriverHours = governedByDriverHours;
            return this;
        }

        public Builder setVehicleRegistration(String vehicleRegistration) {
            shiftLog.vehicleRegistration = vehicleRegistration;
            return this;
        }

        public Builder setPoaTime(Long poaTime) {
            shiftLog.poaTime = poaTime;
            return this;
        }

        public ShiftLog build() {
            return shiftLog;
        }
    }

    @Override
    public String toString() {
        return "ShiftLog{" +
                "shiftLogId=" + shiftLogId +
                ", userUid='" + userUid + '\'' +
                ", companyName='" + companyName + '\'' +
                ", workedForAgent=" + workedForAgent +
                ", agentName='" + agentName + '\'' +
                ", shiftStart=" + shiftStart +
                ", shiftEnd=" + shiftEnd +
                ", breakTaken=" + breakTaken +
                ", breakStart=" + breakStart +
                ", breakEnd=" + breakEnd +
                ", governedByDriverHours=" + governedByDriverHours +
                ", vehicleRegistration='" + vehicleRegistration + '\'' +
                ", poaTime=" + poaTime +
                '}';
    }

    public int getShiftLogId() {
        return shiftLogId;
    }

    public void setShiftLogId(int shiftLogId) {
        this.shiftLogId = shiftLogId;
    }

    @NonNull
    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(@NonNull String userUid) {
        this.userUid = userUid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isWorkedForAgent() {
        return workedForAgent;
    }

    public void setWorkedForAgent(boolean workedForAgent) {
        this.workedForAgent = workedForAgent;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Date getShiftStart() {
        return shiftStart;
    }

    public void setShiftStart(Date shiftStart) {
        this.shiftStart = shiftStart;
    }

    public Date getShiftEnd() {
        return shiftEnd;
    }

    public void setShiftEnd(Date shiftEnd) {
        this.shiftEnd = shiftEnd;
    }

    public boolean isBreakTaken() {
        return breakTaken;
    }

    public void setBreakTaken(boolean breakTaken) {
        this.breakTaken = breakTaken;
    }

    public Date getBreakStart() {
        return breakStart;
    }

    public void setBreakStart(Date breakStart) {
        this.breakStart = breakStart;
    }

    public Date getBreakEnd() {
        return breakEnd;
    }

    public void setBreakEnd(Date breakEnd) {
        this.breakEnd = breakEnd;
    }

    public boolean isGovernedByDriverHours() {
        return governedByDriverHours;
    }

    public void setGovernedByDriverHours(boolean governedByDriverHours) {
        this.governedByDriverHours = governedByDriverHours;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public Long getPoaTime() {
        return poaTime;
    }

    public void setPoaTime(Long poaTime) {
        this.poaTime = poaTime;
    }
}
