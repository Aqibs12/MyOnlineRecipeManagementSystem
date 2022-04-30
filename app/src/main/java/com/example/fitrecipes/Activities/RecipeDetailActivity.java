package com.example.fitrecipes.Activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.fitrecipes.R;
import com.example.fitrecipes.Models.RecipeAdapter;
import com.example.fitrecipes.Models.Recipe;
import com.example.fitrecipes.Models.MyRecyclerViewAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import android.os.Bundle;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        String uid = getIntent().getStringExtra(MyRecyclerViewAdapter.USER_KEY);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid);
    }
}