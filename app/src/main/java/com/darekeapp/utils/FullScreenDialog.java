package com.darekeapp.utils;

import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.darekeapp.R;
import com.darekeapp.database.ShiftLog;
import com.darekeapp.database.ShiftLogDatabase;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class FullScreenDialog extends DialogFragment {
    private Toolbar toolbar;

    private ShiftLogDatabase db;

    private EditText companyName;
    private SwitchCompat workedForAgent;
    private EditText agentName;
    private SingleDateAndTimePicker shiftStart;
    private SingleDateAndTimePicker shiftEnd;
    private SwitchCompat breakTaken;
    private SingleDateAndTimePicker breakStart;
    private SingleDateAndTimePicker breakEnd;
    private SwitchCompat isTransportJob;
    private EditText transportCompanyName;
    private EditText vehicleRegistration;

    public static void display(FragmentManager fragmentManager) {
        FullScreenDialog fullScreenDialog = new FullScreenDialog();
        fullScreenDialog.show(fragmentManager, "fullscreen_dialog");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.full_screen_dialog_layout,
                container, false);
        toolbar = view.findViewById(R.id.toolbar);

        companyName = view.findViewById(R.id.company_name);
        workedForAgent = view.findViewById(R.id.worked_for_agent);
        agentName = view.findViewById(R.id.agent_name);
        shiftStart = view.findViewById(R.id.shift_start_time);
        shiftEnd = view.findViewById(R.id.shift_end_time);
        breakTaken = view.findViewById(R.id.break_taken);
        breakStart = view.findViewById(R.id.break_start_time);
        breakEnd = view.findViewById(R.id.break_end_time);
        isTransportJob = view.findViewById(R.id.transport_job);
        transportCompanyName = view.findViewById(R.id.transport_company_name);
        vehicleRegistration = view.findViewById(R.id.vehicle_registration);

        shiftStart.setIsAmPm(false);
        shiftEnd.setIsAmPm(false);
        breakStart.setIsAmPm(false);
        breakEnd.setIsAmPm(false);

        shiftStart.setStepMinutes(1);
        shiftEnd.setStepMinutes(1);
        breakStart.setStepMinutes(1);
        breakEnd.setStepMinutes(1);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenDialog.this.dismiss();
            }
        });
        toolbar.inflateMenu(R.menu.full_screen_dialog_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (fieldsValid()) {
                    showMessage("Test message passed");
                    db = Room.databaseBuilder(getContext(),
                            ShiftLogDatabase.class,
                            "ShiftLogDatabase").build();
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            ShiftLog.Builder shiftLogBuilder = new ShiftLog.Builder();
                            // Save user inputs into database.
                            shiftLogBuilder.setCompanyName(companyName.getText().toString());
                            shiftLogBuilder.setWorkedForAgent(workedForAgent.isChecked());
                            if (!workedForAgent.isChecked()) {
                                shiftLogBuilder.setAgentName(null);
                            } else {
                                shiftLogBuilder.setAgentName(agentName.getText().toString());
                            }
                            shiftLogBuilder.setShiftStart(shiftStart.getDate());
                            shiftLogBuilder.setShiftEnd(shiftEnd.getDate());
                            shiftLogBuilder.setBreakTaken(breakTaken.isChecked());
                            if (!breakTaken.isChecked()) {
                                shiftLogBuilder.setBreakStart(null);
                                shiftLogBuilder.setBreakEnd(null);
                            } else {
                                shiftLogBuilder.setBreakStart(breakStart.getDate());
                                shiftLogBuilder.setBreakEnd(breakEnd.getDate());
                            }
                            shiftLogBuilder.setTransportJob(isTransportJob.isChecked());
                            if (!isTransportJob.isChecked()) {
                                shiftLogBuilder.setTransportCompanyName(null);
                                shiftLogBuilder.setVehicleRegistration(null);
                            } else {
                                shiftLogBuilder.setTransportCompanyName(
                                        transportCompanyName.getText().toString());
                                shiftLogBuilder.setVehicleRegistration(
                                        vehicleRegistration.getText().toString());
                            }
                            // Build the shift log object.
                            ShiftLog shiftLog = shiftLogBuilder.build();
                            // Store the current data of the database inside a `List`.
                            List<ShiftLog> currentDatabaseContent = db.shiftLogDao().getAllShiftLogs(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            // Delete all the data of the current user from the database.
                            db.shiftLogDao().deleteAllShiftLogs(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            // Add the new shift log to `currentDatabaseContent`.
                            currentDatabaseContent.add(shiftLog);
                            /*
                             * Add all of the shift logs from `currentDatabaseContent` to the
                             * database.
                             */
                            for (ShiftLog shiftLogs : currentDatabaseContent) {
                                db.shiftLogDao().insert(shiftLogs);
                            }
                            // Test message.
                            Log.d("TEST", db.shiftLogDao().getAllShiftLogs(FirebaseAuth.getInstance().getCurrentUser().getUid()).toString());
                            // Close the `FullScreenDialog`.
                            FullScreenDialog.this.dismiss();
                        }
                    });
                }
                return true;
            }
        });
    }

    private boolean fieldsValid() {
        if (companyName.getText().toString().isEmpty()) {
            companyName.setBackgroundResource(R.drawable.invalid_edt);
            showMessage("Fill out the highlighted boxes");
            return false;
        } else {
            companyName.setBackgroundResource(R.drawable.round_edt);
        }

        if (workedForAgent.isChecked() && agentName.getText().toString().isEmpty()) {
            agentName.setBackgroundResource(R.drawable.invalid_edt);
            showMessage("Fill out the highlighted boxes");
            return false;
        } else {
            agentName.setBackgroundResource(R.drawable.round_edt);
        }

        if ((shiftStart.getDate().after(shiftEnd.getDate())) ||
                (shiftStart.getDate().after(shiftEnd.getDate()) &&
                        shiftStart.getDate().getTime() <= shiftEnd.getDate().getTime())) {
            showMessage("Invalid shift date or time");
            return false;
        }

        if (breakTaken.isChecked() &&
                (breakStart.getDate().before(shiftStart.getDate())) ||
                (breakStart.getDate().after(shiftEnd.getDate())) ||
                (breakStart.getDate().after(breakEnd.getDate()) &&
                        breakStart.getDate().getTime() <= breakEnd.getDate().getTime())) {
            showMessage("Invalid break date or time");
            return false;
        }

        if (isTransportJob.isChecked() && transportCompanyName.getText().toString().isEmpty()) {
            transportCompanyName.setBackgroundResource(R.drawable.invalid_edt);
            showMessage("Fill out the highlighted boxes");
            return false;
        } else {
            transportCompanyName.setBackgroundResource(R.drawable.round_edt);
        }

        if (isTransportJob.isChecked() && vehicleRegistration.getText().toString().isEmpty()) {
            vehicleRegistration.setBackgroundResource(R.drawable.invalid_edt);
            showMessage("Fill out the highlighted boxes");
            return false;
        } else {
            vehicleRegistration.setBackgroundResource(R.drawable.round_edt);
        }

        return true;
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
