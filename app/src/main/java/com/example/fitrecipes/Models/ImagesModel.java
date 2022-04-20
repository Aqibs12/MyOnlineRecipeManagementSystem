package com.example.fitrecipes.Models;

import java.io.Serializable;

public class ImagesModel implements Serializable {

    String id;
    String userImage;

    public ImagesModel() {
    }

    public ImagesModel(String id, String userImage) {
        this.id = id;
        this.userImage = userImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}