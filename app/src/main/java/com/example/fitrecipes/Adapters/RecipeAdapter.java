package com.example.fitrecipes.Adapters;

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
import com.example.fitrecipes.Models.RecipeModel;
import com.example.fitrecipes.R;

import java.util.ArrayList;
import java.util.List;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> implements Filterable {


    private List<RecipeModel> exampleList;
    public List<RecipeModel> exampleListFull;
    Context context;

    public RecipeAdapter(List<RecipeModel> exampleList, Context context)
    {
        this.exampleList = exampleList;
        this.context =  context;
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



        position = holder.getAdapterPosition();
        Glide.with(context).load(exampleListFull.get(position).getImagesModelArrayList().get(0).getImage()).into(holder.img);
        holder.tvRecipeName.setText(exampleListFull.get(position).getName());
        holder.tvCategory.setText(exampleListFull.get(position).getCategory());
        holder.tvUserName.setText(exampleListFull.get(position).getUser().getName());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, RecipeDetailsActivity.class);
                intent.putExtra("model",exampleListFull.get(holder.getAdapterPosition()));
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
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            wholecard = itemView.findViewById(R.id.wholecard);
            tvRecipeName = itemView.findViewById(R.id.tv);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvUserName = itemView.findViewById(R.id.tv_user_name);

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
                List<RecipeModel> filteredList = new ArrayList<>();
                for (RecipeModel row : exampleList) {
                    String ingToshow="";
                    for (int i=0;i<row.getIngredientModelArrayList().size();i++){
                        ingToshow=ingToshow+"\n"+row.getIngredientModelArrayList().get(i).getIngredient();
                    }
                    // name match condition. this might differ depending on your requirement
                    // here we are looking for name or phone number match
                    if (row.getName().toLowerCase().contains(charString.toLowerCase()) || ingToshow.contains(constraint)) {
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

            exampleListFull = (ArrayList<RecipeModel>) results.values;
            notifyDataSetChanged();
        }
    };
}
