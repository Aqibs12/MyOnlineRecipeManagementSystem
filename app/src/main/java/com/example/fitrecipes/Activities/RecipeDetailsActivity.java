package com.example.fitrecipes.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.fitrecipes.Models.Ingredient;
import com.example.fitrecipes.Models.Recipe;
import com.example.fitrecipes.Models.RecipeModel;
import com.example.fitrecipes.Models.UserModel;
import com.example.fitrecipes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

public class RecipeDetailsActivity extends AppCompatActivity {

    private RecipeModel recipeModel;
    private Recipe recipe;
    TextView name, desc, inst, cat, ing, ingTitle;
    ImageView image, share, ivFav;
    MaterialButton edit, btnDelete;
    String uuid;
    String ingToshow = "";
    private static ViewPager mPager;
    private static int currentPage = 0;
    String USERID;
    boolean isFav = false;
    String loggedInUser;
    UserModel userModel;
    String loggedInUserFavouriteRecipe = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        mPager = (ViewPager) findViewById(R.id.pager);
        share = findViewById(R.id.share);
        ingTitle = findViewById(R.id.ingTitle);
        edit = findViewById(R.id.edit);
        btnDelete = findViewById(R.id.btn_delete);
        image = findViewById(R.id.img);
        name = findViewById(R.id.name);
        desc = findViewById(R.id.desc);
        inst = findViewById(R.id.inst);
        cat = findViewById(R.id.cat);
        ing = findViewById(R.id.ing);
        ivFav = findViewById(R.id.favRec);
        uuid = LoginActivity.UUID;
        recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        recipeModel = recipe.getRecipeModel();
        userModel = (UserModel) getIntent().getSerializableExtra("user");
        loggedInUser = userModel.getId();
        uuid = getIntent().getStringExtra("uuid");
       /* for (int i=0;i<recipeModel.getIngredientModelArrayList().size();i++){
            ingToshow=ingToshow+"\n"+recipeModel.getRecipeIng();

        }*/
        ingToshow = ingToshow + "\n" + recipeModel.getRecipeIng();
        Glide.with(getApplicationContext()).load(recipeModel.getRecipe_image()).into(image);
        name.setText(recipeModel.getName() + " (Est. time: " + recipeModel.getRecipeT() + ")");
        desc.setText(recipeModel.getRecipeD());
        inst.setText(recipeModel.getRecipeI());
        cat.setText(recipeModel.getRecipeCategory());
        ingTitle.setText("Ingredients(" + recipeModel.getRecipeIng() + " Quantity)");

        StringBuilder stringBuilder = new StringBuilder();
        for (Ingredient ingredient : recipeModel.getIngredientList()) {
            stringBuilder.append(ingredient.getName() + " (" + ingredient.getQuantity() + " " + ingredient.getUnitName() + ")\n");
        }
        ing.setText(stringBuilder.toString());

        //   ing.setText((CharSequence) recipeModel.getIngredientList());


        mPager.setVisibility(View.GONE);
        image.setVisibility(View.VISIBLE);
        init();
        if (recipeModel.getUser() != null) {
            if (!loggedInUser.equals(recipeModel.getUser().getId())) {
                edit.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);

            }
        }
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditRecipeActivity.class);
                intent.putExtra("recipe", recipe);
                intent.putExtra("user", userModel);
                startActivity(intent);
                finish();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecipeDetailsActivity.this);
                builder.setMessage("Are you sure you want to delete this Recipe?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseDatabase.getInstance().getReference().child("Recipes").child(recipe.getRecipeId()).removeValue();
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_SUBJECT, recipeModel.getName() + " recipe");
                intent.putExtra(Intent.EXTRA_TEXT, recipeModel.getName() +
                        "\n Ingredients : " + ing.getText() +
                        "\n Image : " + recipeModel.getRecipe_image() +
                        "\n Description : " + recipeModel.getRecipeD() +
                        "\n Instructions : " + recipeModel.getRecipeI()
                );
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Choose an app :"));
            }
        });
        // favourite
        ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //recipeModel.getUser().getId()
                if (isFav) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipeDetailsActivity.this);
                    builder.setMessage("Are you sure you want to remove this Recipe from favourite list?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    FirebaseDatabase.getInstance().getReference().child("Favourite").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(recipeModel.getId()).removeValue();
                                    ivFav.setColorFilter(getResources().getColor(R.color.black));
                                    isFav = false;
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                } else {
                    FirebaseDatabase.getInstance().getReference().child("Favourite")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(recipeModel.getId())
                            .setValue(recipeModel.getId()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RecipeDetailsActivity.this, "Recipe added to favourites", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RecipeDetailsActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }


            }
        });
        //fetching favourite id from db
        FirebaseDatabase firebaseDatabase2 = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase2 = firebaseDatabase2.getReference().child("Favourite").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                .child(recipeModel.getId());
        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    loggedInUserFavouriteRecipe = snapshot.child(recipeModel.getId()).getValue().toString();
//                    loggedInUserFavouriteRecipe = snapshot.getValue().toString();

                    if (loggedInUserFavouriteRecipe.equals(recipeModel.getId())) {
                        Toast.makeText(RecipeDetailsActivity.this, "Id Matched", Toast.LENGTH_SHORT).show();

                        ivFav.setColorFilter(getResources().getColor(R.color.red));
                        isFav = true;
                        /*if(ivFav.getColorFilter()==ivFav.getColorFilter(getResources().getColor(R.color.red))){

                        }*/
                    }
                } catch (Exception e) {
                    Toast.makeText(RecipeDetailsActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RecipeDetailsActivity.this, "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //fetching end
    }

    public void onBackPress(View view) {
        onBackPressed();
    }

    private void init() {

      /*  String loginUserID = SessionManager.getStringPref(HelperKeys.USER_ID, this);
        if(!loginUserID.equals(recipeModel.getUser_id()+"")){
            edit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);

        }
*/


        /* mPager.setAdapter(new SlidingImage_Adapter(this, recipeModel.getImagesModelArrayList(),"not"));*/

       /* CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

       // recipeModel.getImagesModelArrayList().size();
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
*/
    }
}