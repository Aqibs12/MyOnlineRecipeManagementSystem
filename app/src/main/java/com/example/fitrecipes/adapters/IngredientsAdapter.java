package com.example.fitrecipes.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitrecipes.Activities.RecipeDetailsActivity;
import com.example.fitrecipes.Models.Ingredient;
import com.example.fitrecipes.Models.Recipe;
import com.example.fitrecipes.Models.UserModel;
import com.example.fitrecipes.R;

import java.util.ArrayList;
import java.util.List;


public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.RecipeViewHolder> {
    public List<Ingredient> dataList;
    Context context;
    public IngredientsAdapter(Context context)
    {
        this.context =  context;
        this.dataList = new ArrayList<>();
    }
    public IngredientsAdapter(Context context,List<Ingredient> ingredientList)
    {
        this.context =  context;
        this.dataList = ingredientList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_add_ingredient,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {

        Ingredient ingredient = dataList.get(holder.getAdapterPosition());
        holder.tvQuantity.setText(ingredient.getQuantity()+"");
        holder.tvName.setText(ingredient.getName());
        holder.spnUnits.setSelection(ingredient.getUnit());
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredient.setQuantity(ingredient.getQuantity()+1);
                holder.tvQuantity.setText(ingredient.getQuantity()+"");
            }
        });
        holder.btnSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ingredient.getQuantity()<=1)
                {
                    dataList.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }
                else {
                    ingredient.setQuantity(ingredient.getQuantity()-1);
                    holder.tvQuantity.setText(ingredient.getQuantity()+"");
                }
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
        holder.spnUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ingredient.setUnit(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public boolean add(Ingredient ingredient) {
        for(Ingredient ing:dataList){
            if(ing.getName().equals(ingredient.getName())){
                return false;
            }
        }
        dataList.add(ingredient);
        notifyDataSetChanged();

        return true;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder
    {

        ImageView ivDelete;
        Button btnAdd,btnSubtract;
        TextView tvQuantity,tvName;
        Spinner spnUnits;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            btnAdd = itemView.findViewById(R.id.btn_add);
            btnSubtract = itemView.findViewById(R.id.btn_subtract);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvName = itemView.findViewById(R.id.tv_ing_name);
            spnUnits = itemView.findViewById(R.id.spn_units);

        }
    }


}
