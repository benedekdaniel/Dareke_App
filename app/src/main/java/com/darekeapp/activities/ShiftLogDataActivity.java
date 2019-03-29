package com.darekeapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.darekeapp.R;
import com.darekeapp.fragments.ShiftLogsFragment;

import java.text.SimpleDateFormat;

public class ShiftLogDataActivity extends AppCompatActivity {

    private TextView companyNameDataText;
    private TextView workedForAgentDataText;
    private TextView agentNameDataTitle;
    private TextView agentNameDataText;
    private TextView shiftStartDataText;
    private TextView shiftEndDataText;
    private TextView breakTakenDataText;
    private TextView breakStartDataTitle;
    private TextView breakStartDataText;
    private TextView breakEndDataTitle;
    private TextView breakEndDataText;
    private TextView governedByDriverHoursDataText;
    private TextView vehicleRegistrationDataTitle;
    private TextView vehicleRegistrationDataText;
    private TextView poaTimeDataTitle;
    private TextView poaTimeDataText;
    private TextView driveTimeDataTitle;
    private TextView driveTimeDataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_log_data);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm:ss");

        companyNameDataText = findViewById(R.id.company_name_data_text);
        workedForAgentDataText = findViewById(R.id.worked_for_agent_data_text);
        agentNameDataTitle = findViewById(R.id.agent_name_data_title);
        agentNameDataText = findViewById(R.id.agent_name_data_text);
        shiftStartDataText = findViewById(R.id.shift_start_data_text);
        shiftEndDataText = findViewById(R.id.shift_end_data_text);
        breakTakenDataText = findViewById(R.id.break_taken_data_text);
        breakStartDataTitle = findViewById(R.id.break_start_time_data_title);
        breakStartDataText = findViewById(R.id.break_start_time_data_text);
        breakEndDataTitle = findViewById(R.id.break_end_time_data_title);
        breakEndDataText = findViewById(R.id.break_end_time_data_text);
        governedByDriverHoursDataText = findViewById(R.id.governed_by_driver_hours_data_text);
        vehicleRegistrationDataTitle = findViewById(R.id.vehicle_registration_data_title);
        vehicleRegistrationDataText = findViewById(R.id.vehicle_registration_data_text);
        poaTimeDataTitle = findViewById(R.id.poa_data_title);
        poaTimeDataText = findViewById(R.id.poa_data_text);
        driveTimeDataTitle = findViewById(R.id.drive_time_data_title);
        driveTimeDataText = findViewById(R.id.drive_time_data_text);

        // Company name.
        companyNameDataText.setText(getIntent().getStringExtra(
                ShiftLogsFragment.EXTRA_COMPANY_NAME));
        // Agent details.
        workedForAgentDataText.setText(String.valueOf(getIntent().getBooleanExtra(
                ShiftLogsFragment.EXTRA_WORKED_FOR_AGENT, false)));
        if (workedForAgentDataText.getText().equals(String.valueOf(false))) {
            agentNameDataTitle.setVisibility(View.GONE);
            agentNameDataText.setVisibility(View.GONE);
        } else {
            agentNameDataText.setText(
                    getIntent().getStringExtra(ShiftLogsFragment.EXTRA_AGENT_NAME));
        }
        // Shift start/end.
        shiftStartDataText.setText(formatter.format(getIntent().getSerializableExtra(
                ShiftLogsFragment.EXTRA_SHIFT_START)));
        shiftEndDataText.setText(formatter.format(getIntent().getSerializableExtra(
                ShiftLogsFragment.EXTRA_SHIFT_END)));
        // Break details.
        breakTakenDataText.setText(String.valueOf(getIntent().getBooleanExtra(
                ShiftLogsFragment.EXTRA_BREAK_TAKEN, false)));
        if (breakTakenDataText.getText().equals(String.valueOf(false))) {
            breakStartDataTitle.setVisibility(View.GONE);
            breakStartDataText.setVisibility(View.GONE);
            breakEndDataTitle.setVisibility(View.GONE);
            breakEndDataText.setVisibility(View.GONE);
        } else {
            breakStartDataText.setText(formatter.format(getIntent().getSerializableExtra(
                    ShiftLogsFragment.EXTRA_BREAK_START)));
            breakEndDataText.setText(formatter.format(getIntent().getSerializableExtra(
                    ShiftLogsFragment.EXTRA_BREAK_END)));
        }
        // Transport job specific data.
        governedByDriverHoursDataText.setText(String.valueOf(getIntent().getBooleanExtra(
                ShiftLogsFragment.EXTRA_GOVERNED_BY_DRIVER_HOURS, false)));
        if (governedByDriverHoursDataText.getText().equals(String.valueOf(false))) {
            vehicleRegistrationDataTitle.setVisibility(View.GONE);
            vehicleRegistrationDataText.setVisibility(View.GONE);
            poaTimeDataTitle.setVisibility(View.GONE);
            poaTimeDataText.setVisibility(View.GONE);
            driveTimeDataTitle.setVisibility(View.GONE);
            driveTimeDataText.setVisibility(View.GONE);
        } else {
            vehicleRegistrationDataText.setText(
                    getIntent().getStringExtra(ShiftLogsFragment.EXTRA_VEHICLE_REGISTRATION));
            poaTimeDataText.setText(String.valueOf(getIntent().getLongExtra(
                    ShiftLogsFragment.EXTRA_POA_TIME, 0)));
            driveTimeDataText.setText(String.valueOf(getIntent().getLongExtra(
                    ShiftLogsFragment.EXTRA_DRIVE_TIME, 0)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shift_log_data_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            return true;
        } else if (id == R.id.action_share) {
            return true;
        } else if (id == android.R.id.home) {
            /*
             * When the user clicks back on the action bar, return back to the
             * `ShiftLogsFragment` by finishing this activity.
             */
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
