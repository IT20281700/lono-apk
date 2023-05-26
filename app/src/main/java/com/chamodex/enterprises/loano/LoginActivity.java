package com.chamodex.enterprises.loano;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText login_username_tif, login_password_tif;
    Button loginBtn;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(this, "Already signed in!", Toast.LENGTH_SHORT).show();
            Intent main = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(main);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // set date viewer
        TextView dateViewer = findViewById(R.id.date_viewer);
        dateViewer.setText(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));

        // register intent button implement
        Button regButton = findViewById(R.id.register);
        regButton.setOnClickListener(v -> {
            Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        });

        mAuth = FirebaseAuth.getInstance();
        // login handle
        login_username_tif = findViewById(R.id.login_username_tif);
        login_password_tif = findViewById(R.id.login_password_tif);
        loginBtn = findViewById(R.id.login);
        progressBar = findViewById(R.id.progress_bar);

        loginBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String email, password;
            email = String.valueOf(login_username_tif.getText());
            password = String.valueOf(login_password_tif.getText());

            if (TextUtils.isEmpty(email)) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(password)) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                       if (task.isSuccessful()) {
                           // Sign in success
                           Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                           Intent main = new Intent(getApplicationContext(), MainActivity.class);
                           startActivity(main);
                           finish();
                       } else {
                           //Sign in failed
                           Toast.makeText(this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                       }
                    });
        });

    }
}