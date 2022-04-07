package com.example.fitrecipes.Activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.fitrecipes.Models.UserModel;
import com.example.fitrecipes.R;
import com.example.fitrecipes.Util.DatabaseHelper;
import com.example.fitrecipes.Util.HelperKeys;
import com.example.fitrecipes.Util.SessionManager;
import com.example.fitrecipes.Util.ValidationChecks;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileActivity extends Activity {
    DatabaseReference reference;
    MaterialButton btn_update;
    TextView tvTotalRecipes;
    EditText name,phone,emailAddress,password,question,answer;
    ValidationChecks validationChecks = new ValidationChecks();
    Context context;
    String _USERNAME, _Name, _Email, _Phone, _Password, _Question, _Answer;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private static final String USERS = "users";

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
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(USERS);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("email").getValue().equals(emailAddress)) ;
                    emailAddress.setText(ds.child("email").getValue(String.class));
                    name.setText(ds.child("name").getValue(String.class));
                    password.setText(ds.child("password").getValue(String.class));
                    phone.setText(ds.child("phone").getValue(String.class));
                    answer.setText(ds.child("security_answer").getValue(String.class));
                    question.setText(ds.child("security_question").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        reference = FirebaseDatabase.getInstance().getReference("users");
        btn_update=findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View view) {
                validationCheck();
                if (isEmailChanged() || isNameChanged() || isPhoneChange() || isPasswordChanged() || isQuestionChanged() || isAnswerChanged()){
                    Toast.makeText(context, "Data has been changed", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(context, "Data is same and can not be changed", Toast.LENGTH_SHORT).show();
            }
        });
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
    private boolean isQuestionChanged() {
        if (!_Question.equals(question.getEditableText().toString())) {
            reference.child(_USERNAME).child("security_question").setValue(password.getEditableText().toString());
            _Question = question.getEditableText().toString();
            return true;
        }else
            return false;
    }
    private boolean isAnswerChanged() {
        if (!_Answer.equals(answer.getEditableText().toString())) {
            reference.child(_USERNAME).child("security_answer").setValue(password.getEditableText().toString());
            _Answer = answer.getEditableText().toString();
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
        ){}
    }
}