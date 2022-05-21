package com.example.fitrecipes.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fitrecipes.Models.Recipe;
import com.example.fitrecipes.Models.RecipeModel;
import com.example.fitrecipes.Models.UserModel;
import com.example.fitrecipes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditRecipeActivity extends AppCompatActivity {
    EditText R_name, R_instr, R_ingred, R_Desc, R_Url;
    Spinner R_category, R_time, R_srv_people;
    Button btnSubmit;
    UserModel userModel;
    private String uuid = "";
    private String USERID = "";
    private DatabaseReference productsRef;
    private FirebaseDatabase firebaseDatabase;
    Recipe recipe;
    RecipeModel recipeModel;
    ArrayAdapter<String> adapter, adapter1, adapter2;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recipe_modifications);
        uuid = LoginActivity.UUID;
        recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        recipeModel = recipe.getRecipeModel();
        userModel = (UserModel) getIntent().getSerializableExtra("user");
        init();
        String[] category = {"Category", "Russian", "American", "Thai", "Indonesian",
                "African", "Afghani", "Pakistani", "Malaysian", "Maxican", "Chinese"};
        String[] serving_people = {"Serving People", "1", "2", "3", "4",
                "5", "6", "7", "8", "9", "10"};
        String[] cook_time = {"Cook Time", "5 Minutes", "10 Minutes", "15 Minutes", "20 Minutes",
                "25 Minutes", "30 Minutes", "35 Minutes", "40 Minutes", "45 Minutes", "50 Minutes"};

        /** Category Adapter */
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        R_category.setAdapter(adapter);
        /** Serving People Adapter */
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, serving_people);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        R_srv_people.setAdapter(adapter1);
        /** Cooking Time Adapter */
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cook_time);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        R_time.setAdapter(adapter2);
        productsRef = FirebaseDatabase.getInstance().getReference("Recipess").child(recipe.getRecipeId());

        btnSubmit.
                setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (isValid()) {
                            String RecipeName = R_name.getText().toString();
                            //           String RecipeImg = R_Url.getText().toString();
                            String RecipeD = R_Desc.getText().toString();
                            String RecipeI = R_instr.getText().toString();
                            String RecipeIng = R_ingred.getText().toString();
                            String RecipeT = R_time.getSelectedItem().toString();
                            String RecipeCategory = R_category.getSelectedItem().toString();
                            String RecipePeople = R_srv_people.getSelectedItem().toString();
                            if (RecipeName.equals("")) {
                                Toast.makeText(getApplicationContext(), "Write down Recipe Name.", Toast.LENGTH_LONG).show();
                            } else if (RecipeD.equals("")) {
                                Toast.makeText(getApplicationContext(), "Write down Recipe Description.", Toast.LENGTH_LONG).show();
                            } else {
                                HashMap<String, Object> productMap = new HashMap<>();
                                productMap.put("id", recipe.getRecipeId());
                                productMap.put("name", RecipeName);
                                //             productMap.put("recipe_image", RecipeImg);
                                productMap.put("recipeD", RecipeD);
                                productMap.put("recipeI", RecipeI);
                                productMap.put("recipeIng", RecipeIng);
                                productMap.put("recipeT", RecipeT);
                                productMap.put("recipeCategory", RecipeCategory);
                                productMap.put("recipe_people", RecipePeople);
                                productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(EditRecipeActivity.this, "Changes Applied", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(EditRecipeActivity.this, "Please Add All Recipe Details", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


    private void init() {
        R_name = findViewById(R.id.edit_tv_R_name);
        R_time = findViewById(R.id.edit_tv_R_time);
        R_category = findViewById(R.id.sp_category);
        R_instr = findViewById(R.id.edit_tv_R_Ins);
        R_ingred = findViewById(R.id.edit_tv_R_Ing);
        R_srv_people = findViewById(R.id.edit_tv_R_people);
        R_Desc = findViewById(R.id.edit_tv_R_desc);
        btnSubmit = findViewById(R.id.edit_btn_submit);

        R_name.setText(recipeModel.getName());
//        R_time.getSelectedItem().toString();
//        R_category.getSelectedItem().toString();
        R_instr.setText(recipeModel.getRecipeI());
        R_ingred.setText(recipeModel.getRecipeIng());
      //  R_srv_people.getSelectedItem().toString();
        R_Desc.setText(recipeModel.getRecipeD());


    }

    private boolean isValid() {

        if (R_name.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please Enter Recipe Name", Toast.LENGTH_SHORT).show();
            return false;
        }

     /*   if(filePathUri == null) {
            // ask user to upload picture
            Toast.makeText(context, "Please Upload Image", Toast.LENGTH_SHORT).show();
            return false;
        }
*/
/*        if (adapter1 != null) {
            int position = R_srv_people.getSelectedItemPosition();
            if (position > 0) {
                Toast.makeText(context, "Serving People selected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Please select Sering People", Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        if (adapter2 != null) {
            int position1 = R_time.getSelectedItemPosition();
            if (position1 > 0) {
                Toast.makeText(context, "Cook Time selected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Please select Time", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (adapter != null) {
                int pos = (int) R_category.getSelectedItemId();
                if (pos > 0) {
                    Toast.makeText(context, "Category is selected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Please select Category", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }*/
        if (R_Desc.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please Enter Recipe Description", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (R_instr.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please Enter Recipe Instructions", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (R_ingred.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please Enter Recipe Ingredients", Toast.LENGTH_SHORT).show();
            return false;
        }
            return true;

        }



}