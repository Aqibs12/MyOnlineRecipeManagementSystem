package com.example.fitrecipes.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.fitrecipes.Activities.MainActivity;
import com.example.fitrecipes.Activities.MyRecipesActivity;
import com.example.fitrecipes.Models.UserModel;
import com.example.fitrecipes.R;
import com.example.fitrecipes.Util.DatabaseHelper;
import com.example.fitrecipes.Util.HelperKeys;
import com.example.fitrecipes.Util.SessionManager;
import com.example.fitrecipes.Util.ValidationChecks;
import com.google.android.material.button.MaterialButton;


public class ProfileFragment extends Fragment {


    MaterialButton btn_update;
    TextView tvTotalRecipes;
    EditText name,phone,email,password,question,answer;
    ValidationChecks validationChecks = new ValidationChecks();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.layout_profile, container, false);
        name=view.findViewById(R.id.name);
        answer=view.findViewById(R.id.answer);
        question=view.findViewById(R.id.question);
        password=view.findViewById(R.id.password);
        email=view.findViewById(R.id.email);
        phone=view.findViewById(R.id.phone);
        tvTotalRecipes = view.findViewById(R.id.tv_total_recipes);
        int userID =  Integer.parseInt(SessionManager.getStringPref(HelperKeys.USER_ID,getContext()));
        //get all the recipes of this user id from the database and set text to tvTotalRecipes
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        final int totalRecipes = databaseHelper.getAllRecipes(userID).size();
        tvTotalRecipes.setText(totalRecipes+"");
        name.setText(SessionManager.getStringPref(HelperKeys.USER_NAME, getContext()));
        answer.setText(SessionManager.getStringPref(HelperKeys.USER_A, getContext()));
        question.setText(SessionManager.getStringPref(HelperKeys.USER_Q, getContext()));
        password.setText(SessionManager.getStringPref(HelperKeys.USER_PASSWORD, getContext()));
        email.setText(SessionManager.getStringPref(HelperKeys.USER_EMAIL, getContext()));
        phone.setText(SessionManager.getStringPref(HelperKeys.USER_PHONE_NO, getContext()));
        btn_update=view.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validationCheck();
            }
        });
        view.findViewById(R.id.tv_view_all_recipes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),MyRecipesActivity.class));
            }
        });
        return view;
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
            userModel.setId(Integer.parseInt(SessionManager.getStringPref(HelperKeys.USER_ID, getContext())));
            DatabaseHelper databaseHelper=new DatabaseHelper(getContext());
            databaseHelper.updateUserData(userModel);
            
                SessionManager.putStringPref(HelperKeys.USER_NAME, String.valueOf(name.getText().toString()), getContext());
                SessionManager.putStringPref(HelperKeys.USER_EMAIL, String.valueOf(email.getText().toString()), getContext());
                SessionManager.putStringPref(HelperKeys.USER_PHONE_NO, String.valueOf(phone.getText().toString()), getContext());
                SessionManager.putStringPref(HelperKeys.USER_PASSWORD, String.valueOf(password.getText().toString()), getContext());
                SessionManager.putStringPref(HelperKeys.USER_Q, String.valueOf(question.getText().toString()), getContext());
                SessionManager.putStringPref(HelperKeys.USER_A, String.valueOf(answer.getText().toString()), getContext());
            Toast.makeText(getContext(), "UserModel updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
           
        }
    }
}