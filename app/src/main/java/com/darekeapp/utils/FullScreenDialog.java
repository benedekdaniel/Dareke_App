package com.darekeapp.utils;

import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.darekeapp.R;
import com.darekeapp.database.ShiftLog;
import com.darekeapp.database.ShiftLogDatabase;
import com.darekeapp.fragments.ShiftLogsFragment;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FullScreenDialog extends DialogFragment {
    private Toolbar toolbar;

    private ShiftLogDatabase db;

    private AutoCompleteTextView companyName;
    private SwitchCompat workedForAgent;
    private AutoCompleteTextView agentName;
    private SingleDateAndTimePicker shiftStart;
    private SingleDateAndTimePicker shiftEnd;
    private SwitchCompat breakTaken;
    private TextView breakStartText;
    private SingleDateAndTimePicker breakStart;
    private TextView breakEndText;
    private SingleDateAndTimePicker breakEnd;
    private SwitchCompat governedByDriverHours;
    private AutoCompleteTextView vehicleRegistration;
    private TextView poaText;
    private SingleDateAndTimePicker poaTime;
    private TextView driveTimeText;
    private SingleDateAndTimePicker driveTime;

    public FullScreenDialog() {}

    public void display(FragmentManager fragmentManager) {
        show(fragmentManager, "fullscreen_dialog");
    }

    /**
     * Formatter to format the date and time to default
     * values to have a date of today and set the time
     * to 00 for hours and 01 for minutes.
     * @return the required date and time
     */
    public Date getDefaultDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
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
        breakStartText = view.findViewById(R.id.break_start_text);
        breakStart = view.findViewById(R.id.break_start_time);
        breakEndText = view.findViewById(R.id.break_end_text);
        breakEnd = view.findViewById(R.id.break_end_time);
        governedByDriverHours = view.findViewById(R.id.governed_by_driver_hours);
        vehicleRegistration = view.findViewById(R.id.vehicle_registration);
        poaText = view.findViewById(R.id.poa_text);
        poaTime = view.findViewById(R.id.poa_time);
        driveTimeText = view.findViewById(R.id.drive_time_text);
        driveTime = view.findViewById(R.id.drive_time);

        db = Room.databaseBuilder(getContext(),
                ShiftLogDatabase.class,
                "ShiftLogDatabase").allowMainThreadQueries().build();

        // List of all companies that the logged in user has worked for.
        List<String> companyList = db.shiftLogDao().getAllCompanies(
                FirebaseAuth.getInstance().getCurrentUser().getUid());

        List<String> agentNameList = db.shiftLogDao().getAllAgentNames(
                FirebaseAuth.getInstance().getCurrentUser().getUid());

        List<String> vehicleRegistrationList = db.shiftLogDao().getAllVehicleRegistrations(
                FirebaseAuth.getInstance().getCurrentUser().getUid());

        // Remove all null values from `agentNameList`.
        agentNameList.removeAll(Collections.singleton(null));

        // Remove all null values from `vehicleRegistrationList`.
        vehicleRegistrationList.removeAll(Collections.singleton(null));

        // Display list of suggestions after one character has been typed.
        companyName.setThreshold(1);
        agentName.setThreshold(1);
        vehicleRegistration.setThreshold(1);

        // Shows a list of previously-used companies as the user begins to type.
        companyName.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, companyList));

        // Shows a list of previously-used agent names as the user begins to type
        agentName.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, agentNameList));

        // Shows a list of previously-used vehicle registrations as the user begins to type.
        vehicleRegistration.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, vehicleRegistrationList));

        // Displays all the times in 24-hour format.
        shiftStart.setIsAmPm(false);
        shiftEnd.setIsAmPm(false);
        breakStart.setIsAmPm(false);
        breakEnd.setIsAmPm(false);
        poaTime.setIsAmPm(false);
        driveTime.setIsAmPm(false);

        // Sets the step of the minutes to 1.
        shiftStart.setStepMinutes(1);
        shiftEnd.setStepMinutes(1);
        breakStart.setStepMinutes(1);
        breakEnd.setStepMinutes(1);
        poaTime.setStepMinutes(1);
        driveTime.setStepMinutes(1);

        // Set max/min date for break start/end.
        breakStart.setMinDate(shiftStart.getDate());
        breakStart.setMaxDate(shiftEnd.getDate());

        breakEnd.setMinDate(shiftStart.getDate());
        breakEnd.setMaxDate(shiftEnd.getDate());

        // Remove the dates from hour and minute inputs.
        poaTime.setDisplayDays(false);
        driveTime.setDisplayDays(false);

        // Set the default date and time.
        poaTime.setDefaultDate(getDefaultDate());
        driveTime.setDefaultDate(getDefaultDate());

        // Set initial visibility of optional fields to `View.GONE`.
        agentName.setVisibility(View.GONE);
        breakStartText.setVisibility(View.GONE);
        breakStart.setVisibility(View.GONE);
        breakEndText.setVisibility(View.GONE);
        breakEnd.setVisibility(View.GONE);
        vehicleRegistration.setVisibility(View.GONE);
        poaText.setVisibility(View.GONE);
        poaTime.setVisibility(View.GONE);
        driveTimeText.setVisibility(View.GONE);
        driveTime.setVisibility(View.GONE);

        /*
         * If the arguments in the `Bundle` are not null, populate the fields in the shift log
         * form with the given information from the `Bundle`.
         */
        if (getArguments() != null) {
            companyName.setText(getArguments().getString(ShiftLogsFragment.EXTRA_COMPANY_NAME));

            if (getArguments().getBoolean(ShiftLogsFragment.EXTRA_WORKED_FOR_AGENT)) {
                workedForAgent.setChecked(true);
                agentName.setVisibility(View.VISIBLE);
                agentName.setText(getArguments().getString(ShiftLogsFragment.EXTRA_AGENT_NAME));
            }

            shiftStart.setDefaultDate((Date) getArguments().getSerializable(
                    ShiftLogsFragment.EXTRA_SHIFT_START));
            shiftEnd.setDefaultDate((Date) getArguments().getSerializable(
                    ShiftLogsFragment.EXTRA_SHIFT_END));

            if (getArguments().getBoolean(ShiftLogsFragment.EXTRA_BREAK_TAKEN)) {
                breakTaken.setChecked(true);
                breakStartText.setVisibility(View.VISIBLE);
                breakStart.setVisibility(View.VISIBLE);
                breakStart.setDefaultDate((Date) getArguments().getSerializable(
                        ShiftLogsFragment.EXTRA_BREAK_START));
                breakEndText.setVisibility(View.VISIBLE);
                breakEnd.setVisibility(View.VISIBLE);
                breakEnd.setDefaultDate((Date) getArguments().getSerializable(
                        ShiftLogsFragment.EXTRA_BREAK_END));
            }

            if (getArguments().getBoolean(ShiftLogsFragment.EXTRA_GOVERNED_BY_DRIVER_HOURS)) {
                governedByDriverHours.setChecked(true);
                vehicleRegistration.setVisibility(View.VISIBLE);
                vehicleRegistration.setText(getArguments().getString(
                        ShiftLogsFragment.EXTRA_VEHICLE_REGISTRATION));
                poaText.setVisibility(View.VISIBLE);
                poaTime.setDefaultDate((Date) getArguments().getSerializable(
                        ShiftLogsFragment.EXTRA_POA_TIME));
                driveTimeText.setVisibility(View.VISIBLE);
                driveTime.setDefaultDate((Date) getArguments().getSerializable(
                        ShiftLogsFragment.EXTRA_DRIVE_TIME));
            }
        }

        workedForAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!workedForAgent.isChecked()) {
                    agentName.setVisibility(View.GONE);
                } else {
                    agentName.setVisibility(View.VISIBLE);
                }
            }
        });

        breakTaken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!breakTaken.isChecked()) {
                    breakStartText.setVisibility(View.GONE);
                    breakStart.setVisibility(View.GONE);
                    breakEndText.setVisibility(View.GONE);
                    breakEnd.setVisibility(View.GONE);
                } else {
                    breakStartText.setVisibility(View.VISIBLE);
                    breakStart.setVisibility(View.VISIBLE);
                    breakEndText.setVisibility(View.VISIBLE);
                    breakEnd.setVisibility(View.VISIBLE);
                }
            }
        });

        governedByDriverHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!governedByDriverHours.isChecked()) {
                    vehicleRegistration.setVisibility(View.GONE);
                    poaText.setVisibility(View.GONE);
                    poaTime.setVisibility(View.GONE);
                    driveTimeText.setVisibility(View.GONE);
                    driveTime.setVisibility(View.GONE);
                } else {
                    vehicleRegistration.setVisibility(View.VISIBLE);
                    poaText.setVisibility(View.VISIBLE);
                    poaTime.setVisibility(View.VISIBLE);
                    driveTimeText.setVisibility(View.VISIBLE);
                    driveTime.setVisibility(View.VISIBLE);
                }
            }
        });

        // Return the view inflated with the layout defined for full screen dialog.
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenDialog.this.dismiss();
            }
        });
        toolbar.inflateMenu(R.menu.full_screen_dialog_menu);
        if (getArguments() != null) {
            toolbar.getMenu().findItem(R.id.action_add).setTitle("Save");
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (fieldsValid()) {
                    db = Room.databaseBuilder(getContext(),
                            ShiftLogDatabase.class,
                            "ShiftLogDatabase").build();
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            ShiftLog shiftLog = insertData();
                            if (getArguments() != null) {
                                int clickedShiftLogId =
                                        getArguments().getInt(ShiftLogsFragment.EXTRA_SHIFT_LOG_ID);
                                shiftLog.setShiftLogId(clickedShiftLogId);
                                db.shiftLogDao().insertShiftLogs(shiftLog);
                            } else {
                                // Store the current data of the database inside a `List`.
                                List<ShiftLog> currentDatabaseContent = db.shiftLogDao()
                                        .getAllShiftLogs(FirebaseAuth.getInstance()
                                                .getCurrentUser().getUid());
                                // Delete all the data of the current user from the database.
                                db.shiftLogDao().deleteAllShiftLogs(FirebaseAuth.getInstance()
                                        .getCurrentUser().getUid());
                                // Add the new shift log to `currentDatabaseContent`.
                                currentDatabaseContent.add(shiftLog);
                                /*
                                 * Add all of the shift logs from `currentDatabaseContent` to the
                                 * database.
                                 */
                                for (ShiftLog shiftLogs : currentDatabaseContent) {
                                    db.shiftLogDao().insertShiftLogs(shiftLogs);
                                }

                                // Refresh the `ShiftLogsFragment`.
                                FragmentManager fragmentManager = getActivity()
                                        .getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager
                                        .beginTransaction();
                                fragmentTransaction.replace(R.id.container, new ShiftLogsFragment())
                                        .commit();
                            }
                            // Close the `FullScreenDialog`.
                            FullScreenDialog.this.dismiss();
                        }
                    });
                    db.close();
                }

                return true;
            }
        });
    }

    private ShiftLog insertData() {
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
        shiftLogBuilder.setGovernedByDriverHours(
                governedByDriverHours.isChecked());
        if (!governedByDriverHours.isChecked()) {
            shiftLogBuilder.setVehicleRegistration(null);
            shiftLogBuilder.setPoaTime(null);
            shiftLogBuilder.setDriveTime(null);
        } else {
            shiftLogBuilder.setVehicleRegistration(
                    vehicleRegistration.getText().toString());
            shiftLogBuilder.setPoaTime(poaTime.getDate().getTime());
            shiftLogBuilder.setDriveTime(driveTime.getDate().getTime());
        }
        // Build the shift log object.
        return shiftLogBuilder.build();
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
                (breakStart.getDate().before(shiftStart.getDate()) ||
                        breakStart.getDate().after(shiftEnd.getDate())) ||
                (breakStart.getDate().after(breakEnd.getDate()) &&
                        breakStart.getDate().getTime() <= breakEnd.getDate().getTime())) {
            showMessage("Invalid break date or time");
            return false;
        }

        if (governedByDriverHours.isChecked() &&
                vehicleRegistration.getText().toString().isEmpty()) {
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
