package com.example.firebasepractise.model;

import com.google.firebase.firestore.SetOptions;

public class ServicePlanner {

    String serviceDescription;
    String serviceName;
    String serviceParentCategory;
    String serviceChildCategory;
    int servicePrice;
    boolean approved;
    String documentId;
    String email;
    String phoneNumber;
    String date;
    String imageUrl;

    public ServicePlanner () {

    }

    public ServicePlanner(String serviceDescription, String serviceName, String serviceParentCategory, String serviceChildCategory, int servicePrice, boolean approved, String documentId, String email, String phoneNumber, String date, String imageUrl) {
        this.serviceDescription = serviceDescription;
        this.serviceName = serviceName;
        this.serviceParentCategory = serviceParentCategory;
        this.serviceChildCategory = serviceChildCategory;
        this.servicePrice = servicePrice;
        this.approved = approved;
        this.documentId = documentId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getServiceChildCategory() {
        return serviceChildCategory;
    }

    public void setServiceChildCategory(String serviceChildCategory) {
        this.serviceChildCategory = serviceChildCategory;
    }

    public String getServiceParentCategory() {
        return serviceParentCategory;
    }

    public void setServiceParentCategory(String serviceParentCategory) {
        this.serviceParentCategory = serviceParentCategory;
    }

    public int getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(int servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }
}
