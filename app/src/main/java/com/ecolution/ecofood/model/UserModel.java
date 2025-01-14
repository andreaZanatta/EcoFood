package com.ecolution.ecofood.model;

import android.util.Log;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {
    private String user_id;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private boolean isSeller;
    private String image;
    private List<String> favourites;

    public UserModel() { }

    public UserModel(boolean isSeller) {
        this.isSeller = isSeller;

    }

    public UserModel(String id, String fname, String lname, String email, String hash, boolean isSeller, String img, List<String> favourites){
        this.user_id = id;
        this.firstName = fname;
        this.lastName = lname;
        this.email = email;
        this.passwordHash = hash;
        this.isSeller = isSeller;
        this.image = img;
        this.favourites = favourites;
    }

    public UserModel(String  id, String fname, String lname, String email, boolean isSeller){
        this.user_id = id;
        this.firstName = fname;
        this.lastName = lname;
        this.email = email;
        this.isSeller = isSeller;
    }

    public void setUser_id(String id) {
        this.user_id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setFavourites(List<String> favourites) { this.favourites = favourites; }

    public String getUser_id() {
        return user_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public String getImage() {
        return image;
    }

    public String getFullName() { return firstName+" "+lastName; }

    public List<String> getFavourites() { return favourites; }
}
