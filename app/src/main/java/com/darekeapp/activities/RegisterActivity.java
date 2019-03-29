package com.darekeapp.activities;

import android.content.Intent;
import android.os.Handler;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    private EditText regName;
    private EditText regEmail;
    private EditText regPassword;
    private EditText regConfirmPassword;
    private ProgressBar regProgressBar;
    private Button regButton;
    private Button regLinkToLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        regName = findViewById(R.id.hint_name);
        regEmail = findViewById(R.id.register_email);
        regPassword = findViewById(R.id.register_password);
        regConfirmPassword = findViewById(R.id.register_password_confirm);
        regProgressBar = findViewById(R.id.register_progress_bar);
        regProgressBar.setVisibility(View.INVISIBLE);
        regButton = findViewById(R.id.btn_register);
        regLinkToLogin = findViewById(R.id.btn_link_to_sign_in);

        mAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regButton.setVisibility(View.INVISIBLE);
                regProgressBar.setVisibility(View.VISIBLE);
                final String name = regName.getText().toString();
                final String email = regEmail.getText().toString();
                final String password = regPassword.getText().toString();
                final String confirmPassword = regConfirmPassword.getText().toString();

                if (name.isEmpty() || email.isEmpty() || !validPassword(password) ||
                        confirmPassword.isEmpty() || !password.equals(confirmPassword)) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showMessage("Please verify all fields");
                        }
                    }, 2000);
                    regButton.setVisibility(View.VISIBLE);
                } else {
                    createUserAccount(email, name, password);
                }
            }
        });

        regLinkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginActivity();
            }
        });
    }

    private boolean validPassword(String password) {
        /*
         * Check if the password is a minimum of eight characters, and contains
         * at least one uppercase letter, one lowercase letter and one number.
         */
        if (password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")) {
            return true;
        } else {
            showMessage("Password is not strong enough");
            return false;
        }
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
                            updateUserInfo(mAuth.getCurrentUser(), name);
                            regProgressBar.setVisibility(View.INVISIBLE);
                            launchLoginActivity();
                        } else {
                            showMessage(
                                    "Account creation failed" + task.getException().getMessage());
                            regButton.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void updateUserInfo(FirebaseUser currentUser, String name) {
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();
        currentUser.updateProfile(profileUpdate);
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        RegisterActivity.this.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        regName.setText("");
        regEmail.setText("");
        regPassword.setText("");
        regConfirmPassword.setText("");
    }
}
