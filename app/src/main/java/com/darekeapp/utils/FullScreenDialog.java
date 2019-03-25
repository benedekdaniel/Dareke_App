package com.darekeapp.utils;

import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.darekeapp.R;
import com.darekeapp.database.ShiftLogDatabase;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;

public class FullScreenDialog extends DialogFragment {
    private Toolbar toolbar;

    private ShiftLogDatabase db;

    private EditText companyName;
    private SwitchCompat workedForAgent;
    private EditText agentName;
    private SingleDateAndTimePicker shiftStart;
    private SingleDateAndTimePicker shiftEnd;
    private SwitchCompat breakTaken;
    private TextView breakStartText;
    private SingleDateAndTimePicker breakStart;
    private TextView breakEndText;
    private SingleDateAndTimePicker breakEnd;
    private SwitchCompat isTransportJob;
    private EditText transportCompanyName;
    private EditText vehicleRegistration;

    Dialog popupAddPoa;

    public static void display(FragmentManager fragmentManager) {
        FullScreenDialog fullScreenDialog = new FullScreenDialog();
        fullScreenDialog.show(fragmentManager, "fullscreen_dialog");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);

        initialisePopup();
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
        breakStartText = view.findViewById(R.id.break_start_text);
        breakStart = view.findViewById(R.id.break_start_time);
        breakEndText = view.findViewById(R.id.break_end_text);
        breakEnd = view.findViewById(R.id.break_end_time);
        isTransportJob = view.findViewById(R.id.transport_job);
        transportCompanyName = view.findViewById(R.id.transport_company_name);
        vehicleRegistration = view.findViewById(R.id.vehicle_registration);

        // Displays all the times in 24-hour format.
        shiftStart.setIsAmPm(false);
        shiftEnd.setIsAmPm(false);
        breakStart.setIsAmPm(false);
        breakEnd.setIsAmPm(false);

        // Sets the step of the minutes to 1.
        shiftStart.setStepMinutes(1);
        shiftEnd.setStepMinutes(1);
        breakStart.setStepMinutes(1);
        breakEnd.setStepMinutes(1);

        // Set initial visibility of optional fields to `View.GONE`.
        agentName.setVisibility(View.GONE);
        breakStartText.setVisibility(View.GONE);
        breakStart.setVisibility(View.GONE);
        breakEndText.setVisibility(View.GONE);
        breakEnd.setVisibility(View.GONE);
        transportCompanyName.setVisibility(View.GONE);
        vehicleRegistration.setVisibility(View.GONE);

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

        isTransportJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTransportJob.isChecked()) {
                    transportCompanyName.setVisibility(View.GONE);
                    vehicleRegistration.setVisibility(View.GONE);
                } else {
                    transportCompanyName.setVisibility(View.VISIBLE);
                    vehicleRegistration.setVisibility(View.VISIBLE);
                }
            }
        });

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
                    Toast.makeText(getContext(), "Test message passed", Toast.LENGTH_SHORT).show();
                    db = Room.databaseBuilder(getContext(),
                                              ShiftLogDatabase.class,
                                        "ShiftLogDatabase").build();
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

    private void initialisePopup() {
        popupAddPoa = new Dialog(getActivity());
        popupAddPoa.setContentView(R.layout.popup_calendar);
        popupAddPoa.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,
                Toolbar.LayoutParams.WRAP_CONTENT);
        popupAddPoa.getWindow().getAttributes().gravity = Gravity.TOP;
    }

}
