package com.ecolution.ecofood.model;

import java.util.List;

public class CustomerModel extends UserModel {
    private NotificationType notification;


    public CustomerModel() {
        super();
        //super(false);
    }

    public CustomerModel(String id, String fname, String lname, String email, String hash, String img, List<String> fav, NotificationType notification) {
        super(id, fname, lname, email, hash, false, img, fav);
        this.notification = notification;
    }

    public CustomerModel(String id, String fname, String lname, String email) {
        super(id, fname, lname, email, false);
        this.notification = NotificationType.All;
    }

    public CustomerModel(String id, String fname, String lname, String email, String hash, String img) {
        super(id, fname, lname, email, false);
        this.notification = NotificationType.All;
    }

    public void setNotification(NotificationType nt) { this.notification = nt; }

    public NotificationType getNotification() { return this.notification; }
}
