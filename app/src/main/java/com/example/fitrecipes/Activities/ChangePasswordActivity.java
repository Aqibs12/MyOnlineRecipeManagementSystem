package com.example.fitrecipes.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitrecipes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChangePasswordActivity extends AppCompatActivity {
    EditText ed_changepass,ed_change_Cpass;
    Button btn_savePassword;
    private String userId;
    FirebaseAuth fAuth;
    FirebaseUser user;
    private String newPass;
    private String confirmPass;
/** Working Pending 11-4-22*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initViews();
        setListeners();
        userId = fAuth.getCurrentUser().getUid();
        user  = fAuth.getCurrentUser();
    }
    private void validateData() {
        newPass = ed_changepass.getText().toString();
        confirmPass = ed_change_Cpass.getText().toString();
        if (newPass.isEmpty() || confirmPass.isEmpty()){
            ed_changepass.setError("Required");
            ed_change_Cpass.setError("Required");
        }else {
            renewPass();
        }
    }
    private void renewPass() {
        String newPassword = ed_change_Cpass.getText().toString();
        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ChangePasswordActivity.this, "Check Your Email", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(ChangePasswordActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void initViews()
    {
        ed_changepass = findViewById(R.id.change_password);
        ed_change_Cpass = findViewById(R.id.change_cpassword);
        btn_savePassword = findViewById(R.id.btn_savePassword);
    }
    private void setListeners(){
        btn_savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }
}
