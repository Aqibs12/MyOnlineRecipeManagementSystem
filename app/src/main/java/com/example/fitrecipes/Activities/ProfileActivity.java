package com.example.fitrecipes.Activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.fitrecipes.Models.UserModel;
import com.example.fitrecipes.R;
import com.example.fitrecipes.Util.DatabaseHelper;
import com.example.fitrecipes.Util.HelperKeys;
import com.example.fitrecipes.Util.SessionManager;
import com.example.fitrecipes.Util.ValidationChecks;
import com.google.android.material.button.MaterialButton;


public class ProfileActivity extends Activity {


    MaterialButton btn_update;
    TextView tvTotalRecipes;
    EditText name,phone,email,password,question,answer;
    ValidationChecks validationChecks = new ValidationChecks();
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;
        name=findViewById(R.id.name);
        answer=findViewById(R.id.answer);
        question=findViewById(R.id.question);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        tvTotalRecipes = findViewById(R.id.tv_total_recipes);
        int userID =  Integer.parseInt(SessionManager.getStringPref(HelperKeys.USER_ID,context));
        //get all the recipes of this user id from the database and set text to tvTotalRecipes
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        final int totalRecipes = databaseHelper.getAllRecipes(userID).size();
        tvTotalRecipes.setText(totalRecipes+"");
        name.setText(SessionManager.getStringPref(HelperKeys.USER_NAME, context));
        answer.setText(SessionManager.getStringPref(HelperKeys.USER_A, context));
        question.setText(SessionManager.getStringPref(HelperKeys.USER_Q, context));
        password.setText(SessionManager.getStringPref(HelperKeys.USER_PASSWORD, context));
        email.setText(SessionManager.getStringPref(HelperKeys.USER_EMAIL, context));
        phone.setText(SessionManager.getStringPref(HelperKeys.USER_PHONE_NO, context));
        btn_update=findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validationCheck();
            }
        });
        findViewById(R.id.tv_view_all_recipes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,MyRecipesActivity.class));
            }
        });

    }
    private void validationCheck() {
        if ((validationChecks.validateAnyName(email, "Please Enter Email"))
                && (validationChecks.validateEmail(email, "Please Enter Valid Email"))
                && (validationChecks.validateAnyName(password, "Please Enter Password"))
                && (validationChecks.validateAnyName(name, "Please Enter Name"))
                && (validationChecks.validateAnyName(answer, "Please Enter Answer"))
                && (validationChecks.validateAnyName(question, "Please Enter Question"))
                && (validationChecks.validateAnyName(phone, "Please Enter Phone"))
        ) {
            UserModel userModel = new UserModel(
                    name.getText().toString(),
                    email.getText().toString(),
                    phone.getText().toString(),
                    password.getText().toString(),
                    question.getText().toString(),
                    answer.getText().toString()
            );
            userModel.setId(Integer.parseInt(SessionManager.getStringPref(HelperKeys.USER_ID, context)));
            DatabaseHelper databaseHelper=new DatabaseHelper(context);
            databaseHelper.updateUserData(userModel);

            SessionManager.putStringPref(HelperKeys.USER_NAME, String.valueOf(name.getText().toString()), context);
            SessionManager.putStringPref(HelperKeys.USER_EMAIL, String.valueOf(email.getText().toString()), context);
            SessionManager.putStringPref(HelperKeys.USER_PHONE_NO, String.valueOf(phone.getText().toString()), context);
            SessionManager.putStringPref(HelperKeys.USER_PASSWORD, String.valueOf(password.getText().toString()), context);
            SessionManager.putStringPref(HelperKeys.USER_Q, String.valueOf(question.getText().toString()), context);
            SessionManager.putStringPref(HelperKeys.USER_A, String.valueOf(answer.getText().toString()), context);
            Toast.makeText(context, "UserModel updated Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();

        }
    }
}