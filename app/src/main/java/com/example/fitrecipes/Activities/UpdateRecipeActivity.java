package com.example.fitrecipes.Activities;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitrecipes.Models.ImagesModel;
import com.example.fitrecipes.Models.IngredientModel;
import com.example.fitrecipes.Models.RecipeModel;
import com.example.fitrecipes.R;
import com.example.fitrecipes.Util.ValidationChecks;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class UpdateRecipeActivity extends AppCompatActivity {
    Spinner spin;
    CircleImageView profile_image;
    EditText name, serving, time, desc, instructions, ingredient;
    private Uri filePathUri;
    MaterialButton btn,addIng;
    ValidationChecks validationChecks = new ValidationChecks();
    private RecipeModel recipeModel;
    RecyclerView recyclerIngreditent,recyclerImages;
    ArrayList<IngredientModel> ingredientModelArrayList;
    ArrayList<ImagesModel> imagesModelArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_recipe);
        init();
        setListeners();
    }
    private void init(){
        recyclerImages =findViewById(R.id.recyclerImages);
        recyclerIngreditent =findViewById(R.id.recyclerIngreditent);
        btn = findViewById(R.id.btn);
        addIng = findViewById(R.id.adding);
        spin = findViewById(R.id.spin);
        profile_image = findViewById(R.id.profile_image);
        name = findViewById(R.id.name);
        time = findViewById(R.id.time);
        desc = findViewById(R.id.desc);
        instructions = findViewById(R.id.instructions);
        ingredient = findViewById(R.id.ingredients);
        serving = findViewById(R.id.serving);

    }
    private void setListeners(){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validationCheck();
            }
        });

    }
    private void validationCheck() {
        if ((validationChecks.validateAnyName(name, "Please Enter Name"))
                && (validationChecks.validateAnyName(serving, "Please Enter Serving"))
                && (validationChecks.validateAnyName(time, "Please Enter Time"))
                && (validationChecks.validateAnyName(desc, "Please Enter Description"))
                && (validationChecks.validateAnyName(instructions, "Please Enter Instructions"))
                && imagesModelArrayList.size()!=0
                && ingredientModelArrayList.size()!=0
                && spin.getSelectedItemPosition() != 0
        ) {
        }
    }
}