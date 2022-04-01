package com.example.fitrecipes.Activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitrecipes.Adapters.ImagesAdapter;
import com.example.fitrecipes.Adapters.IngredientAdapter;
import com.example.fitrecipes.Models.CategoryModel;
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
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;


public class AddRecipeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Context context;
    Spinner spin;
    CircleImageView profile_image;
    EditText name, serving, time, desc, instructions, ingredient;
    private Uri filePathUri;
    MaterialButton btn, addIng;
    ValidationChecks validationChecks = new ValidationChecks();
    RecyclerView recyclerIngreditent,recyclerImages;
    private IngredientAdapter ingredientAdapter;
    private ImagesAdapter imagesAdapter;
    ArrayList<IngredientModel> ingredientModelArrayList;
    ArrayList<CategoryModel> categoryModelArrayList;
    ArrayList<ImagesModel> imagesModelArrayList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        context = this;

        /** fetch list of categories here in a arrayList*/
        categoryModelArrayList = new ArrayList();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        categoryModelArrayList.addAll(databaseHelper.getAllCategories());
        // ToDo Make Array of string Items.
        /* DatabaseHelper databaseHelper1 = new DatabaseHelper(context);*/
        String[] cats = new String[categoryModelArrayList.size()];
        for(int i=0;i<categoryModelArrayList.size();i++){
            CategoryModel categoryModel = categoryModelArrayList.get(i);
            cats[i] = categoryModel.getCategory();
        }

        List<String> categoryList = new ArrayList<>();
        for(CategoryModel categoryModel:categoryModelArrayList)
        {
            categoryList.add(categoryModel.getCategory());
        }

        // Inflate the layout for this fragment
        recyclerImages = findViewById(R.id.recyclerImages);
        recyclerIngreditent = findViewById(R.id.recyclerIngreditent);
        btn = findViewById(R.id.btn);
        addIng = findViewById(R.id.adding);
        spin = findViewById(R.id.spin);
        // ToDo Assign Array to spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,categoryList);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
        profile_image = findViewById(R.id.profile_image);
        name = findViewById(R.id.name);
        time = findViewById(R.id.time);
        desc = findViewById(R.id.desc);
        instructions = findViewById(R.id.instructions);
        ingredient = findViewById(R.id.ingredients);
        serving = findViewById(R.id.serving);
        ingredientModelArrayList = new ArrayList();
        recyclerIngreditent.setLayoutManager(new LinearLayoutManager(context));
        ingredientAdapter = new IngredientAdapter(ingredientModelArrayList, context);
        recyclerIngreditent.setAdapter(ingredientAdapter);

        imagesModelArrayList = new ArrayList();
        recyclerImages.setLayoutManager(new LinearLayoutManager(context));
        imagesAdapter = new ImagesAdapter(imagesModelArrayList, context);
        recyclerImages.setAdapter(imagesAdapter);

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

    }
    public void onImage() {

        ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            filePathUri = data.getData();
            Log.i("Tah", filePathUri.toString());
//            profile_image.setImageURI(filePathUri);
            imagesModelArrayList.add(new ImagesModel(filePathUri.getPath()));
            imagesAdapter.notifyDataSetChanged();
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show();
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
            RecipeModel recipeModel = new RecipeModel(
                    name.getText().toString(),
                    serving.getText().toString(),
                    time.getText().toString(),
                    spin.getSelectedItem().toString(),
                    desc.getText().toString(),
                    instructions.getText().toString()

            );
            recipeModel.setUser_id(Integer.parseInt(SessionManager.getStringPref(HelperKeys.USER_ID, context)));
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            String s = databaseHelper.saveRecipeData(recipeModel, ingredientModelArrayList,imagesModelArrayList);
            if (s.equals("added")) {
                Toast.makeText(context, "Data added Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(context, "Data already exists", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}