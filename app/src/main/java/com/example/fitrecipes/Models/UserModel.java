package com.example.fitrecipes.Models;

import java.io.Serializable;

public class UserModel {

    public UserModel() {
    }

    public UserModel(String name, String email, String phone, String password, String security_question, String security_answer) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.security_question = security_question;
        this.security_answer = security_answer;
    }

    boolean isAnonymous = false;
    int id;
    String name,email,phone,password,security_question,security_answer;

    public UserModel(String fullName, String emailAddress, String password, String phoneNumber) {
        this.name = fullName;
        this.email = emailAddress;
        this.password = password;
        this.phone = phoneNumber;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSecurity_question() {
        return security_question;
    }

    public void setSecurity_question(String security_question) {
        this.security_question = security_question;
    }

    public String getSecurity_answer() {
        return security_answer;
    }

    public void setSecurity_answer(String security_answer) {
        this.security_answer = security_answer;
    }

    public static UserModel getAnonymousUser(){
        UserModel anonymousUser = new UserModel("Anonymous","anonymous@gmail.com","+1923450678","","","");
        anonymousUser.isAnonymous = true;
        return anonymousUser;
    }

}
