package com.chamodex.enterprises.loano;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText register_username_tif, register_password_tif;
    Button registerBtn, loginIntentBtn;
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
        setContentView(R.layout.activity_register);

        // set date viewer
        TextView dateViewer = findViewById(R.id.date_viewer);
        dateViewer.setText(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));

        // back to login implement
        ImageButton backButton = findViewById(R.id.backToLogin);
        backButton.setOnClickListener(v -> {
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(login);
        });
        loginIntentBtn = findViewById(R.id.login);
        loginIntentBtn.setOnClickListener(v -> {
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(login);
        });

        mAuth = FirebaseAuth.getInstance();
        // register handle
        register_username_tif = findViewById(R.id.register_username_tif);
        register_password_tif = findViewById(R.id.register_password_tif);
        registerBtn = findViewById(R.id.register);
        progressBar = findViewById(R.id.progress_bar);

        registerBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String email, password;
            email = String.valueOf(register_username_tif.getText());
            password = String.valueOf(register_password_tif.getText());

            if (TextUtils.isEmpty(email)) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(password)) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener( task -> {
                        progressBar.setVisibility(View.GONE);
                       if (task.isSuccessful()) {
                           // Sign up success
                           Log.d(TAG, "Users creating process successful.");
                           Toast.makeText(this, "Account create successful", Toast.LENGTH_SHORT).show();
                       } else {
                           // Sign up failed
                           Log.w(TAG, "Users creating process unsuccessful.", task.getException());
                           Toast.makeText(this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                       }
                    });
        });

    }
}