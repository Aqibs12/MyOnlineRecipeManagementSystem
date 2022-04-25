package com.example.fitrecipes.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitrecipes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditRecipeActivity extends AppCompatActivity {
    EditText R_name, R_time, R_instr, R_ingred, R_srv_peop, R_Desc, R_Url;
    Button btnSubmit;
    private DatabaseReference productsRef;
    String RecipeID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        init();
        RecipeID = getIntent().getExtras().getString("rid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Recipess").child(RecipeID);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String RecipeName = R_name.getText().toString();
                String RecipeImg = R_Url.getText().toString();
                String RecipeD = R_Desc.getText().toString();
                String RecipeI = R_instr.getText().toString();
                String RecipeIng = R_ingred.getText().toString();
                String RecipeT = R_time.getText().toString();
                String RecipePeople = R_srv_peop.getText().toString();
                if (RecipeName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Write down Recipe Name.", Toast.LENGTH_LONG).show();
                } else if (RecipeD.equals("")) {
                    Toast.makeText(getApplicationContext(), "Write down Recipe Description.", Toast.LENGTH_LONG).show();
                } else {
                    HashMap<String, Object> productMap = new HashMap<>();
                    productMap.put("id", RecipeID);
                    productMap.put("name", RecipeName);
                    productMap.put("recipe_image", RecipeImg);
                    productMap.put("recipeD", RecipeD);
                    productMap.put("recipeI", RecipeI);
                    productMap.put("recipeIng", RecipeIng);
                    productMap.put("recipeT", RecipeT);
                    productMap.put("recipe_people", RecipePeople);
                    productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(EditRecipeActivity.this, "Changes Applied", Toast.LENGTH_SHORT).show();
                            /*Intent it = new Intent(EditRecipeActivity.this, HomeActivity.class);
                            startActivity(it);
                            finish();*/
                        }
                    });
                }
            }
        });
    }


    private void init() {
        R_name = findViewById(R.id.edit_tv_R_name);
        R_time = findViewById(R.id.edit_tv_R_time);
        R_instr = findViewById(R.id.edit_tv_R_Ins);
        R_ingred = findViewById(R.id.edit_tv_R_Ing);
        R_srv_peop = findViewById(R.id.edit_tv_R_people);
        R_Desc = findViewById(R.id.edit_tv_R_desc);
        R_Url = findViewById(R.id.edit_tv_R_url);
        btnSubmit = findViewById(R.id.edit_btn_submit);
    }
}