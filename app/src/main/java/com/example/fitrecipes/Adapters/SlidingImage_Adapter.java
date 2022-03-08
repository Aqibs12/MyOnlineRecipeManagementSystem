package com.example.fitrecipes.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.fitrecipes.Models.ImagesModel;
import com.example.fitrecipes.R;

import java.util.ArrayList;
import java.util.List;

public class SlidingImage_Adapter extends PagerAdapter {

    private List<ImagesModel> imageModelArrayList;
    private LayoutInflater inflater;
    private Context context;
    String type;

    public SlidingImage_Adapter(Context context, List<ImagesModel> imageModelArrayList, String type) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
        this.type = type;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    //getting size of total items list

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        if (!type.equals("details")){
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        }else {

        }
        Glide.with(context).load(imageModelArrayList.get(position).getImage()).into(imageView);
        //imageView.setImageResource(imageModelArrayList.get(position).getImage_drawable());

        view.addView(imageLayout, 0);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
