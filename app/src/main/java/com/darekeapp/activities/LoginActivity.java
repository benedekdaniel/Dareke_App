package com.darekeapp.activities;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmail;
    private EditText loginPassword;
    private ProgressBar loginProgressBar;
    private Button loginButton;
    private Button loginLinkToReg;
    private Button loginForgotPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = (EditText) findViewById(R.id.login_email);
        loginPassword = (EditText) findViewById(R.id.login_password);
        loginProgressBar = (ProgressBar) findViewById(R.id.login_progress_bar);
        loginProgressBar.setVisibility(View.INVISIBLE);
        loginButton = (Button) findViewById(R.id.btn_sign_in);
        loginLinkToReg = (Button) findViewById(R.id.btn_link_to_register);
        loginForgotPassword = (Button) findViewById(R.id.btn_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setVisibility(View.INVISIBLE);
                loginProgressBar.setVisibility(View.VISIBLE);

                final String email = loginEmail.getText().toString();
                final String password = loginPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    showMessage("Please verify all fields");
                    loginButton.setVisibility(View.VISIBLE);
                } else {
                    signIn(email, password);
                }
            }
        });

        loginLinkToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });

        loginForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loginEmail.getText().toString().equals("")) {
                    mAuth.sendPasswordResetEmail(loginEmail.getText().toString()).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showMessage("Reset password email sent");
                            } else {
                                showMessage("Error sending reset password email");
                            }
                        }
                    });
                } else {
                    showMessage("Enter your email address");
                }
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loginProgressBar.setVisibility(View.INVISIBLE);
                    updateUI();
                } else {
                    showMessage(task.getException().getMessage());
                    loginButton.setVisibility(View.VISIBLE);
                    loginProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateUI() {
       Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
       LoginActivity.this.startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            updateUI();
        }
    }
}
