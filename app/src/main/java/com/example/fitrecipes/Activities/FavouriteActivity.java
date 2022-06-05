package com.example.fitrecipes.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fitrecipes.Models.Recipe;
import com.example.fitrecipes.Models.RecipeModel;
import com.example.fitrecipes.Models.UserModel;
import com.example.fitrecipes.R;
import com.example.fitrecipes.adapters.OriginalRecipeAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    OriginalRecipeAdapter adapter;
    Context context;
    UserModel userModel;
    private String uuid = "";
    private String USERID = "";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,databaseReference1;
    private static final String USERS = "users";
    private String currentUserID = "";
    ValueEventListener listener;
    private ArrayList<Recipe> recipes;
    TextView tvLoggedUser;
    ImageView iv_LoggedUserPic, iv_BackPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        init();
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        userModel = (UserModel) getIntent().getSerializableExtra("user");
        uuid = LoginActivity.UUID;
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserID = firebaseUser.getUid();
        USERID = getIntent().getExtras().getString("uuid");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("likes");
        databaseReference1 = firebaseDatabase.getReference().child("users");

        //      databaseReference = firebaseDatabase.getReference(USERS).child(USERID);
        recipes = new ArrayList<>();
        listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (currentUserID != null) {
                    recipes.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        RecipeModel recipeModel = postSnapshot.getValue(RecipeModel.class);
                        Recipe recipe = new Recipe(postSnapshot.getKey(), recipeModel);

                        if (recipe.getRecipeModel().getUser().getId().equals(USERID)) {
                            recipes.add(recipe);
                        }
                    }
                }

                ((TextView) findViewById(R.id.tv_total_recipes)).setText("Total Recipes: "+recipes.size());
                adapter = new OriginalRecipeAdapter(recipes,userModel,FavouriteActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference1.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (uuid.equals(ds.getKey())) {
                        tvLoggedUser.setText(ds.child("name").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        iv_BackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init(){
        recyclerView = findViewById(R.id.recyclerview_MyRecipes);
        iv_BackPress = findViewById(R.id.iv_back);
        iv_LoggedUserPic = findViewById(R.id.iv_loggedPic);
        tvLoggedUser = findViewById(R.id.tv_loggedInUser);
    }
}