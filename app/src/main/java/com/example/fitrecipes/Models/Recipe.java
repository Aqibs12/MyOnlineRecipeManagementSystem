package com.example.fitrecipes.Models;

import java.io.Serializable;

public class Recipe implements Serializable {
    String recipeId;
    RecipeModel recipeModel;

    Recipe(){

    }

    public Recipe(String recipeId, RecipeModel recipeModel) {
        this.recipeId = recipeId;
        this.recipeModel = recipeModel;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public RecipeModel getRecipeModel() {
        return recipeModel;
    }

    public void setRecipeModel(RecipeModel recipeModel) {
        this.recipeModel = recipeModel;
    }
}
