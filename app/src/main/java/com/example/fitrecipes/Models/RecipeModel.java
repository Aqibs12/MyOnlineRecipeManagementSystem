package com.example.fitrecipes.Models;

import java.io.Serializable;
import java.util.List;

public class RecipeModel {



    public RecipeModel(String name, String serving_persons, String cook_time, String category, String description, String instruc,String url) {

        this.name = name;
        this.serving_persons = serving_persons;
        this.cook_time = cook_time;
        this.category = category;
        this.description = description;
        this.instruc = instruc;
        this.imageUrl = url;

    }
    public RecipeModel() {
    }

    int id,user_id;
    String name,serving_persons,cook_time,category,description,instruc,imageUrl;
  /*  List<ImagesModel> imagesModelArrayList;
    List<IngredientModel>  ingredientModelArrayList;*/
    UserModel user;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServing_persons() {
        return serving_persons;
    }

    public void setServing_persons(String serving_persons) {
        this.serving_persons = serving_persons;
    }

    public String getCook_time() {
        return cook_time;
    }

    public void setCook_time(String cook_time) {
        this.cook_time = cook_time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstruc() {
        return instruc;
    }

    public void setInstruc(String instruc) {
        this.instruc = instruc;
    }

    /*public List<ImagesModel> getImagesModelArrayList() {
        return imagesModelArrayList;
    }

    public void setImagesModelArrayList(List<ImagesModel> imagesModelArrayList) {
        this.imagesModelArrayList = imagesModelArrayList;
    }

    public List<IngredientModel> getIngredientModelArrayList() {
        return ingredientModelArrayList;
    }

    public void setIngredientModelArrayList(List<IngredientModel> ingredientModelArrayList) {
        this.ingredientModelArrayList = ingredientModelArrayList;
    }*/

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
