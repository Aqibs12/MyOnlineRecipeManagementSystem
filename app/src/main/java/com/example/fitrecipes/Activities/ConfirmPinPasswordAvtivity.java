package com.example.fitrecipes.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitrecipes.R;

public class ConfirmPinPasswordAvtivity extends AppCompatActivity {
    private Button btn_submit_confirmpin;
    EditText ed_pin;
    TextView qstn;
    private String userId,a,q="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pin_password_avtivity);
        a=getIntent().getStringExtra("a");
        userId=getIntent().getStringExtra("user_id");
        q=getIntent().getStringExtra("q");
        ed_pin = findViewById(R.id.ed_pin);
        qstn = findViewById(R.id.qstn);
        qstn.setText(q);
        ed_pin.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ed_pin.setBackground(getDrawable(R.drawable.edit_text_with_stroke));
                return false;
            }
        });
        btn_submit_confirmpin = findViewById(R.id.btn_submit_confirmpin);
        btn_submit_confirmpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ed_pin.getText().toString().equals("")){
                    if (a.equals(ed_pin.getText().toString())){
                        Intent intent = new Intent(ConfirmPinPasswordAvtivity.this, ChangePasswordActivity.class);
                        intent.putExtra("user_id",getIntent().getStringExtra("user_id"));
                        startActivity(intent);

                    }else {
                        Toast.makeText(ConfirmPinPasswordAvtivity.this, "Incorrect answer", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}
