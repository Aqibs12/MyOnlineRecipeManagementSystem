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


import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitrecipes.Models.UserModel;
import com.example.fitrecipes.R;
import com.example.fitrecipes.Util.DatabaseHelper;


public class SignUpActivity extends AppCompatActivity {
    private TextView tv_signIn;
    private EditText et_fullName, et_emailAddress, et_password, et_cPassword, et_phoneNumber,sign_up_question,sign_up_ans;
    private Button btn_signUp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
       
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


    public void validations() {
        boolean check = false;
        //making variable for validations
        String fullName = et_fullName.getText().toString().trim();
        String emailAddress = et_emailAddress.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String cPassword = et_cPassword.getText().toString().trim();
        String phoneNumber = et_phoneNumber.getText().toString().trim();
        //validate data here
        if (TextUtils.isEmpty(fullName)) {
            check = true;
            et_fullName.setError("Please Enter your Name");
        }
        if (fullName.length() < 3) {
            et_fullName.setError("Please Enter Name");
            return;
        }
        if (sign_up_question.length() < 3) {
            sign_up_question.setError("Please Enter Question");
            return;
        }
        if (sign_up_ans.length() < 3) {
            sign_up_ans.setError("Please Enter Answer");
            return;
        }
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 10) {
            et_phoneNumber.setError("Please Enter Phone Number");
            return;
        }
        if (phoneNumber.length() < 9) {
            et_phoneNumber.setError("Please Enter a valid Phone Number with at least 10 digits");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            et_emailAddress.setError("Email Address is Invalid");
            return;
        }

        if (!password.equals(cPassword)) {
            et_cPassword.setError("Password Does not matches....");
            return;
        }

        DatabaseHelper databaseHelper=new DatabaseHelper(getApplicationContext());
        UserModel userModel = new UserModel(
                et_fullName.getText().toString(),
                et_emailAddress.getText().toString(),
                et_phoneNumber.getText().toString(),
                et_password.getText().toString(),
                sign_up_question.getText().toString(),
                sign_up_ans.getText().toString()
        );
        String s =databaseHelper.saveUserData(userModel);
        if (s.equals("added")){
            Toast.makeText(getApplicationContext(), "UserModel Registered Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(getApplicationContext(), "UserModel Already Registered", Toast.LENGTH_SHORT).show();

        }


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
                validations();
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


    public void onBackPressed(View view) {
        onBackPressed();
    }


}
