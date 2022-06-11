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
import java.util.List;

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
    private UserModel loggedInUser;
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
        databaseReference = firebaseDatabase.getReference("Favourites").child(USERID);
        databaseReference1 = firebaseDatabase.getReference().child("users");

        //      databaseReference = firebaseDatabase.getReference(USERS).child(USERID);
        recipes = new ArrayList<>();
        adapter = new OriginalRecipeAdapter(loggedInUser, FavouriteActivity.this);
        recyclerView.setAdapter(adapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //loggedInUser = dataSnapshot.getValue(UserModel.class);
                recipes.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = postSnapshot.getValue(Recipe.class);
                    //Recipe recipe = new Recipe(postSnapshot.getKey(), university);
                    recipes.add(recipe);
                }
                for(Recipe recipe: recipes) {
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Recipess").child(recipe.getId());
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            RecipeModel recipeModel = snapshot.getValue(RecipeModel.class);
                            Recipe recipe1 = new Recipe(snapshot.getKey(), recipeModel);
                            adapter.addRecipe(recipe1);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
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