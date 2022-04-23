package com.example.fitrecipes.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitrecipes.Activities.LoginActivity;
import com.example.fitrecipes.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Recipe> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, List<Recipe> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recipesrvlayout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecipeModel recipeModel = mData.get(position).getRecipeModel();
        if(!LoginActivity.UUID.equals(recipeModel.getId()))
        {
            holder.delete_icon.setVisibility(View.GONE);
        }
        String RecipeId= mData.get(position).getRecipeId();
       Glide.with(holder.imageView).load(recipeModel.getRecipe_image()).placeholder(R.drawable.coffee_mugs).into(holder.imageView);
        holder.tv_Recipe_name.setText(recipeModel.name);
        holder.tvRecipeInstructions.setText(recipeModel.recipeI);
        holder.tvRecipeSrvPeople.setText(recipeModel.recipe_people);
        holder.tvRecipeIngredients.setText(recipeModel.getRecipeIng());
        holder.tvRecipeDescription.setText(recipeModel.recipeD);
        holder.tvRecipeTime.setText(recipeModel.recipeT);

        holder.delete_icon.setOnClickListener(view -> {
            FirebaseDatabase.getInstance().getReference().child("Recipess").child(RecipeId).removeValue();
            mData.remove(position);
            notifyDataSetChanged();
        });
        //ToDO  code for edit and delete func
     /*        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.iv_RecipePic.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                        .setExpanded(true,1100)
                        .create();

                View myview = dialogPlus.getHolderView();
                final EditText R_name = myview.findViewById(R.id.tv_R_name);
                final EditText R_time = myview.findViewById(R.id.tv_R_time);
                final EditText R_instr = myview.findViewById(R.id.tv_R_Ins);
                final EditText R_ingred = myview.findViewById(R.id.tv_R_Ing);
                final EditText R_srv_peop = myview.findViewById(R.id.tv_R_people);
                final EditText R_Desc = myview.findViewById(R.id.tv_R_desc);
                final EditText R_Url = myview.findViewById(R.id.tv_R_url);
                Button submit=myview.findViewById(R.id.btn_submit);

                R_name.setText(model.getName());
                R_time.setText(model.getRecipeT());
                R_instr.setText(model.getRecipeI());
                R_ingred.setText(model.getRecipeIng());
                R_srv_peop.setText(model.getRecipe_people());
                R_Desc.setText(model.getRecipeD());
                R_Url.setText(model.getRecipe_image());

                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("name",R_name.getText().toString());
                        map.put("recipeD",R_Desc.getText().toString());
                        map.put("recipeI",R_instr.getText().toString());
                        map.put("recipeIng",R_ingred.getText().toString());
                        map.put("recipeT",R_time.getText().toString());
                        map.put("recipe_image",R_Url.getText().toString());
                        map.put("recipe_people",R_srv_peop.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("recipes")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });


            }
        });


*/

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Recipe_name,tvRecipeTime,tvRecipeDescription,tvRecipeIngredients,tvRecipeSrvPeople,tvRecipeInstructions;
        ImageView imageView, delete_icon;

        ViewHolder(View itemView) {
            super(itemView);
            tv_Recipe_name = itemView.findViewById(R.id.tv_Recipe_name);
            tvRecipeTime = itemView.findViewById(R.id.tv_cook_time);
            tvRecipeDescription = itemView.findViewById(R.id.tv_Recipe_Description);
            tvRecipeIngredients = itemView.findViewById(R.id.tv_Recipe_Ingredients);
            tvRecipeSrvPeople = itemView.findViewById(R.id.tv_People);
            tvRecipeInstructions = itemView.findViewById(R.id.tv_Recipe_Instructions);
            imageView = itemView.findViewById(R.id.img1);
            delete_icon = itemView.findViewById(R.id.delete_icon);
        }

    }
}
