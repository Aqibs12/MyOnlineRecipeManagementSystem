// this adapter was made for firebase_recyclerview not for recyclerview

package com.example.fitrecipes.Models;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitrecipes.Activities.AddRecipeActivity;
import com.example.fitrecipes.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.ViewHolder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeAdapter extends FirebaseRecyclerAdapter<RecipeModel,RecipeAdapter.MyViewHolder> {
    private List<RecipeModel> exampleList;

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
    protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull RecipeModel model) {
                holder.tvRecipeTime.setText("Recipe Time: " + model.getRecipeT());
                holder.tvRecipeName.setText("Recipe Name" + model.getName());
                holder.tvRecipeDescription.setText("Recipe Description: " + model.getRecipeD());
                // Picasso.get().load(model.getRecipe_image()).into(holder.iv_RecipePic);
                Glide.with(holder.iv_RecipePic.getContext()).load(model.getRecipe_image()).into(holder.iv_RecipePic);
                holder.tvRecipeInstructions.setText("Recipe Instructions" + model.getRecipeI());
                holder.tvRecipeSrvPeople.setText("Recipe Serving People"+ model.getRecipe_people());
                holder.tvRecipeIngredients.setText("Recipe Ingredients" + model.getRecipeIng());

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


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.iv_RecipePic.getContext());
                builder.setTitle("Delete Recipe");
                builder.setMessage("Delete...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("recipes")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
            }
        });


*/
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipesrvlayout, parent, false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecipeTime, tvRecipeDescription,tvRecipeName,tvRecipeIngredients,tvRecipeInstructions,tvRecipeSrvPeople;
        ImageView iv_RecipePic,edit,delete;;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_RecipePic = itemView.findViewById(R.id.img1);
            tvRecipeName = itemView.findViewById(R.id.tv_Recipe_name);
            tvRecipeTime = itemView.findViewById(R.id.tv_cook_time);
            tvRecipeDescription = itemView.findViewById(R.id.tv_Recipe_Description);
            tvRecipeIngredients = itemView.findViewById(R.id.tv_Recipe_Ingredients);
            tvRecipeSrvPeople = itemView.findViewById(R.id.tv_People);
            tvRecipeInstructions = itemView.findViewById(R.id.tv_Recipe_Instructions);
            edit=(ImageView)itemView.findViewById(R.id.edit_icon);
            delete=(ImageView)itemView.findViewById(R.id.delete_icon);
        }

    }
}

