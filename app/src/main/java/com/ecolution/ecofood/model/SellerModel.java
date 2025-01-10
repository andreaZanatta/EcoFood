package com.ecolution.ecofood.model;

import android.app.Notification;

public class SellerModel extends UserModel{
    private String shopName;
    private String address;
    private String logo;
    private Notification notification;

    public SellerModel() { }

    public SellerModel(String id, String fname, String lname, String email, String hash, boolean isSeller, String img, String shopName, String address, String logo, Notification notification) {
        super(id, fname, lname, email, hash, isSeller, img);
        this.shopName = shopName;
        this.address = address;
        this.notification = notification;
    }

    public SellerModel(String id, String fname, String lname, String email, String shopName, String address) {
        super(id, fname, lname, email, true);
        this.shopName = shopName;
        this.address = address;
    }

    public SellerModel(String id, String fname, String lname, String email, String hash, boolean isSeller, String img, String shopName, String address, String logo) {
        super(id, fname, lname, email, hash, isSeller, img);
        this.shopName = shopName;
        this.address = address;
        this.logo = logo;
    }

    public void setShopName(String nm) { this.shopName = nm; }

    public String getShopName() { return this.shopName; }

    public void setAddress(String addr) { this.address = addr; }

    public String getAddress() { return this.address; }

    public void setNotification(Notification nt) { this.notification = nt; }

    public Notification getNotification() { return this.notification; }

    public String getLogo() { return this.logo; }
}
