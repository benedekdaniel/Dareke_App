package com.darekeapp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.darekeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private EditText regName;
    private EditText regEmail;
    private EditText regPassword;
    private EditText regConfirmPassword;
    private ProgressBar progressBar;
    private Button regButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regName = (EditText) findViewById(R.id.register_name);
        regEmail = (EditText) findViewById(R.id.register_email);
        regPassword = (EditText) findViewById(R.id.register_password);
        regConfirmPassword = (EditText) findViewById(R.id.register_password_confirm);
        progressBar = (ProgressBar) findViewById(R.id.register_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        regButton = (Button) findViewById(R.id.btn_register);

        mAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regButton.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                final String name = regName.getText().toString();
                final String email = regEmail.getText().toString();
                final String password = regPassword.getText().toString();
                final String confirmPassword = regConfirmPassword.getText().toString();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() ||
                        confirmPassword.isEmpty() || !password.equals(confirmPassword)) {
                    showMessage("Please verify all fields");
                    regButton.setVisibility(View.VISIBLE);
                } else {
                    createUserAccount(email, name, password);
                }
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void createUserAccount(String email, final String name, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            showMessage("Account created");
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {
                            showMessage(
                                    "Account creation failed" + task.getException().getMessage());
                            regButton.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
