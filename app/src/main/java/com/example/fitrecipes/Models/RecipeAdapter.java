package com.example.fitrecipes.Models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitrecipes.Activities.AddRecipeActivity;
import com.example.fitrecipes.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

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
//        holder.tvRecipeTime.setText("Recipe Time: " + model.getRecipeT());
//        holder.tvRecipeDescription.setText("Recipe Description: " + model.getRecipeD());
                holder.tvRecipeDescription.setText("Recipe Description: " + model.getRecipeD());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipesrvlayout, parent, false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecipeTime, tvRecipeDescription;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecipeTime = itemView.findViewById(R.id.tvRecipeTime);
            tvRecipeDescription = itemView.findViewById(R.id.tvRecipeDescription);
        }
    }
}
