package com.example.fitrecipes.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitrecipes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends AppCompatActivity {

    EditText et_input1, et_input2, et_input3,et_input4,et_input5,et_input6;
    TextView textMobile;
    Button btn_Verify;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otpactivity);
        init();
        setUpOtpInputs();
        setListeners();
        textMobile.setText(String.format("+92-%s",getIntent().getStringExtra("mobile")));
        verificationId = getIntent().getStringExtra("UhAGMwRjClQQHh6anTtdC1Dyuts2");
    }

    private void init(){
        et_input1 = findViewById(R.id.inputCode1);
        et_input2 = findViewById(R.id.inputCode2);
        et_input3 = findViewById(R.id.inputCode3);
        et_input4 = findViewById(R.id.inputCode4);
        et_input5 = findViewById(R.id.inputCode5);
        et_input6 = findViewById(R.id.inputCode6);
        textMobile = findViewById(R.id.tv_mobile);
        btn_Verify = findViewById(R.id.btn_verify_otp);
    }

    private void setUpOtpInputs(){
        et_input1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    et_input2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_input2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    et_input3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_input3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    et_input4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_input4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    et_input5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_input5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    et_input6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setListeners(){
        btn_Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_input1.getText().toString().trim().isEmpty()
                || et_input2.getText().toString().trim().isEmpty()
                || et_input3.getText().toString().trim().isEmpty()
                || et_input4.getText().toString().trim().isEmpty()
                || et_input5.getText().toString().trim().isEmpty()
                || et_input6.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(VerifyOTPActivity.this, "Please Enter Valid Code", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = et_input1.getText().toString()
                        + et_input2.getText().toString()
                        + et_input3.getText().toString()
                        +et_input4.getText().toString()
                        + et_input5.getText().toString()
                        +et_input6.getText().toString();

                if (verificationId != null) {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);


                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        String id = task.getResult().getUser().getUid();
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class).putExtra("uuid",id);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(VerifyOTPActivity.this, "Verification Code Entered is invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    findViewById(R.id.tv_resend_otp).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PhoneAuthProvider.getInstance().verifyPhoneNumber("+92" + getIntent().getStringExtra("mobile"),
                                    60,
                                    TimeUnit.SECONDS,
                                    VerifyOTPActivity.this,
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            Toast.makeText(VerifyOTPActivity.this, "Error OTP", Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            verificationId = newVerificationId;
                                            Toast.makeText(VerifyOTPActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    });
                }
            }
        });
    }
}