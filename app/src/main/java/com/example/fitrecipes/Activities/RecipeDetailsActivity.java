package com.example.fitrecipes.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.fitrecipes.Adapters.SlidingImage_Adapter;
import com.example.fitrecipes.Models.RecipeModel;
import com.example.fitrecipes.R;
import com.example.fitrecipes.Util.DatabaseHelper;
import com.example.fitrecipes.Util.HelperKeys;
import com.example.fitrecipes.Util.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.viewpagerindicator.CirclePageIndicator;

public class RecipeDetailsActivity extends AppCompatActivity {

    private RecipeModel recipeModel;
    TextView name,desc,inst,cat,ing,ingTitle;
    ImageView image,share;
    MaterialButton edit,btnDelete;
    String ingToshow="";
    private static ViewPager mPager;
    private static int currentPage = 0;
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
        recipeModel= (RecipeModel) getIntent().getSerializableExtra("model");
        for (int i=0;i<recipeModel.getIngredientModelArrayList().size();i++){
            ingToshow=ingToshow+"\n"+recipeModel.getIngredientModelArrayList().get(i).getIngredient();
        }
        Glide.with(getApplicationContext()).load(recipeModel.getImagesModelArrayList().get(0).getImage()).into(image);
        name.setText(recipeModel.getName()+"("+recipeModel.getCook_time()+" min)");
        desc.setText(recipeModel.getDescription());
        inst.setText(recipeModel.getInstruc());
        cat.setText(recipeModel.getCategory());
        ingTitle.setText("Ingredients("+recipeModel.getServing_persons()+" Serving)");
        ing.setText(ingToshow);
//        if (recipeModel.getImagesModelArrayList().size()>1){
            mPager.setVisibility(View.VISIBLE);
            image.setVisibility(View.GONE);
//        }
//        else {
//            mPager.setVisibility(View.GONE);
//            image.setVisibility(View.VISIBLE);
//        }
        init();
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), UpdateRecipeActivity.class);
                intent.putExtra("model",recipeModel);
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
                                DatabaseHelper databaseHelper = new DatabaseHelper(RecipeDetailsActivity.this);
                                databaseHelper.deleteRecipe(recipeModel.getId());
                                Toast.makeText(RecipeDetailsActivity.this,"Recipe Deleted Successfully",Toast.LENGTH_LONG).show();
                                LocalBroadcastManager.getInstance(RecipeDetailsActivity.this).sendBroadcast(new Intent("delete_recipe"));
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
                        "\n Instructions : "+recipeModel.getInstruc()
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

        String loginUserID = SessionManager.getStringPref(HelperKeys.USER_ID, this);
        if(!loginUserID.equals(recipeModel.getUser_id()+"")){
            edit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);

        }



        mPager.setAdapter(new SlidingImage_Adapter(this, recipeModel.getImagesModelArrayList(),"not"));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        recipeModel.getImagesModelArrayList().size();
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

    }
}