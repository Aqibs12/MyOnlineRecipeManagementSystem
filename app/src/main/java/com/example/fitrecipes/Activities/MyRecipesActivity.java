package com.example.fitrecipes.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.fitrecipes.Activities.adapters.OriginalRecipeAdapter;
import com.example.fitrecipes.Models.Recipe;
import com.example.fitrecipes.Models.RecipeModel;
import com.example.fitrecipes.Models.UserModel;
import com.example.fitrecipes.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyRecipesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    OriginalRecipeAdapter adapter;
    Context context;
    UserModel userModel;
    private String uuid = "";
    private String USERID = "";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private static final String USERS = "users";
    private String currentUserID = "";
    private UserModel loggedInUser;
    ValueEventListener listener;
    private ArrayList<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);
        recyclerView = findViewById(R.id.recyclerview_MyRecipes);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        userModel = (UserModel) getIntent().getSerializableExtra("user");
        uuid = LoginActivity.UUID;
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserID = firebaseUser.getUid();
        USERID = getIntent().getExtras().getString("uuid");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Recipess");
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
                      if (recipe.getRecipeModel().getId().equals(currentUserID)) {
                            recipes.add(recipe);
                        }
                    }
                }

               // adapter.notifyDataSetChanged();
                adapter = new OriginalRecipeAdapter(recipes,uuid,MyRecipesActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}