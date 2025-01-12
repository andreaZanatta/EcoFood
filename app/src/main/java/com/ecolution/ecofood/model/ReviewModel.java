package com.ecolution.ecofood.model;

public class ReviewModel {
    private String title;
    private String description;
    private float grade;
    private String customer;
    private String seller;

    public ReviewModel() { }

    public ReviewModel(String title, String description, float grade, String customer, String seller) {
        this.title = title;
        this.description = description;
        this.grade = grade;
        this.customer = customer;
        this.seller = seller;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public void setCustomer(String  customer) {
        this.customer = customer;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public float getGrade() {
        return grade;
    }

    public String getCustomer() {
        return customer;
    }

    public String getSeller() {
        return seller;
    }
}
