package com.example.fitrecipes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitrecipes.Models.ImagesModel;
import com.example.fitrecipes.Models.IngredientModel;
import com.example.fitrecipes.R;

import java.io.File;
import java.util.List;


public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.RecipeViewHolder> {



    public List<ImagesModel> exampleListFull;
    Context context;

    public ImagesAdapter(List<ImagesModel> exampleListFull, Context context)
    {
        this.exampleListFull = exampleListFull;
        this.context =  context;


    }
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_ingredient,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, final int position) {



        holder.tv.setText(new File(exampleListFull.get(position).getImage()).getName());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             exampleListFull.remove(position);
             Toast.makeText(context, "Item has been removed", Toast.LENGTH_SHORT).show();
             notifyDataSetChanged();
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
        TextView tv;
        CardView wholecard;
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            wholecard = itemView.findViewById(R.id.wholecard);
            tv = itemView.findViewById(R.id.tv);

        }
    }


}
