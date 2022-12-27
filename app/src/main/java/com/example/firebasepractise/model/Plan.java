package com.example.firebasepractise.model;

import java.net.URL;

public class Plan {

    private String title;
    private String description;
    private String date;
    private boolean approved;
    private String userId;
    private String documentId;
    private String imageUrl;

    public Plan () {

    }

    public Plan(String title, String description, String date, boolean approved, String userId, String documentId, String imageUrl) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.approved = approved;
        this.userId = userId;
        this.documentId = documentId;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
