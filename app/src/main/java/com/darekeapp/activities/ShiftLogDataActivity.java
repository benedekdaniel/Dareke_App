package com.darekeapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.darekeapp.R;
import com.darekeapp.fragments.ShiftLogsFragment;
import com.darekeapp.utils.FullScreenDialog;

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

    /**
     * Request code for permission.
     */
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_log_data);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

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
        shiftStartDataText.setText(dateFormatter.format(getIntent().getSerializableExtra(
                ShiftLogsFragment.EXTRA_SHIFT_START)));
        shiftEndDataText.setText(dateFormatter.format(getIntent().getSerializableExtra(
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
            breakStartDataText.setText(dateFormatter.format(getIntent().getSerializableExtra(
                    ShiftLogsFragment.EXTRA_BREAK_START)));
            breakEndDataText.setText(dateFormatter.format(getIntent().getSerializableExtra(
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
            String poaStringFormatted = timeFormatter.format(
                    getIntent().getLongExtra(ShiftLogsFragment.EXTRA_POA_TIME, 0));
            poaTimeDataText.setText(formatTime(poaStringFormatted));
            String driveTimeFormatted = timeFormatter.format(getIntent().getLongExtra(
                    ShiftLogsFragment.EXTRA_DRIVE_TIME, 0));
            driveTimeDataText.setText(formatTime(driveTimeFormatted));
        }
    }

    private String formatTime(String str) {
        String hours = "";
        String minutes = "";
        String[] separated;

        separated = str.split(":");

        if (separated[0].equals("0") || separated[0].equals("1")) {
            hours = " hour ";
        } else {
            hours = " hours ";
        }

        if (separated[1].equals("0") || separated[1].equals("1")) {
            minutes = " minute ";
        } else {
            minutes = " minutes ";
        }

        return separated[0] + hours + separated[1] + minutes;
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
            FullScreenDialog fullScreenDialog = new FullScreenDialog();
            Bundle args = new Bundle();
            args.putInt(ShiftLogsFragment.EXTRA_SHIFT_LOG_ID, getIntent().getIntExtra(
                    ShiftLogsFragment.EXTRA_SHIFT_LOG_ID, -1));
            args.putString(ShiftLogsFragment.EXTRA_COMPANY_NAME, getIntent().getStringExtra(
                    ShiftLogsFragment.EXTRA_COMPANY_NAME));
            args.putBoolean(ShiftLogsFragment.EXTRA_WORKED_FOR_AGENT, getIntent().getBooleanExtra(
                    ShiftLogsFragment.EXTRA_WORKED_FOR_AGENT, false));
            args.putString(ShiftLogsFragment.EXTRA_AGENT_NAME, getIntent().getStringExtra(
                    ShiftLogsFragment.EXTRA_AGENT_NAME));
            args.putSerializable(ShiftLogsFragment.EXTRA_SHIFT_START, getIntent()
                    .getSerializableExtra(ShiftLogsFragment.EXTRA_SHIFT_START));
            args.putSerializable(ShiftLogsFragment.EXTRA_SHIFT_END, getIntent()
                    .getSerializableExtra(ShiftLogsFragment.EXTRA_SHIFT_END));
            fullScreenDialog.setArguments(args);
            fullScreenDialog.display(getSupportFragmentManager());
        } else if (id == R.id.action_share) {
            // Check for permissions (e.g. read contacts and send SMS).
            if (!checkPermission()) {
                // Permission not given, ask for it.
                requestPermission();
            } else {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = shiftLogMessageToSend();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Shift Log");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        } else if (id == android.R.id.home) {
            /*
             * When the user clicks back on the action bar, return back to the
             * `ShiftLogsFragment` by finishing this activity.
             */
            finish();
        }
        return true;
    }

    /**
     * Checks whether the permission for sending sms and accessing contacts has been granted.
     * @return true if permission granted, otherwise false
     */
    public boolean checkPermission() {
        int SendSMSPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.SEND_SMS);
        int ContactPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_CONTACTS);

        return SendSMSPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ContactPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ShiftLogDataActivity.this, new String[] {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
    }

    /**
     * Format the message to be sent with the shift details.
     * @return the `String` of shift log details
     */
    private String shiftLogMessageToSend() {
        String message;
        message = "Company name: " + companyNameDataText.getText() + "\r\n" +
                "Worked for agent? " + workedForAgentDataText.getText() + "\r\n";

        if (!workedForAgentDataText.getText().equals("false")) {
            message += "Agent name: " + agentNameDataText.getText() + "\r\n";
        }

        message += "Shift start: " + shiftStartDataText.getText() + "\r\n" +
                "Shift end: " + shiftEndDataText.getText() + "\r\n" +
                "Break taken? " + breakTakenDataText.getText() + "\r\n";

        if (!breakTakenDataText.getText().equals("false")) {
            message += "Break start: " + breakStartDataText.getText() + "\r\n" +
                    "Break end: " + breakEndDataText.getText() + "\r\n";
        }

        message += "Governed by driver hours? " + governedByDriverHoursDataText.getText() + "\r\n";

        if (!governedByDriverHoursDataText.getText().equals("false")) {
            message += "Vehicle registration: " + vehicleRegistrationDataText.getText() + "\r\n" +
                    "POA time: " + poaTimeDataText.getText() + "\r\n" +
                    "Drive time: " + driveTimeDataText.getText();
        }

        // Return the formatted message with the given shift details.
        return message;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean sendSMSPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readContactsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (!sendSMSPermission || !readContactsPermission) {
                        Toast.makeText(getApplicationContext(), "Permission not granted",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                    break;
                }
        }
    }

}
