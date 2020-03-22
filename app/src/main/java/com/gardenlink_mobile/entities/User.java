package com.gardenlink_mobile.entities;

import android.graphics.drawable.Drawable;

public class User {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String phone;
    private String photo;
    private Boolean newsletter;

    private Drawable drawablePhoto;

    public Boolean getNewsletter() {
        return newsletter;
    }

    public void setNewsletter(Boolean newsletter) {
        this.newsletter = newsletter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Drawable getDrawablePhoto() {
        return drawablePhoto;
    }

    public void setDrawablePhoto(Drawable drawablePhoto) {
        this.drawablePhoto = drawablePhoto;
    }
}
