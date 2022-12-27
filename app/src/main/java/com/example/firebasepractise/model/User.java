package com.example.firebasepractise.model;

public class User {

    private String userId;
    private String name;
    private String type;
    private String phoneNumber;

    public User() {

    }

    public User(String userId, String name, String type, String phoneNumber) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
