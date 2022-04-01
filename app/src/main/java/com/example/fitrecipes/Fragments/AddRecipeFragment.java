package com.example.fitrecipes.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitrecipes.Activities.MainActivity;
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


public class AddRecipeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /** fetch list of categories here in a arrayList*/
        categoryModelArrayList = new ArrayList();
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        categoryModelArrayList.addAll(databaseHelper.getAllCategories());
        // ToDo Make Array of string Items.
       /* DatabaseHelper databaseHelper1 = new DatabaseHelper(getContext());*/
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
        View view = inflater.inflate(R.layout.layout_add_recipe, container, false);
        recyclerImages = view.findViewById(R.id.recyclerImages);
        recyclerIngreditent = view.findViewById(R.id.recyclerIngreditent);
        btn = view.findViewById(R.id.btn);
        addIng = view.findViewById(R.id.adding);
        spin = view.findViewById(R.id.spin);
        // ToDo Assign Array to spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,categoryList);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
        profile_image = view.findViewById(R.id.profile_image);
        name = view.findViewById(R.id.name);
        time = view.findViewById(R.id.time);
        desc = view.findViewById(R.id.desc);
        instructions = view.findViewById(R.id.instructions);
        ingredient = view.findViewById(R.id.ingredients);
        serving = view.findViewById(R.id.serving);
        ingredientModelArrayList = new ArrayList();
        recyclerIngreditent.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientAdapter = new IngredientAdapter(ingredientModelArrayList, getContext());
        recyclerIngreditent.setAdapter(ingredientAdapter);

        imagesModelArrayList = new ArrayList();
        recyclerImages.setLayoutManager(new LinearLayoutManager(getContext()));
        imagesAdapter = new ImagesAdapter(imagesModelArrayList, getContext());
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

        return view;
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
            Toast.makeText(getContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
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
            recipeModel.setUser_id(Integer.parseInt(SessionManager.getStringPref(HelperKeys.USER_ID, getContext())));
            DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
            String s = databaseHelper.saveRecipeData(recipeModel, ingredientModelArrayList,imagesModelArrayList);
            if (s.equals("added")) {
                Toast.makeText(getContext(), "Data added Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            } else {
                Toast.makeText(getContext(), "Data already exists", Toast.LENGTH_SHORT).show();
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