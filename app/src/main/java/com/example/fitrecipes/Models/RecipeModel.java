package com.example.fitrecipes.Models;

public class RecipeModel {
    String id;
    String name;
    String recipeT;
    String recipeD;
    String RecipeI;
    String RecipeIng;
    String Recipe_people;
    String Recipe_image;


    public RecipeModel(String uid, String tempImageName, String recipeTime, String recipe_description, String recipe_instructions, String recipe_ingredients, String recipe_no_serving_people, String url) {
    this.id = uid;
    this.name = tempImageName;
    this.recipeT = recipeTime;
    this.recipeD = recipe_description;
    this.RecipeI = recipe_instructions;
    this.RecipeIng = recipe_ingredients;
    this.Recipe_people = recipe_no_serving_people;
    this.Recipe_image = url;
    }
    UserModel user;

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipeT() {
        return recipeT;
    }

    public void setRecipeT(String recipeT) {
        this.recipeT = recipeT;
    }

    public String getRecipeD() {
        return recipeD;
    }

    public void setRecipeD(String recipeD) {
        this.recipeD = recipeD;
    }

    public String getRecipeI() {
        return RecipeI;
    }

    public void setRecipeI(String recipeI) {
        RecipeI = recipeI;
    }

    public String getRecipeIng() {
        return RecipeIng;
    }

    public void setRecipeIng(String recipeIng) {
        RecipeIng = recipeIng;
    }

    public String getRecipe_people() {
        return Recipe_people;
    }

    public void setRecipe_people(String recipe_people) {
        Recipe_people = recipe_people;
    }

    public String getRecipe_image() {
        return Recipe_image;
    }

    public void setRecipe_image(String recipe_image) {
        Recipe_image = recipe_image;
    }
}
