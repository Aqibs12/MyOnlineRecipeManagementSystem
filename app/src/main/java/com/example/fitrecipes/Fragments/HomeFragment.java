package com.example.fitrecipes.Fragments;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.fitrecipes.Activities.LoginActivity;
import com.example.fitrecipes.Activities.RecipeDetailsActivity;
import com.example.fitrecipes.Models.RecipeModel;
import com.example.fitrecipes.R;
import com.example.fitrecipes.Adapters.RecipeAdapter;
import com.example.fitrecipes.Util.DatabaseHelper;
import com.example.fitrecipes.Util.HelperKeys;
import com.example.fitrecipes.Util.SessionManager;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment {
    private RecipeModel recipeModel;
    int totalsize;
    private SliderLayout sliderLayout;
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    ArrayList<RecipeModel> recipeModelArrayList;
    private EditText et_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);
//        recipeModel= (RecipeModel) getActivity().getIntent().getSerializableExtra("model");
        //slider code start
        TextView name1 = view.findViewById(R.id.name);
        et_search = view.findViewById(R.id.et_search);
        name1.setText("Hi " + "" + SessionManager.getStringPref(HelperKeys.USER_NAME, getContext()) + " ,Welcome to FitRecipes");
        sliderLayout = view.findViewById(R.id.slider);
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3333);
        recyclerView = view.findViewById(R.id.recyclerview);
        recipeModelArrayList = new ArrayList();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recipeAdapter = new RecipeAdapter(recipeModelArrayList, getContext());
        recyclerView.setAdapter(recipeAdapter);
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        recipeModelArrayList.clear();
        recipeModelArrayList.addAll(databaseHelper.getAllRecipeData());
        Collections.shuffle(recipeModelArrayList);
        recipeAdapter.notifyDataSetChanged();

        HashMap<String, String> file_maps = new HashMap<String, String>();

        if (recipeModelArrayList.size() == 0) {
            Toast.makeText(getContext(), "Please add some recipe data first", Toast.LENGTH_SHORT).show();
        }

        if(recipeModelArrayList.size()>=8)
            totalsize = 8;
        else
            totalsize = recipeModelArrayList.size();


        /** here you need to implement the logic to limit the size to maximum 8*/
        for (int i = 0;  i < totalsize; i++) {
            file_maps.put(recipeModelArrayList.get(i).getName(), recipeModelArrayList.get(i).getImagesModelArrayList().get(0).getImage());
        }


        for (Map.Entry<String, String> entry : file_maps.entrySet()) {
            String name = entry.getValue();
            TextSliderView textSliderView = new TextSliderView(getContext());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(new File(file_maps.get(name)))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);


            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    /** open detail page activity and pass the clicked recipe object in the intent */
                    Toast.makeText(getContext(), "Slider Cicked", Toast.LENGTH_SHORT).show();
                    Intent it=new Intent(getActivity(), RecipeDetailsActivity.class);
                    //it.putExtra("model",recipeModelArrayList.get(finalI));
                    startActivity(it);
                }
            });

            sliderLayout.addSlider(textSliderView);

        }

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (recipeAdapter != null) {

                    recipeAdapter.getFilter().filter(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (recipeAdapter != null) {
                    recipeAdapter.getFilter().filter(s);
                }
            }
        });
        view.findViewById(R.id.tv_log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManager.clearsession(getActivity());
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}