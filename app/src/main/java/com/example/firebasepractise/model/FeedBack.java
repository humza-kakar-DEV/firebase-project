package com.example.firebasepractise.model;

public class FeedBack {

    String plannerEmail;
    String userEmail;
    String text;
    String rating;

    public FeedBack () {

    }

    public FeedBack(String plannerEmail, String userEmail, String text, String rating) {
        this.plannerEmail = plannerEmail;
        this.userEmail = userEmail;
        this.text = text;
        this.rating = rating;
    }

    public String getPlannerEmail() {
        return plannerEmail;
    }

    public void setPlannerEmail(String plannerEmail) {
        this.plannerEmail = plannerEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

}
