package com.example.fitrecipes.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.fitrecipes.R;
import com.example.fitrecipes.Util.ValidationChecks;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class LoginActivity extends AppCompatActivity {
    EditText login_email, login_password;
    TextView tv_forgotPass, tv_changePass;
    private FirebaseAuth myauth;
    private FirebaseUser user;
    private String userId;
    Button btnlogin;
    ProgressBar progressBar;
    ValidationChecks validationChecks = new ValidationChecks();
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    public static String UUID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myauth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = firebaseDatabase.getReference().child("Users");
        init();
        setListeners();
    }

    private void init(){
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        tv_forgotPass = findViewById(R.id.tv_frogot_password);
        btnlogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressB);

    }
    private void setListeners(){
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailAddress = login_email.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
               // btnlogin.setVisibility(View.INVISIBLE);
                String password = login_password.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                    login_email.setError("Email Address is Invalid");
                    login_email.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    login_password.setError("Please Enter Password");
                    return;
                }
                if (!password.equals(password)) {
                    login_password.setError("Password Does not matches....");
                    login_password.requestFocus();
                    return;
                }


                databaseReference.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            if (UUID.equals(ds.getKey())) {
                                login_password.setText(ds.child("password").getValue(String.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                if (!password.equals(login_password)) {

                    myauth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            btnlogin.setVisibility(View.VISIBLE);
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                try {
                                    String id = task.getResult().getUser().getUid();
                                    UUID = task.getResult().getUser().getUid();
                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class).putExtra("uuid", UUID));
                                    finish();
                                } catch (Exception e) {
                                }

                            } else {

                                Toast.makeText(LoginActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

                }

            }



                });
        TextView tv_signUp = findViewById(R.id.tv_signUp);
        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        tv_forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);

            }
        });


        findViewById(R.id.btn_login_guest_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String guestUserEmail = "guest@gmail.com";
                String password = "123456";

                myauth.signInWithEmailAndPassword(guestUserEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        btnlogin.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            try {
                                String id = task.getResult().getUser().getUid();
                                UUID = task.getResult().getUser().getUid();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class).putExtra("uuid", UUID));
                               /* Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                                intent.putExtra("guest@gmail.com",guestUserEmail);
                                startActivity(intent);*/
                                finish();
                            }catch (Exception e){

                            }

                        } else {
                            Toast.makeText(LoginActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show(); }

                    }
                });
            }

        });
        login_email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                login_email.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.edit_text_with_stroke));
                login_password.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.edit_text_without_stroke));
                return false;
            }
        });
        login_password.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                login_email.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                login_password.setBackground(getDrawable(R.drawable.edit_text_with_stroke));
                return false;
            }
        });




    }

    public void dashBoardLogin(View view) {
        validationCheck();
    }

    private void validationCheck() {
        if ((validationChecks.validateAnyName(login_email, "Please Enter Email"))
                && (validationChecks.validateEmail(login_email, "Please Enter Valid Email"))
                && (validationChecks.validateAnyName(login_password, "Please Enter Password"))
        ) {

            //   loginUser(this, login_email.getText().toString(), login_password.getText().toString(), "user");
        }
    }
}
