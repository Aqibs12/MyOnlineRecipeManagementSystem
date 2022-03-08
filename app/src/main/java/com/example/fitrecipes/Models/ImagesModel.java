package com.example.fitrecipes.Models;

import java.io.Serializable;

public class ImagesModel implements Serializable {
    int id,recipeId;
    String image;

    public ImagesModel(String image) {
        this.image = image;
    }


    public ImagesModel() {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
