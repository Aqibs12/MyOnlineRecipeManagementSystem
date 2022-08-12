package com.example.fitrecipes.Activities;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.text.TextUtils;

import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitrecipes.Models.UserModel;
import com.example.fitrecipes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SignUpActivity extends AppCompatActivity {
    private TextView tv_signIn;
    private EditText et_fullName, et_emailAddress, et_password, et_cPassword, et_phoneNumber, sign_up_question, sign_up_ans;
    private Button btn_signUp;
    private FirebaseAuth myauth;
    FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;
    String inPut = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        myauth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        initViews();
        onClickListeners();
    }

    public void initViews() {
        tv_signIn = findViewById(R.id.tv_signIn);
        et_fullName = findViewById(R.id.sign_up_FullName);
        et_emailAddress = findViewById(R.id.sign_up_email);
        et_password = findViewById(R.id.sign_up_password);
        et_cPassword = findViewById(R.id.sign_up_cPassword);
        et_phoneNumber = findViewById(R.id.sign_up_phoneNumber);
        sign_up_question = findViewById(R.id.sign_up_question);
        sign_up_ans = findViewById(R.id.sign_up_ans);
        btn_signUp = findViewById(R.id.btn_signUp);
    }

    public void onClickListeners() {

        tv_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValid()) {
                    String fullName = et_fullName.getText().toString().trim();
                    String emailAddress = et_emailAddress.getText().toString().trim();
                    String password = et_password.getText().toString().trim();
                    String phoneNumber = et_phoneNumber.getText().toString().trim();
                    String signUpQ = sign_up_question.getText().toString().trim();
                    String signUpA = sign_up_ans.getText().toString().trim();
                    phoneValidation(fullName,emailAddress,phoneNumber,password,signUpQ,signUpA);


                }
            }
        });
        et_fullName.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_fullName.setBackground(getDrawable(R.drawable.edit_text_with_stroke));
                et_emailAddress.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_password.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_cPassword.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_phoneNumber.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                sign_up_question.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                sign_up_ans.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                return false;
            }
        });

        et_password.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_fullName.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_emailAddress.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_password.setBackground(getDrawable(R.drawable.edit_text_with_stroke));
                et_cPassword.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_phoneNumber.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                sign_up_question.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                sign_up_ans.setBackground(getDrawable(R.drawable.edit_text_without_stroke));

                return false;
            }
        });
        et_cPassword.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_fullName.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_emailAddress.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_password.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_cPassword.setBackground(getDrawable(R.drawable.edit_text_with_stroke));
                et_phoneNumber.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                sign_up_question.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                sign_up_ans.setBackground(getDrawable(R.drawable.edit_text_without_stroke));

                return false;
            }
        });
        et_phoneNumber.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_fullName.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_emailAddress.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_password.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_cPassword.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_phoneNumber.setBackground(getDrawable(R.drawable.edit_text_with_stroke));
                sign_up_question.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                sign_up_ans.setBackground(getDrawable(R.drawable.edit_text_without_stroke));

                return false;
            }
        });
        et_emailAddress.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_fullName.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_password.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_cPassword.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_phoneNumber.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_emailAddress.setBackground(getDrawable(R.drawable.edit_text_with_stroke));
                sign_up_question.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                sign_up_ans.setBackground(getDrawable(R.drawable.edit_text_without_stroke));

                return false;
            }
        });
        sign_up_ans.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_fullName.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_password.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_cPassword.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_phoneNumber.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_emailAddress.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                sign_up_question.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                sign_up_ans.setBackground(getDrawable(R.drawable.edit_text_with_stroke));


                return false;
            }
        });
        sign_up_question.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_fullName.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_password.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_cPassword.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_phoneNumber.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                et_emailAddress.setBackground(getDrawable(R.drawable.edit_text_without_stroke));
                sign_up_question.setBackground(getDrawable(R.drawable.edit_text_with_stroke));
                sign_up_ans.setBackground(getDrawable(R.drawable.edit_text_without_stroke));

                return false;
            }
        });

    }

    private boolean isValid() {
        String fullName = et_fullName.getText().toString().trim();
        String emailAddress = et_emailAddress.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String cPassword = et_cPassword.getText().toString().trim();
        String phoneNumber = et_phoneNumber.getText().toString().trim();
        String signUpQ = sign_up_question.getText().toString().trim();
        String signUpA = sign_up_ans.getText().toString().trim();
        //validate data here
        if (TextUtils.isEmpty(fullName)) {
            et_fullName.setError("Please Enter your Name");
            et_fullName.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            et_emailAddress.setError("Email Address is Invalid");
            et_emailAddress.requestFocus();
            return false;
        }

        if (et_password.length() < 8) {
            et_password.setError("Please Enter a valid Password with at least 8 Alphabets");
            et_password.requestFocus();
            return false;
        }

        boolean isUppercase = false;
        boolean isSpecialCharacter = false;
        int digits = 0;
        for (int i = 0; i < et_password.getText().toString().length(); i++) {
            char c = et_password.getText().toString().charAt(i);
            if (Character.isUpperCase(c))
                isUppercase = true;
            if (Character.isDigit(c))
                digits++;
            if (String.valueOf(c).matches("[^a-zA-Z0-9]"))
                isSpecialCharacter = true;
        }
        if (!isUppercase) {
            et_password.setError("Please enter at least 1 upper case letter in password");
            et_password.requestFocus();
            return false;
        }
        if (digits <= 0) {
            et_password.setError("Please enter at least 2 digits in password");
            et_password.requestFocus();
            return false;
        }
        if (!isSpecialCharacter) {
            et_password.setError("Please enter at least 1 special character in password");
            et_password.requestFocus();
            return false;
        }

        if (!password.equals(cPassword)) {
            et_cPassword.setError("Password Does not matches....");
            et_cPassword.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 9) {
            et_phoneNumber.setError("Please Enter Phone Number");
            et_phoneNumber.requestFocus();
            return false;
        }
        if (phoneNumber.length() < 9) {
            et_phoneNumber.setError("Please Enter a valid Phone Number with at least 10 digits");
            et_phoneNumber.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(signUpQ)) {
            sign_up_question.setError("Please Enter Question");
            sign_up_question.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(signUpA)) {
            sign_up_ans.setError("Please Enter Answer");
            sign_up_ans.requestFocus();
            return false;
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // another val
    private void phoneValidation(String fullName, String emailAddress, String phoneNumber2, String password, String signUpQ, String signUpA) {
      int i;   FirebaseDatabase firebaseDatabase2 = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase2 = firebaseDatabase2.getReference().child("users");
        //data retrival
        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> addPhone = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String phone = (String) snapshot.child("phone").getValue();
                    addPhone.add(phone);

                }
                if (addPhone.contains(phoneNumber2)) {
                    Toast.makeText(SignUpActivity.this, "Number  exists", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "Number dont Exists", Toast.LENGTH_SHORT).show();
                    myauth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UserModel userModel = new UserModel(FirebaseAuth.getInstance().getCurrentUser().getUid(), fullName, emailAddress, phoneNumber2, password, signUpQ, signUpA);
                                FirebaseDatabase.getInstance().getReference("users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                    startActivity(intent);

                                                } else {

                                                    Toast.makeText(SignUpActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(SignUpActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }
}

