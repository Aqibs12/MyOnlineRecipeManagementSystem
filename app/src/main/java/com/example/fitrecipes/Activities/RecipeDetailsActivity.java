package com.example.fitrecipes.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.fitrecipes.Models.Recipe;
import com.example.fitrecipes.Models.RecipeModel;
import com.example.fitrecipes.Models.UserModel;
import com.example.fitrecipes.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

public class RecipeDetailsActivity extends AppCompatActivity {

    private RecipeModel recipeModel;
    private Recipe recipe;
    TextView name,desc,inst,cat,ing,ingTitle;
    ImageView image,share;
    MaterialButton edit,btnDelete;
    String uuid;
    String ingToshow="";
    private static ViewPager mPager;
    private static int currentPage = 0;
    String USERID;
    String loggedInUser;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        mPager = (ViewPager) findViewById(R.id.pager);
        share=findViewById(R.id.share);
        ingTitle=findViewById(R.id.ingTitle);
        edit=findViewById(R.id.edit);
        btnDelete = findViewById(R.id.btn_delete);
        image=findViewById(R.id.img);
        name=findViewById(R.id.name);
        desc=findViewById(R.id.desc);
        inst=findViewById(R.id.inst);
        cat=findViewById(R.id.cat);
        ing=findViewById(R.id.ing);
        uuid = LoginActivity.UUID;
        recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        recipeModel = recipe.getRecipeModel();
        userModel = (UserModel) getIntent().getSerializableExtra("user");
        loggedInUser = userModel.getId();
        uuid = getIntent().getStringExtra("uuid");
       /* for (int i=0;i<recipeModel.getIngredientModelArrayList().size();i++){
            ingToshow=ingToshow+"\n"+recipeModel.getRecipeIng();

        }*/
        ingToshow=ingToshow+"\n"+recipeModel.getRecipeIng();
        Glide.with(getApplicationContext()).load(recipeModel.getRecipe_image()).into(image);
        name.setText(recipeModel.getName()+"("+recipeModel.getRecipeT()+" min)");
        desc.setText(recipeModel.getRecipeD());
        inst.setText(recipeModel.getRecipeI());
        cat.setText(recipeModel.getRecipeCategory());
        ingTitle.setText("Ingredients("+recipeModel.getRecipe_people()+" Serving)");
        ing.setText(ingToshow);
      /*if (recipeModel.getImagesModelArrayList().size()>1){
            mPager.setVisibility(View.VISIBLE);
            image.setVisibility(View.GONE);
       }
        else {
            mPager.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
        }*/
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
                Intent intent= new Intent(getApplicationContext(), EditRecipeActivity.class);
                intent.putExtra("recipe",recipe);
                intent.putExtra("user",userModel);
                startActivity(intent);
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
                                FirebaseDatabase.getInstance().getReference().child("Recipess").child(recipe.getRecipeId()).removeValue();
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
                intent.putExtra(Intent.EXTRA_SUBJECT, recipeModel.getName()+" recipe");
                intent.putExtra(Intent.EXTRA_TEXT, recipeModel.getName()+
                        "\n Ingredients : "+ingToshow+
                        "\n Instructions : "+recipeModel.getRecipeI()
                );
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Choose an app :"));
            }
        });
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