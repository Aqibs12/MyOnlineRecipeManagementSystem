package com.example.fitrecipes.Models;

import java.io.Serializable;

public class Recipe implements Serializable {
    String recipeid;
    //String recipeId;
    RecipeModel recipeModel;

    Recipe(){

    }

    public Recipe(String id, RecipeModel recipeModel) {
        this.recipeid = id;
        this.recipeModel = recipeModel;
    }

    public String getRecipeid() {
        return recipeid;
    }

    public void setRecipeid(String recipeid) {
        this.recipeid = recipeid;
    }

    public RecipeModel getRecipeModel() {
        return recipeModel;
    }

    public void setRecipeModel(RecipeModel recipeModel) {
        this.recipeModel = recipeModel;
    }
}
