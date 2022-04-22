package com.example.fitrecipes.Activities;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitrecipes.Models.RecipeAdapter;
import com.example.fitrecipes.Models.RecipeModel;
import com.example.fitrecipes.R;
import com.example.fitrecipes.Util.ValidationChecks;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddRecipeActivity extends AppCompatActivity {
    RecipeAdapter recipeAdapter;
    Context context;
    Spinner spin;
    CircleImageView profile_image;
    EditText name, serving, time, desc, instructions, ingredient;
    private Uri filePathUri;
    MaterialButton btn, addIng;
    ValidationChecks validationChecks = new ValidationChecks();
    RecyclerView  rvRecipe;
    DatabaseReference databaseReference ,databaseReference2,databaseReference4;
    int Image_Request_Code = 1;
    ProgressDialog progressDialog;
    public static String UUID = "";
    private String myauth;
    String currentUserId;
    private String currentUserID = "";
    private String currentUserID2 = "";
    ValueEventListener listener;
    ArrayList<String> list;
    ArrayList<RecipeModel> recipeModelArrayList;
    ArrayAdapter<String> adapter;
    String productRandomKey;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        context = this;
        init();

        databaseReference = FirebaseDatabase.getInstance().getReference("images");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Recipes");
        progressDialog = new ProgressDialog(AddRecipeActivity.this);
        currentUserID2 = getIntent().getExtras().getString("uuid");
        UUID = getIntent().getExtras().getString("uuid");


      /** firebase recyclerview adapter
       *  FirebaseRecyclerOptions<RecipeModel> options =

                new FirebaseRecyclerOptions.Builder<RecipeModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("recipes"), RecipeModel.class)
                        .build();
        recipeAdapter = new RecipeAdapter(options);
        rvRecipe.setAdapter(recipeAdapter);*/


        databaseReference = FirebaseDatabase.getInstance().getReference("spinner");
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spin.setAdapter(adapter);

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImage();
            }
        });
     fetchUserData();
    }
    @Override
    protected void onStart() {
        super.onStart();
     // recipeAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
   //     recipeAdapter.stopListening();
    }
    private void fetchUserData() {
        listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot mydata : snapshot.getChildren())
                    list.add(mydata.getValue().toString());
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void init() {
        btn = findViewById(R.id.btn);
        addIng = findViewById(R.id.adding);
        spin = findViewById(R.id.spin);
        // ToDo Assign Array to spinner

        profile_image = findViewById(R.id.profile_image);
        name = findViewById(R.id.name);
        time = findViewById(R.id.time);
        desc = findViewById(R.id.desc);
        instructions = findViewById(R.id.instructions);
        ingredient = findViewById(R.id.ingredients);
        serving = findViewById(R.id.serving);
        myauth = FirebaseAuth.getInstance().getUid();
        rvRecipe = findViewById(R.id.recyclerImages2);
        rvRecipe.setHasFixedSize(true);
        rvRecipe.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathUri);
                profile_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public void UploadImage() {

        if (filePathUri != null) {
            progressDialog.setTitle("Recipe is Uploading...");
            progressDialog.show();

            try {
                StorageReference storageReference2 = FirebaseStorage.getInstance().getReference().child(System.currentTimeMillis() + "." + GetFileExtension(filePathUri));
                storageReference2.putFile(filePathUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final String[] photoLink = {""};
                                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        photoLink[0] = uri.toString();
                                        Glide.with(profile_image).load(photoLink[0]).into(profile_image);
                                        String TempImageName = name.getText().toString().trim();
                                        String RecipeTime = time.getText().toString().trim();
                                        String Recipe_Description = desc.getText().toString().trim();
                                        String Recipe_Instructions = instructions.getText().toString().trim();
                                        String Recipe_Ingredients = ingredient.getText().toString().trim();
                                        String Recipe_No_Serving_People = serving.getText().toString().trim();
                                        //custom lines added
                                        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        if (mFirebaseUser != null) {
                                            currentUserID = mFirebaseUser.getUid(); //Do what you need to do with the id
                                        }

                                        //
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Recipe Uploaded Successfully ", Toast.LENGTH_LONG).show();
                                        RecipeModel imageUploadInfo = new RecipeModel(UUID, TempImageName, RecipeTime,
                                                Recipe_Description, Recipe_Instructions, Recipe_Ingredients, Recipe_No_Serving_People,
                                                photoLink[0]);
                                        String ImageUploadId = databaseReference.push().getKey();

                                        //little changes in line 233
                                        long time= System.currentTimeMillis();
                                        String timee=String.valueOf(time);
                                        databaseReference2.child(UUID).child("recipe").setValue(imageUploadInfo);

                                    }
                                });
//
                            }
                        });
            }catch (Exception e)
            {
                Log.d( "UploadImage",e.getMessage());
                e.printStackTrace();
            }


        } else {
            Toast.makeText(AddRecipeActivity.this, "Please Select Recipe Image or Add Recipe Name", Toast.LENGTH_LONG).show();
        }
    }
}