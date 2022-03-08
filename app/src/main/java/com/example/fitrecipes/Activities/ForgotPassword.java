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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitrecipes.Models.UserModel;
import com.example.fitrecipes.R;
import com.example.fitrecipes.Util.DatabaseHelper;

import java.util.List;


public class ForgotPassword extends AppCompatActivity {
    private Button btn_submit;
    EditText signup_recoveryEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!signup_recoveryEmail.getText().toString().equals("")) {
                    forgotPass(ForgotPassword.this);
                }
            }
        });
        signup_recoveryEmail = findViewById(R.id.signup_recoveryEmail);
        signup_recoveryEmail.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                signup_recoveryEmail.setBackground(getDrawable(R.drawable.edit_text_with_stroke));
                return false;
            }
        });


    }

    public void forgotPass(final Activity context) {
        DatabaseHelper databaseHelper=new DatabaseHelper(getApplicationContext());
        List<UserModel> userModelList = databaseHelper.getForgotPasswordData(signup_recoveryEmail.getText().toString());
        if (userModelList.size()!=0){
            Intent intent = new Intent(context, ConfirmPinPasswordAvtivity.class);
            intent.putExtra("q", ""+ userModelList.get(0).getSecurity_question());
            intent.putExtra("a", ""+ userModelList.get(0).getSecurity_answer());
            intent.putExtra("user_id", ""+ userModelList.get(0).getId());
            context.startActivity(intent);
            finish();
        }else {
            Toast.makeText(context, "No such user found", Toast.LENGTH_SHORT).show();
        }



    }

    public void onBackPress(View view) {
        onBackPressed();
    }
}
