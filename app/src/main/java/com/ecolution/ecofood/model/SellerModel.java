package com.ecolution.ecofood.model;

public class SellerModel extends UserModel{
    private String shopName;
    private String address;
    private String logo;
    private NotificationType notification;

    public SellerModel() { super(true); }

    public SellerModel(String id, String fname, String lname, String email, String hash, String img, String shopName, String address, String logo, NotificationType notification) {
        super(id, fname, lname, email, hash, true, img);
        this.shopName = shopName;
        this.address = address;
        this.notification = notification;
    }

    public SellerModel(String id, String fname, String lname, String email, String shopName, String address) {
        super(id, fname, lname, email, true);
        this.shopName = shopName;
        this.address = address;
        this.notification = NotificationType.All;
    }

    public SellerModel(String id, String fname, String lname, String email, String hash, String img, String shopName, String address, String logo) {
        super(id, fname, lname, email, hash, true, img);
        this.shopName = shopName;
        this.address = address;
        this.logo = logo;
        this.notification = NotificationType.All;
    }

    public void setShopName(String nm) { this.shopName = nm; }

    public String getShopName() { return this.shopName; }

    public void setAddress(String addr) { this.address = addr; }

    public String getAddress() { return this.address; }

    public void setNotification(NotificationType nt) { this.notification = nt; }

    public NotificationType getNotification() { return this.notification; }

    public String getLogo() { return this.logo; }
}
