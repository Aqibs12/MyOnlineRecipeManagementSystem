// this adapter for made for recyclerview to fetch firebase data
package com.example.fitrecipes.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitrecipes.Activities.MyRecipesActivity;
import com.example.fitrecipes.R;

import java.util.ArrayList;

public class RecipeModelAdapter extends RecyclerView.Adapter<RecipeModelAdapter.MyViewHolder> {
Context context;
ArrayList<RecipeModel> recipeModels;

    public RecipeModelAdapter(Context context, ArrayList<RecipeModel> recipeModelslist) {
        this.context = context;
        this.recipeModels = recipeModelslist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipesrvlayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
RecipeModel recipelist=recipeModels.get(position);
holder.tvRecipeTime.setText(recipelist.getRecipeT());
holder.tvRecipeName.setText(recipelist.getName());
       holder.tvRecipeDescription.setText("Recipe Description: " + recipelist.getRecipeD());

                Glide.with(holder.iv_RecipePic.getContext()).load(recipelist.getRecipe_image()).into(holder.iv_RecipePic);
                holder.tvRecipeInstructions.setText("Recipe Instructions" + recipelist.getRecipeI());
                holder.tvRecipeSrvPeople.setText("Recipe Serving People"+ recipelist.getRecipe_people());
                holder.tvRecipeIngredients.setText("Recipe Ingredients" + recipelist.getRecipeIng());
    }

    @Override
    public int getItemCount() {
        return recipeModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecipeTime, tvRecipeDescription,tvRecipeName,tvRecipeIngredients,tvRecipeInstructions,tvRecipeSrvPeople;
        ImageView iv_RecipePic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_RecipePic = itemView.findViewById(R.id.img1);
            tvRecipeName = itemView.findViewById(R.id.tv_Recipe_name);
            tvRecipeTime = itemView.findViewById(R.id.tv_cook_time);
            tvRecipeDescription = itemView.findViewById(R.id.tv_Recipe_Description);
            tvRecipeIngredients = itemView.findViewById(R.id.tv_Recipe_Ingredients);
            tvRecipeSrvPeople = itemView.findViewById(R.id.tv_People);
            tvRecipeInstructions = itemView.findViewById(R.id.tv_Recipe_Instructions);
        }
    }
}
