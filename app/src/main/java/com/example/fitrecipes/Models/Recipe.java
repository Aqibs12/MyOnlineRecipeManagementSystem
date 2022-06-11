package com.example.fitrecipes.Models;

import java.io.Serializable;

public class Recipe implements Serializable {
    String id;
    //String recipeId;
    RecipeModel recipeModel;

    Recipe(){

    }

    public Recipe(String id, RecipeModel recipeModel) {
        this.id = id;
        this.recipeModel = recipeModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RecipeModel getRecipeModel() {
        return recipeModel;
    }

    public void setRecipeModel(RecipeModel recipeModel) {
        this.recipeModel = recipeModel;
    }
}
