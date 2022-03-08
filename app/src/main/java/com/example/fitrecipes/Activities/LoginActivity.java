package com.example.fitrecipes.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.fitrecipes.Models.UserModel;
import com.example.fitrecipes.R;
import com.example.fitrecipes.Util.DatabaseHelper;
import com.example.fitrecipes.Util.HelperKeys;
import com.example.fitrecipes.Util.SessionManager;
import com.example.fitrecipes.Util.ValidationChecks;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText login_email, login_password;
    ValidationChecks validationChecks = new ValidationChecks();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final String userId = SessionManager.getStringPref(HelperKeys.USER_ID, getApplicationContext());
        if (!userId.equals("")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);

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

        TextView tv_signUp = findViewById(R.id.tv_signUp);
        TextView tv_forgot_password = findViewById(R.id.tv_frogot_password);
        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_login_guest_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(LoginActivity.this, "", "", "");
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

            loginUser(this, login_email.getText().toString(), login_password.getText().toString(), "user");
        }
    }

    public void loginUser(final Activity context, String email, String password, String type) {
        List<UserModel> userModelList = new ArrayList<>();
        if (email.isEmpty() && password.isEmpty()) {
            userModelList.add(UserModel.getAnonymousUser());
        } else {
            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
            userModelList.addAll(databaseHelper.getLoginData(email, password));
        }
        if (userModelList.size() > 0) {
            SessionManager.putStringPref(HelperKeys.USER_ID, String.valueOf(userModelList.get(0).getId()), LoginActivity.this);
            SessionManager.putStringPref(HelperKeys.USER_NAME, String.valueOf(userModelList.get(0).getName()), LoginActivity.this);
            SessionManager.putStringPref(HelperKeys.USER_EMAIL, String.valueOf(userModelList.get(0).getEmail()), LoginActivity.this);
            SessionManager.putStringPref(HelperKeys.USER_PHONE_NO, String.valueOf(userModelList.get(0).getPhone()), LoginActivity.this);
            SessionManager.putStringPref(HelperKeys.USER_PASSWORD, String.valueOf(userModelList.get(0).getPassword()), LoginActivity.this);
            SessionManager.putStringPref(HelperKeys.USER_Q, String.valueOf(userModelList.get(0).getSecurity_question()), LoginActivity.this);
            SessionManager.putStringPref(HelperKeys.USER_A, String.valueOf(userModelList.get(0).getSecurity_answer()), LoginActivity.this);
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(context, "Incorrect username or password", Toast.LENGTH_SHORT).show();
        }

    }
}
