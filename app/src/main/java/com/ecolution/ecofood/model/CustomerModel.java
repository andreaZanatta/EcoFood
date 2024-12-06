package com.ecolution.ecofood.model;

import android.app.Notification;

public class CustomerModel extends UserModel {
    private Notification notification;

    public CustomerModel() { super(); }

    public CustomerModel(Notification notification) {
        this.notification = notification;
    }

    public CustomerModel(int id, String fname, String lname, String email, String hash, boolean isSeller, String img, Notification notification) {
        super(id, fname, lname, email, hash, isSeller, img);
        this.notification = notification;
    }

    public CustomerModel(int id, String fname, String lname, String email) {
        super(id, fname, lname, email, false);
    }

    public void setNotification(Notification nt) { this.notification = nt; }

    public Notification getNotification() { return this.notification; }
}
