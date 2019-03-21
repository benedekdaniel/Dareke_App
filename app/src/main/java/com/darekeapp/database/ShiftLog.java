package com.darekeapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

@Entity
public class ShiftLog {
    @PrimaryKey
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

    @ColumnInfo(name = "is_transport_job")
    private boolean isTransportJob;

    @ColumnInfo(name = "transport_company_name")
    private String transportCompanyName;

    @ColumnInfo(name = "vehicle_registration")
    private String vehicleRegistration;

    public ShiftLog(String companyName, boolean workedForAgent, String agentName,
                    Date shiftStart, Date shiftEnd,
                    boolean breakTaken, Date breakStart,
                    Date breakEnd, boolean isTransportJob,
                    String transportCompanyName, String vehicleRegistration) {
        this.userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.companyName = companyName;
        this.workedForAgent = workedForAgent;
        this.agentName = agentName;
        this.shiftStart = shiftStart;
        this.shiftEnd = shiftEnd;
        this.breakTaken = breakTaken;
        this.breakStart = breakStart;
        this.breakEnd = breakEnd;
        this.isTransportJob = isTransportJob;
        this.transportCompanyName = transportCompanyName;
        this.vehicleRegistration = vehicleRegistration;
    }

    @Override
    public String toString() {
        return "ShiftLog{" +
                "userUid=" + userUid +
                ", companyName='" + companyName + '\'' +
                ", workedForAgent=" + workedForAgent +
                ", agentName='" + agentName + '\'' +
                ", shiftStart=" + shiftStart +
                ", shiftEnd=" + shiftEnd +
                ", breakTaken=" + breakTaken +
                ", breakStart=" + breakStart +
                ", breakEnd=" + breakEnd +
                ", isTransportCompany=" + isTransportJob +
                ", transportCompanyName='" + transportCompanyName + '\'' +
                ", vehicleRegistration='" + vehicleRegistration + '\'' +
                '}';
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
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

    public boolean isTransportJob() {
        return isTransportJob;
    }

    public void setTransportJob(boolean transportJob) {
        isTransportJob = transportJob;
    }

    public String getTransportCompanyName() {
        return transportCompanyName;
    }

    public void setTransportCompanyName(String transportCompanyName) {
        this.transportCompanyName = transportCompanyName;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }
}
