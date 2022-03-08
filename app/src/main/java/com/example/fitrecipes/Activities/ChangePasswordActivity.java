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

import com.example.fitrecipes.R;
import com.example.fitrecipes.Util.DatabaseHelper;


public class ChangePasswordActivity extends AppCompatActivity {
    EditText ed_changepass,ed_change_Cpass;
    Button btn_savePassword;
    private String userId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        userId=getIntent().getStringExtra("user_id");
        initViews();
        ed_changepass.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ed_change_Cpass.setBackground(getDrawable(R.drawable.edit_text_without_strock));
                ed_changepass.setBackground(getDrawable(R.drawable.edit_text_with_stroke));

                return false;
            }
        });
        ed_change_Cpass.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ed_change_Cpass.setBackground(getDrawable(R.drawable.edit_text_with_stroke));
                ed_changepass.setBackground(getDrawable(R.drawable.edit_text_without_strock));
                return false;
            }
        });
        btn_savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_change_Cpass.getText().toString().equals("")){
                    if (ed_change_Cpass.getText().toString().equals(ed_change_Cpass.getText().toString())){
                        updatePass(ChangePasswordActivity.this);
                    }
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
    public void updatePass(final Activity context) {
        Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show();
        DatabaseHelper databaseHelper=new DatabaseHelper(getApplicationContext());
        databaseHelper.updatePassword(userId,ed_changepass.getText().toString());
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        finish();

    }
}
