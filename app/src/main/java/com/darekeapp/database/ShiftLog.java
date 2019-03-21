package com.darekeapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;

import java.util.Date;


@Entity
public class ShiftLog {
    @PrimaryKey
    @ColumnInfo(name = "user_uid")
    private int userUid;

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

    @ColumnInfo(name = "is_transport_company")
    private boolean isTransportCompany;

    @ColumnInfo(name = "transport_company_name")
    private String transportCompanyName;

    @ColumnInfo(name = "vehicle_registration")
    private String vehicleRegistration;

    public ShiftLog(int userUid, String companyName, boolean workedForAgent, String agentName,
                    Date shiftStart, Date shiftEnd,
                    boolean breakTaken, Date breakStart,
                    Date breakEnd, boolean isTransportCompany,
                    String transportCompanyName, String vehicleRegistration) {
        this.userUid = userUid;
        this.companyName = companyName;
        this.workedForAgent = workedForAgent;
        this.agentName = agentName;
        this.shiftStart = shiftStart;
        this.shiftEnd = shiftEnd;
        this.breakTaken = breakTaken;
        this.breakStart = breakStart;
        this.breakEnd = breakEnd;
        this.isTransportCompany = isTransportCompany;
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
                ", isTransportCompany=" + isTransportCompany +
                ", transportCompanyName='" + transportCompanyName + '\'' +
                ", vehicleRegistration='" + vehicleRegistration + '\'' +
                '}';
    }

    public int getUserUid() {
        return userUid;
    }

    public void setUserUid(int userUid) {
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

    public boolean isTransportCompany() {
        return isTransportCompany;
    }

    public void setTransportCompany(boolean transportCompany) {
        isTransportCompany = transportCompany;
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
