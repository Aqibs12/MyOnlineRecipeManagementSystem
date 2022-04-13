package com.example.fitrecipes.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fitrecipes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


import java.util.List;


public class ForgotPassword extends AppCompatActivity {
    private Button btn_submit;
    EditText signup_recoveryEmail;
    private String email;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        btn_submit = findViewById(R.id.btn_submit);
        signup_recoveryEmail = findViewById(R.id.signup_recoveryEmail);
        fAuth = FirebaseAuth.getInstance();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
            });
    }
    public void onBackPress(View view) {
        onBackPressed();
    }

    private void validateData() {
        email = signup_recoveryEmail.getText().toString();
        if (email.isEmpty()){
            signup_recoveryEmail.setError("Required");
        }else {
            forgetPass();
        }
    }
    private void forgetPass() {
        fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Check Your Email", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(ForgotPassword.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}