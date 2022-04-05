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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileActivity extends Activity {
    DatabaseReference reference;
    MaterialButton btn_update;
    TextView tvTotalRecipes;
    EditText name,phone,emailAddress,password,question,answer;
    ValidationChecks validationChecks = new ValidationChecks();
    Context context;
    String _USERNAME, _Name, _Email, _Phone, _Password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;
        name=findViewById(R.id.name);
        answer=findViewById(R.id.answer);
        question=findViewById(R.id.question);
        password=findViewById(R.id.password);
        emailAddress=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        tvTotalRecipes = findViewById(R.id.tv_total_recipes);
        reference = FirebaseDatabase.getInstance().getReference("users");
        /*int userID =  Integer.parseInt(SessionManager.getStringPref(HelperKeys.USER_ID,context));
        //get all the recipes of this user id from the database and set text to tvTotalRecipes
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        final int totalRecipes = databaseHelper.getAllRecipes(userID).size();
        tvTotalRecipes.setText(totalRecipes+"");
        name.setText(SessionManager.getStringPref(HelperKeys.USER_NAME, context));
        answer.setText(SessionManager.getStringPref(HelperKeys.USER_A, context));
        question.setText(SessionManager.getStringPref(HelperKeys.USER_Q, context));
        password.setText(SessionManager.getStringPref(HelperKeys.USER_PASSWORD, context));
        email.setText(SessionManager.getStringPref(HelperKeys.USER_EMAIL, context));
        phone.setText(SessionManager.getStringPref(HelperKeys.USER_PHONE_NO, context));*/
        btn_update=findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validationCheck();
                if (isEmailChanged() || isNameChanged() || isPhoneChange() || isPasswordChanged()){
                    Toast.makeText(context, "Data has been changed", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(context, "Data is same and can not be changed", Toast.LENGTH_SHORT).show();
            }
        });
       /* findViewById(R.id.tv_view_all_recipes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,MyRecipesActivity.class));
            }
        });*/

    }
    private boolean isEmailChanged() {
        if (!_Email.equals(emailAddress.getEditableText().toString())){
            reference.child(_USERNAME).child("email").setValue(emailAddress.getEditableText().toString());
            _Email = emailAddress.getEditableText().toString();
            return true;

        }else
            return false;

    }

    private boolean isNameChanged() {
        if (!_Name.equals(name.getEditableText().toString())){
            reference.child(_USERNAME).child("name").setValue(name.getEditableText().toString());
            _Name = name.getEditableText().toString();
            return true;

        }else
            return false;

    }

    private boolean isPhoneChange() {
        if (!_Phone.equals(phone.getEditableText().toString())) {
            reference.child(_USERNAME).child("phone").setValue(phone.getEditableText().toString());
            _Phone = phone.getEditableText().toString();
            return true;
        }else
            return false;

    }

    private boolean isPasswordChanged() {
        if (!_Password.equals(password.getEditableText().toString())) {
            reference.child(_USERNAME).child("password").setValue(password.getEditableText().toString());
            _Password = password.getEditableText().toString();
            return true;
        }else
            return false;

    }

    private void validationCheck() {
        if ((validationChecks.validateAnyName(emailAddress, "Please Enter Email"))
                && (validationChecks.validateEmail(emailAddress, "Please Enter Valid Email"))
                && (validationChecks.validateAnyName(password, "Please Enter Password"))
                && (validationChecks.validateAnyName(name, "Please Enter Name"))
                && (validationChecks.validateAnyName(answer, "Please Enter Answer"))
                && (validationChecks.validateAnyName(question, "Please Enter Question"))
                && (validationChecks.validateAnyName(phone, "Please Enter Phone"))
        ) {
           /* UserModel userModel = new UserModel(
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
            finish();*/

        }
    }
}