package com.example.fitrecipes.Activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitrecipes.Activities.adapters.OriginalRecipeAdapter;
import com.example.fitrecipes.Models.ImagesModel;
import com.example.fitrecipes.Models.Recipe;
import com.example.fitrecipes.Models.RecipeAdapter;
import com.example.fitrecipes.Models.RecipeModel;
import com.example.fitrecipes.Models.UserModel;
import com.example.fitrecipes.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.TextSliderView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private SliderLayout sliderLayout;
    private RecyclerView recyclerView;
    ArrayList<RecipeModel> recipeModelArrayList;
    ArrayList<RecipeModel> sliderRecipeList;
    SlidingRootNav slidingRootNav;
    TextView name, phone, emailAddress, tv_changePass;
    ImageView iv_pic, iv_edPic;
    RecipeAdapter recipeAdapter;
    //    EditText etSearch;
    SearchView etSearch;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageTask uploadTask;
    Context context;
    private EditText et_search;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, databaseReference3, databaseReference2;
    private static final String USERS = "users";
    private String myUri = "";
    private String uuid = "";
    private String USERID = "";
    ArrayList<RecipeModel> recipeModelArrayList2;
    OriginalRecipeAdapter adapter;
    ProgressBar progressBar;
    String EditRecipeId;
    private UserModel loggedInUser;
    private ArrayList<Recipe> recipes;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        progressBar = findViewById(R.id.progressB);
        databaseReference3 = FirebaseDatabase.getInstance().getReference();

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withMenuLayout(R.layout.activity_drawer)
                .withMenuOpened(false)
                .inject();
        final List<String> mArrayList = new ArrayList<>();
        context = this;
        uuid = LoginActivity.UUID;
        USERID = getIntent().getExtras().getString("uuid");

        TextView name1 = findViewById(R.id.name);
        etSearch = findViewById(R.id.et_search);
        sliderLayout = findViewById(R.id.slider);
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3333);
        recyclerView = findViewById(R.id.recyclerview);
        recipeModelArrayList = new ArrayList();
        recipeModelArrayList2 = new ArrayList();
        sliderRecipeList = new ArrayList();

        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        List<String> data = new ArrayList<>();

        List<RecipeModel> mData = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference3 = firebaseDatabase.getReference().child("Recipess");


        recipes = new ArrayList<>();
        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sliderLayout.removeAllSliders();
                mData.clear();
                recipes.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    RecipeModel university = postSnapshot.getValue(RecipeModel.class);
                    Recipe recipe = new Recipe(postSnapshot.getKey(), university);
                    recipes.add(recipe);
                    // here you can access to name property like university.name
                }
               /* for (int i = 0; i < recipes.size(); i++) {
                    //if (uuid.equals(recipes.get(i).getRecipeModel().getId())) {
                        //mData.add(recipes.get(i).getRecipeModel());
                        sliderRecipeList.add(recipes.get(i).getRecipeModel());
                    //}
                }*/

                setSlider();
                adapter = new OriginalRecipeAdapter(recipes,uuid,HomeActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(USERS);
        databaseReference2 = firebaseDatabase.getReference(USERS).child(USERID);
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                loggedInUser = snapshot.getValue(UserModel.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setListeners();
        init();

        // search function
        etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processSearch(s);
                return false;
            }
        });
        //search function end
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //things added stop
        StorageReference riversRef = storageReference.child("images");
        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            final String[] photoLink = {""};

            @Override
            public void onSuccess(Uri uri) {
                Glide.with(iv_pic).load(photoLink[0]).into(iv_pic);
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            final String[] photoLink = {""};

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (uuid.equals(ds.getKey())) {
                        emailAddress.setText(ds.child("email").getValue(String.class));
                        name.setText(ds.child("name").getValue(String.class));
                        phone.setText(ds.child("phone").getValue(String.class));
                        Glide.with(iv_pic).load(photoLink[0]).into(iv_pic);
          //              Glide.with(HomeActivity.this).load(photoLink[0]).into(iv_pic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setSlider(){
        sliderLayout.removeAllSliders();
        for (Recipe recipe:recipes) {
            RecipeModel recipeModel = recipe.getRecipeModel();
            TextSliderView textSliderView = new TextSliderView(context);
            // initialize a SliderLayout
            textSliderView
                    .description(recipeModel.getName())
                    .image(recipeModel.getRecipe_image());

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", recipeModel.getName());


            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {

                    // open detail page activity and pass the clicked recipe object in the intent *//*
                    Intent it=new Intent(context, RecipeDetailsActivity.class);
                    it.putExtra("recipe",recipe);
                    it.putExtra("uuid",uuid);
                    startActivity(it);
                }
            });

           sliderLayout.addSlider(textSliderView);

        }
    }

    private void processSearch(String s) {
        FirebaseRecyclerOptions<RecipeModel> options = new FirebaseRecyclerOptions.Builder<RecipeModel>().
                setQuery(FirebaseDatabase.getInstance().getReference()
                        .child("Recipess").orderByChild("name").startAt(s).
                                endAt(s + "\uf8ff"), RecipeModel.class).build();
        recipeAdapter = new RecipeAdapter(options);
        recipeAdapter.startListening();
        recyclerView.setAdapter(recipeAdapter);
    }

    private void setListeners() {
        findViewById(R.id.tv_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutUser();
            }
        });
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,AddRecipeActivity.class);
                intent.putExtra("user",loggedInUser);
                intent.putExtra("uuid",uuid);
                startActivity(intent);
                //startActivity(new Intent(context, AddRecipeActivity.class).putExtra("uuid", USERID));
            }
        });
        findViewById(R.id.iv_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingRootNav.openMenu();
            }
        });
        findViewById(R.id.btn_my_recipes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, MyRecipesActivity.class));
            }
        });

        findViewById(R.id.btn_edit_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ProfileActivity.class));
            }
        });
        findViewById(R.id.iv_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
    }

    private void init() {

        name = findViewById(R.id.tv_name);
        emailAddress = findViewById(R.id.tv_email);
        phone = findViewById(R.id.tv_phone);
        iv_pic = findViewById(R.id.iv_profilePic);
        storage = FirebaseStorage.getInstance();
        iv_edPic = findViewById(R.id.iv_edit);
        storageReference = storage.getReference();

    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            iv_pic.setImageURI(imageUri);
            uploadPicture();
        }
    }

    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadPicture() {
        if (imageUri != null) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Uploading Image.....");
            pd.show();

            try {
                storageReference = FirebaseStorage.getInstance().getReference().child(System.currentTimeMillis() + "." + GetFileExtension(imageUri));
                storageReference.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                pd.dismiss();
                                final String[] photoLink = {""};
                                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        photoLink[0] = uri.toString();
                                        Glide.with(iv_pic).load(photoLink[0]).into(iv_pic);
                                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();
                                        ImagesModel imageUploadInfo = new ImagesModel(uuid,
                                                photoLink[0]);
                                        databaseReference.child("recipes").setValue(imageUploadInfo);
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(context, "Failed To Upload", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                pd.setMessage(" Progress:" + (int) progressPercent + "%");

                            }
                        });
            } catch (Exception e) {

            }
        } else {
            Toast.makeText(HomeActivity.this, "Please Select Image", Toast.LENGTH_LONG).show();

        }
    }

    private void signOutUser() {
        mAuth.signOut();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        uuid = LoginActivity.UUID;
    }
}