package com.example.fitrecipes.Models;

import java.io.Serializable;

public class CategoryModel implements Serializable {

    int id;
    String category;

    public CategoryModel (){

    }

    public CategoryModel(int id, String category) {
        this.id = id;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
