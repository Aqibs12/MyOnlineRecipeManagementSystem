package com.example.fitrecipes.Activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitrecipes.R;
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
    TextView tvTotalRecipes,phone;
    EditText name, emailAddress,  question, answer,password;
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
        name = findViewById(R.id.name);
        answer = findViewById(R.id.answer);
        question = findViewById(R.id.question);
        password = findViewById(R.id.password);
        emailAddress = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        tvTotalRecipes = findViewById(R.id.tv_total_recipes);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(USERS);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (LoginActivity.UUID.equals(ds.getKey())) {
                        _USERNAME = ds.getKey();
                        _Email = ds.child("email").getValue(String.class);
                        _Name = ds.child("name").getValue(String.class);
                        _Password = ds.child("password").getValue(String.class);
                        _Phone = ds.child("phone").getValue(String.class);
                        _Answer = ds.child("security_answer").getValue(String.class);
                        _Question = ds.child("security_question").getValue(String.class);

                        //

                        emailAddress.setText(_Email);
                        name.setText(_Name);
                        /*password.setText(_Password);
                        phone.setText(_Phone);*/
                        answer.setText(_Answer);
                        question.setText(_Question);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        reference = FirebaseDatabase.getInstance().getReference("users");
        btn_update = findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {


                    validationCheck();
                    if (isEmailChanged() || isNameChanged() || isPhoneChange() || isPasswordChanged()  || isAnswerChanged() || isQuestionChanged()) {
//                    if (isEmailChanged() || isNameChanged()  || isQuestionChanged() || isAnswerChanged()) {
//                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
//                    startActivity(intent);
                        onBackPressed();
                        Toast.makeText(context, "Data has been changed", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(context, "Data is same and can not be changed", Toast.LENGTH_SHORT).show();
                }
            }


            private boolean isEmailChanged() {
                if (!_Email.equals(emailAddress.getText().toString())) {
                    reference.child(_USERNAME).child("email").setValue(emailAddress.getText().toString());
                    _Email = emailAddress.getText().toString();
                    return true;

                } else
                    return false;

            }

            private boolean isNameChanged() {
                if (!_Name.equals(name.getText().toString())) {
                    reference.child(_USERNAME).child("name").setValue(name.getText().toString());
                    _Name = name.getText().toString();
                    return true;

                } else
                    return false;

            }

            private boolean isPhoneChange() {
                if (!_Phone.equals(phone.getText().toString())) {
                    reference.child(_USERNAME).child("phone").setValue(phone.getText().toString());
                    _Phone = phone.getText().toString();
                    return true;
                } else
                    return false;

            }

            private boolean isPasswordChanged() {
                if (!_Password.equals(password.getText().toString())) {
                    reference.child(_USERNAME).child("password").setValue(password.getText().toString());
                    _Password = password.getText().toString();
                    return true;
                } else
                    return false;

            }

            private boolean isAnswerChanged() {
                if (!_Answer.equals(answer.getText().toString())) {
                    reference.child(_USERNAME).child("security_answer").setValue(answer.getText().toString());
                    _Answer = answer.getText().toString();
                    return true;
                } else
                    return false;
            }
            private boolean isQuestionChanged() {
                if (!_Question.equals(question.getText().toString())) {
                    reference.child(_USERNAME).child("security_question").setValue(question.getText().toString());
                    _Question = question.getText().toString();
                    return true;
                } else
                    return false;
            }




            private void validationCheck() {
                if ((validationChecks.validateAnyName(emailAddress, "Please Enter Email"))
                        && (validationChecks.validateEmail(emailAddress, "Please Enter Valid Email"))
                        && (validationChecks.validateAnyName(password, "Please Enter Password"))
                        && (validationChecks.validateAnyName(name, "Please Enter Name"))
                        && (validationChecks.validateAnyName(answer, "Please Enter Answer"))
                        && (validationChecks.validateAnyName(question, "Please Enter Question"))
                     //   && (validationChecks.validateAnyName(phone, "Please Enter Phone"))
                ) {
                }
            }

            private boolean isValid() {
                if (TextUtils.isEmpty(_Name)) {
                    name.setError("Please Enter your Name");
                    name.requestFocus();
                    return false;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(_Email).matches()) {
                    emailAddress.setError("Email Address is Invalid");
                    emailAddress.requestFocus();
                    return false;
                }
                if (_Password.length() < 8) {
                    password.setError("Please Enter a valid Password with at least 8 Alphabets");
                    password.requestFocus();
                    return false;
                }

                if (TextUtils.isEmpty(_Phone) || _Phone.length() < 10) {
                    phone.setError("Please Enter Phone Number");
                    phone.requestFocus();
                    return false;
                }
                if (_Phone.length() < 10) {
                    phone.setError("Please Enter a valid Phone Number with at least 10 digits");
                    phone.requestFocus();
                    return false;
                }

                if (TextUtils.isEmpty(_Answer)) {
                    answer.setError("Please Enter Answer");
                    answer.requestFocus();
                    return false;
                }
                if (TextUtils.isEmpty(_Question)) {
                    question.setError("Please Enter Question");
                    question.requestFocus();
                    return false;
                }



                return true;
            }
        });
    }
 }