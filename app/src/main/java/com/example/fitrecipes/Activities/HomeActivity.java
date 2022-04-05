package com.example.fitrecipes.Activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.fitrecipes.Adapters.RecipeAdapter;
import com.example.fitrecipes.Models.RecipeModel;
import com.example.fitrecipes.R;
import com.example.fitrecipes.Util.DatabaseHelper;
import com.example.fitrecipes.Util.HelperKeys;
import com.example.fitrecipes.Util.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecipeModel recipeModel;
    int totalsize;
    private SliderLayout sliderLayout;
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    ArrayList<RecipeModel> recipeModelArrayList;
    ArrayList<RecipeModel> sliderRecipeList;
    SlidingRootNav slidingRootNav;
    TextView name,phone,emailAddress, firstLetter;
    Context context;
    private EditText et_search;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private static final String USERS = "users";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withMenuLayout(R.layout.activity_drawer)
                .withMenuOpened(false)
                .inject();

        context = this;
        TextView name1 = findViewById(R.id.name);
        et_search = findViewById(R.id.et_search);
        name1.setText("Hi " + "" + SessionManager.getStringPref(HelperKeys.USER_NAME, context) + " ,Welcome to FitRecipes");
        sliderLayout = findViewById(R.id.slider);
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3333);
        recyclerView = findViewById(R.id.recyclerview);
        recipeModelArrayList = new ArrayList();
        sliderRecipeList = new ArrayList();
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recipeAdapter = new RecipeAdapter(recipeModelArrayList,context);
        recyclerView.setAdapter(recipeAdapter);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        name=findViewById(R.id.tv_name);
        emailAddress=findViewById(R.id.tv_email);
        phone=findViewById(R.id.tv_phone);
        firstLetter = findViewById(R.id.tv_first_letter);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(USERS);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    if(ds.child("email").getValue().equals(email));
                    emailAddress.setText(ds.child("email").getValue(String.class));
                    name.setText(ds.child("name").getValue(String.class));
                    phone.setText(ds.child("phone").getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (recipeAdapter != null) {

                    recipeAdapter.getFilter().filter(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (recipeAdapter != null) {
                    recipeAdapter.getFilter().filter(s);
                }
            }
        });
        findViewById(R.id.tv_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                signOutUser();
            }
        });
        findViewById(R.id.tv_my_recipes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, MyRecipesActivity.class));
            }
        });
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, AddRecipeActivity.class));
            }
        });
        findViewById(R.id.iv_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingRootNav.openMenu();
            }
        });

        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingRootNav.closeMenu();
            }
        });

        findViewById(R.id.btn_my_recipes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,MyRecipesActivity.class));
            }
        });

        findViewById(R.id.btn_edit_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,ProfileActivity.class));
            }
        });

    }

    private void signOutUser() {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void updateView (){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        recipeModelArrayList.clear();
        recipeModelArrayList.addAll(databaseHelper.getAllRecipes());
        recipeAdapter.notifyDataSetChanged();

        // HashMap<String, String> file_maps = new HashMap<String, String>();

        List<RecipeModel> recipeModelList2 = new ArrayList<>(recipeModelArrayList);
        Collections.shuffle(recipeModelList2);
        if (recipeModelArrayList.size() == 0) {
            Toast.makeText(context, "Please add some recipe data first", Toast.LENGTH_SHORT).show();
        }

        if(recipeModelList2.size()>=8)
            totalsize = 8;
        else
            totalsize = recipeModelList2.size();


        /** here you need to implement the logic to limit the size to maximum 8*/
        for (int i = 0;  i < totalsize; i++) {
            Log.d("abc",i+"");
            //file_maps.put(i+"", recipeModelArrayList.get(i).getImagesModelArrayList().get(0).getImage());
            sliderRecipeList.add(recipeModelList2.get(i));
        }


        sliderLayout.removeAllSliders();
        for (RecipeModel recipeModel:sliderRecipeList) {

            TextSliderView textSliderView = new TextSliderView(context);
            // initialize a SliderLayout
            textSliderView
                    .description(recipeModel.getName())
                    .image(new File(recipeModel.getImagesModelArrayList().get(0).getImage()))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", recipeModel.getName());


            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {

                    /** open detail page activity and pass the clicked recipe object in the intent */
                    Intent it=new Intent(context, RecipeDetailsActivity.class);
                    it.putExtra("model",recipeModel);
                    startActivity(it);
                }
            });

            sliderLayout.addSlider(textSliderView);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }
}