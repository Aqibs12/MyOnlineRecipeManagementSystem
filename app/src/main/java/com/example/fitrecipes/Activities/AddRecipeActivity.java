package com.example.fitrecipes.Activities;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitrecipes.Models.RecipeModel;
import com.example.fitrecipes.R;
import com.example.fitrecipes.Util.ValidationChecks;
/*import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;*/
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddRecipeActivity extends AppCompatActivity {
    Context context;
    Spinner spin;
    CircleImageView profile_image;
    EditText name, serving, time, desc, instructions, ingredient;
    private Uri filePathUri;
    MaterialButton btn, addIng;
    ValidationChecks validationChecks = new ValidationChecks();
    RecyclerView recyclerIngreditent,recyclerImages,rvRecipe;
    StorageReference storageReference;
    DatabaseReference databaseReference,databaseReference2;
    int Image_Request_Code = 1;
    ProgressDialog progressDialog ;
    public static String UUID = "";
    private String myauth;
    String currentUserId;
    private String currentUserID="";
    private String currentUserID2="";
    ValueEventListener listener;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        context = this;
        rvRecipe=findViewById(R.id.recyclerImages);
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Images");
        // add child path
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Images");
        progressDialog = new ProgressDialog(AddRecipeActivity.this);
        currentUserID2= getIntent().getExtras().getString("uuid");
        init();
        databaseReference = FirebaseDatabase.getInstance().getReference("spinner");
        list=new ArrayList<String>();
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,list);
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

    /*//fetching data from firebase
        FirebaseRecyclerOptions<RecipeModel> options = new FirebaseRecyclerOptions.Builder<RecipeModel>().setQuery(databaseReference2, RecipeModel.class).build();
        FirebaseRecyclerAdapter<RecipeModel, AddRecipeViewHolder> adapter = new FirebaseRecyclerAdapter<RecipeModel, AddRecipeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AddRecipeViewHolder holder, int position, @NonNull RecipeModel model) {
                holder.tvRecipeTime.setText("Recipe Time: " + model.getRecipeT());
                holder.tvRecipeDescription.setText("Recipe Description: " + model.getRecipeD());
            }

            @NonNull
            @Override
            public AddRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipesrvlayout, parent, false);
                return new AddRecipeViewHolder(view);
            }
        };
        rvRecipe.setAdapter(adapter);
        adapter.startListening();
        //fetching ended
 */   }

    private void fetchUserData() {
       listener = databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot mydata : snapshot.getChildren())
                   list.add(mydata.getValue().toString());
               adapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }

    private void init(){
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePathUri = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathUri);
                profile_image.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


    public void UploadImage() {

        if (filePathUri != null) {

            progressDialog.setTitle("Recipe is Uploading...");
            progressDialog.show();

            StorageReference storageReference2 = FirebaseStorage.getInstance().getReference().child(System.currentTimeMillis() + "." + GetFileExtension(filePathUri));
            storageReference2.putFile(filePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String TempImageName = name.getText().toString().trim();
                            String RecipeTime = time.getText().toString().trim();
                            String Recipe_Description = desc.getText().toString().trim();
                            String Recipe_Instructions = instructions.getText().toString().trim();
                            String Recipe_Ingredients  = ingredient.getText().toString().trim();
                            String Recipe_No_Serving_People = serving.getText().toString().trim();
                            //custom lines added
                            FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if(mFirebaseUser != null) {
                                currentUserID = mFirebaseUser.getUid(); //Do what you need to do with the id
                            }
                            //
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Recipe Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            RecipeModel imageUploadInfo = new RecipeModel(currentUserID2,TempImageName,RecipeTime,
                                    Recipe_Description,Recipe_Instructions,Recipe_Ingredients,Recipe_No_Serving_People,
                                    taskSnapshot.getUploadSessionUri().toString());
                            /*RecipeModel imageUploadInfo = new RecipeModel(FirebaseAuth.getInstance().getCurrentUser().getUid(),TempImageName,RecipeTime,
                                    Recipe_Description,Recipe_Instructions,Recipe_Ingredients,Recipe_No_Serving_People,
                                    taskSnapshot.getUploadSessionUri().toString());*/
                            String ImageUploadId = databaseReference.push().getKey();
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                        }
                    });
        }
        else {

            Toast.makeText(AddRecipeActivity.this, "Please Select Recipe Image or Add Recipe Name", Toast.LENGTH_LONG).show();

        }
    }
// view holder class
/*public static class AddRecipeViewHolder extends RecyclerView.ViewHolder {

    TextView tvRecipeTime,tvRecipeDescription;

    public AddRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        tvRecipeTime = itemView.findViewById(R.id.tvRecipeTime);
        tvRecipeDescription = itemView.findViewById(R.id.tvRecipeDescription);

    }*/
}
    // view holder class end


