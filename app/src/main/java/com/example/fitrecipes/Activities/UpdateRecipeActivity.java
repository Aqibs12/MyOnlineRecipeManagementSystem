package com.example.fitrecipes.Activities;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitrecipes.Adapters.ImagesAdapter;
import com.example.fitrecipes.Adapters.IngredientAdapter;
import com.example.fitrecipes.Models.ImagesModel;
import com.example.fitrecipes.Models.IngredientModel;
import com.example.fitrecipes.Models.RecipeModel;
import com.example.fitrecipes.R;
import com.example.fitrecipes.Util.DatabaseHelper;
import com.example.fitrecipes.Util.HelperKeys;
import com.example.fitrecipes.Util.SessionManager;
import com.example.fitrecipes.Util.ValidationChecks;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private IngredientAdapter ingredientAdapter;
    private ImagesAdapter imagesAdapter;
    ArrayList<IngredientModel> ingredientModelArrayList;
    ArrayList<ImagesModel> imagesModelArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_recipe);
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
        recipeModel= (RecipeModel) getIntent().getSerializableExtra("model");
//        Glide.with(getApplicationContext()).load(recipeModel.getPhoto()).into(profile_image);
        name.setText(recipeModel.getName());
        desc.setText(recipeModel.getDescription());
        time.setText(recipeModel.getCook_time());
        serving.setText(recipeModel.getServing_persons());
        instructions.setText(recipeModel.getInstruc());
        ingredientModelArrayList = new ArrayList();
        recyclerIngreditent.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ingredientAdapter = new IngredientAdapter(ingredientModelArrayList, getApplicationContext());
        recyclerIngreditent.setAdapter(ingredientAdapter);
        ingredientModelArrayList.addAll(recipeModel.getIngredientModelArrayList());
        ingredientAdapter.notifyDataSetChanged();
        imagesModelArrayList = new ArrayList();
        recyclerImages.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        imagesAdapter = new ImagesAdapter(imagesModelArrayList, getApplicationContext());
        recyclerImages.setAdapter(imagesAdapter);
        imagesModelArrayList.addAll(recipeModel.getImagesModelArrayList());
        imagesAdapter.notifyDataSetChanged();

        String[] myResArray = getResources().getStringArray(R.array.cats);
        List<String> myResArrayList = Arrays.asList(myResArray);
        for (int i=0;i<myResArrayList.size();i++){
            if (myResArrayList.get(i).equals(recipeModel.getCategory())){
                spin.setSelection(i);
            }
        }
//        filePathUri= Uri.parse(recipeModel.getPhoto());
//        ingredient.setText(recipeModel.getIngredient());
        btn.setText("Update");
        addIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ingredient.getText().toString().equals("")) {
                    ingredientModelArrayList.add(new IngredientModel(ingredient.getText().toString()));
                    ingredient.setText("");
                    ingredientAdapter.notifyDataSetChanged();
                }
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImage();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validationCheck();
            }
        });


    }

    public void onImage() {

        ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            filePathUri = data.getData();
            Log.i("Tah",filePathUri.toString());
//            profile_image.setImageURI(filePathUri);
            imagesModelArrayList.add(new ImagesModel(filePathUri.getPath()));
            imagesAdapter.notifyDataSetChanged();

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getApplicationContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }

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
            RecipeModel recipeModel1 = new RecipeModel(
                    name.getText().toString(),
                    serving.getText().toString(),
                    time.getText().toString(),
                    spin.getSelectedItem().toString(),
                    desc.getText().toString(),
                    instructions.getText().toString()

            );
            recipeModel1.setUser_id(Integer.parseInt(SessionManager.getStringPref(HelperKeys.USER_ID, getApplicationContext())));
            recipeModel1.setId(recipeModel.getId());
            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
            databaseHelper.updateRecipeData(recipeModel1, ingredientModelArrayList,imagesModelArrayList);

            Toast.makeText(getApplicationContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();


        }
    }


}