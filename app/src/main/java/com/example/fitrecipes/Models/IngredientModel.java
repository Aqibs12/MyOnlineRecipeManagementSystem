package com.example.fitrecipes.Models;

import java.io.Serializable;

public class IngredientModel implements Serializable {
    int id,recipeId;
    String ingredient;


    public IngredientModel() {
    }

    public IngredientModel(String ingredient) {
        this.ingredient = ingredient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
