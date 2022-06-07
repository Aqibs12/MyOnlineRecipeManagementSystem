package com.example.fitrecipes.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitrecipes.Activities.RecipeDetailsActivity;
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
import java.util.List;


public class OriginalRecipeAdapter extends RecyclerView.Adapter<OriginalRecipeAdapter.RecipeViewHolder> implements Filterable {

   // private List<Recipe> mData;
    private List<Recipe> exampleList;
    public List<Recipe> exampleListFull;
    ImageView like_btn;
    TextView like_text;
    DatabaseReference likereference;
    Boolean testclick=false;

    Context context;
    UserModel userModel;
    public OriginalRecipeAdapter(List<Recipe> exampleList, UserModel userModel, Context context)
    {
        this.exampleList = exampleList;
        this.context =  context;
        this.userModel = userModel;
        this.exampleListFull=exampleList;

    }
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_main_recipe,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        RecipeModel recipeModel = exampleListFull.get(position).getRecipeModel();
        recipeModel.getId();
        String RecipeId = exampleListFull.get(position).getRecipeId();
        Glide.with(context).load(exampleListFull.get(position).getRecipeModel().getRecipe_image()).into(holder.img);
        holder.tvRecipeName.setText(exampleListFull.get(position).getRecipeModel().getName());
        holder.tvCategory.setText(exampleListFull.get(position).getRecipeModel().getRecipeCategory());
        if(exampleListFull.get(position).getRecipeModel().getUser()!=null)
        holder.tvUserName.setText(exampleListFull.get(position).getRecipeModel().getUser().getName());
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final String userid=firebaseUser.getUid();
        holder.getlikebuttonstatus(RecipeId,userid);
        holder.like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testclick=true;

                likereference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(testclick==true)
                        {
                            if(snapshot.child(userid).hasChild(RecipeId))
                            {
                                likereference.child(userid).child(RecipeId).removeValue();
                                testclick=false;
                            }
                            else
                            {
                                likereference.child(userid).child(RecipeId).setValue(true);
                                testclick=false;
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });


        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, RecipeDetailsActivity.class);
                intent.putExtra("recipe",exampleListFull.get(holder.getAdapterPosition()));
                intent.putExtra("user",userModel);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return exampleListFull.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView tvRecipeName,tvUserName,tvCategory;
        CardView wholecard;
        ImageView like_btn;
        TextView like_text;
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            wholecard = itemView.findViewById(R.id.wholecard);
            tvRecipeName = itemView.findViewById(R.id.tv);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            like_btn=(ImageView)itemView.findViewById(R.id.ic_home_item_fav);
            like_text=(TextView)itemView.findViewById(R.id.like_text);

        }

        public void getlikebuttonstatus(String recipeId, String userid) {
            likereference = FirebaseDatabase.getInstance().getReference("Favourites");
            likereference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(userid).hasChild(recipeId)) {
                        int likecount = (int) snapshot.child(userid).getChildrenCount();
                        like_text.setText(likecount + " likes");
                        like_btn.setImageResource(R.drawable.ic_fav_heart);
                    } else {
                        int likecount = (int) snapshot.child(userid).getChildrenCount();
                        like_text.setText(likecount + " likes");
                        like_btn.setImageResource(R.drawable.ic_not_fav_heart);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = constraint.toString();
            exampleListFull=new ArrayList<>();
            if (charString.isEmpty()) {

                exampleListFull.addAll(exampleList);
            } else {
                List<Recipe> filteredList = new ArrayList<>();
                for (Recipe row : exampleList) {
                    String ingToshow="";
                   /* for (int i=0;i<row.getIngredientModelArrayList().size();i++){
                        ingToshow=ingToshow+"\n"+row.getIngredientModelArrayList().get(i).getIngredient();
                    }*/
                    // name match condition. this might differ depending on your requirement
                    // here we are looking for name or phone number match
                    if (row.getRecipeModel().getName().toLowerCase().contains(charString.toLowerCase()) || ingToshow.contains(constraint)) {
                        filteredList.add(row);
                    }
                }

                exampleListFull = filteredList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = exampleListFull;
            return filterResults;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            exampleListFull = (ArrayList<Recipe>) results.values;
            notifyDataSetChanged();
        }
    };
}
