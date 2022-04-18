// this adapter was made for firebase_recyclerview not for recyclerview

package com.example.fitrecipes.Models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitrecipes.Activities.AddRecipeActivity;
import com.example.fitrecipes.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeAdapter extends FirebaseRecyclerAdapter<RecipeModel,RecipeAdapter.MyViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RecipeAdapter(@NonNull FirebaseRecyclerOptions<RecipeModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull RecipeModel model) {
                holder.tvRecipeTime.setText("Recipe Time: " + model.getRecipeT());
                holder.tvRecipeName.setText("Recipe Name" + model.getName());
                holder.tvRecipeDescription.setText("Recipe Description: " + model.getRecipeD());
        Picasso.get().load(model.getRecipe_image()).into(holder.iv_RecipePic);
                //Glide.with(holder.iv_RecipePic.getContext()).load(model.getRecipe_image()).into(holder.iv_RecipePic);
                holder.tvRecipeInstructions.setText("Recipe Instructions" + model.getRecipeI());
                holder.tvRecipeSrvPeople.setText("Recipe Serving People"+ model.getRecipe_people());
                holder.tvRecipeIngredients.setText("Recipe Ingredients" + model.getRecipeIng());


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipesrvlayout, parent, false);
        return new MyViewHolder(view);
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

