package com.example.firebasepractise.model;

public class Booked {

//    planner and user details
    String plannerEmail;
    String plannerPhoneNumber;
    String userEmail;
    String type;
    String imageUrl;
    String date;

//    service values
    String serviceName;
    String serviceDescription;
    String servicePrice;
    String serviceParentCategory;
    String serviceChildCategory;

//    venue values
    String venueName;
    String venueAddress;
    String venueSize;
    String venueRent;
    String venueGuests;
    String venueRooms;
    String venueWashrooms;

    public Booked () {

    }

    public Booked(String plannerEmail, String plannerPhoneNumber, String userEmail, String type, String imageUrl, String date, String serviceName, String serviceDescription, String servicePrice, String serviceParentCategory, String serviceChildCategory) {
        this.plannerEmail = plannerEmail;
        this.plannerPhoneNumber = plannerPhoneNumber;
        this.userEmail = userEmail;
        this.type = type;
        this.imageUrl = imageUrl;
        this.date = date;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.servicePrice = servicePrice;
        this.serviceParentCategory = serviceParentCategory;
        this.serviceChildCategory = serviceChildCategory;
    }

    public Booked(String plannerEmail, String plannerPhoneNumber, String userEmail, String type, String imageUrl, String date, String venueName, String venueAddress, String venueSize, String venueRent, String venueGuests, String venueRooms, String venueWashrooms) {
        this.plannerEmail = plannerEmail;
        this.plannerPhoneNumber = plannerPhoneNumber;
        this.userEmail = userEmail;
        this.type = type;
        this.imageUrl = imageUrl;
        this.date = date;
        this.venueName = venueName;
        this.venueAddress = venueAddress;
        this.venueSize = venueSize;
        this.venueRent = venueRent;
        this.venueGuests = venueGuests;
        this.venueRooms = venueRooms;
        this.venueWashrooms = venueWashrooms;
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

    public String getPlannerEmail() {
        return plannerEmail;
    }

    public void setPlannerEmail(String plannerEmail) {
        this.plannerEmail = plannerEmail;
    }

    public String getPlannerPhoneNumber() {
        return plannerPhoneNumber;
    }

    public void setPlannerPhoneNumber(String plannerPhoneNumber) {
        this.plannerPhoneNumber = plannerPhoneNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServiceParentCategory() {
        return serviceParentCategory;
    }

    public void setServiceParentCategory(String serviceParentCategory) {
        this.serviceParentCategory = serviceParentCategory;
    }

    public String getServiceChildCategory() {
        return serviceChildCategory;
    }

    public void setServiceChildCategory(String serviceChildCategory) {
        this.serviceChildCategory = serviceChildCategory;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public String getVenueSize() {
        return venueSize;
    }

    public void setVenueSize(String venueSize) {
        this.venueSize = venueSize;
    }

    public String getVenueRent() {
        return venueRent;
    }

    public void setVenueRent(String venueRent) {
        this.venueRent = venueRent;
    }

    public String getVenueGuests() {
        return venueGuests;
    }

    public void setVenueGuests(String venueGuests) {
        this.venueGuests = venueGuests;
    }

    public String getVenueRooms() {
        return venueRooms;
    }

    public void setVenueRooms(String venueRooms) {
        this.venueRooms = venueRooms;
    }

    public String getVenueWashrooms() {
        return venueWashrooms;
    }

    public void setVenueWashrooms(String venueWashrooms) {
        this.venueWashrooms = venueWashrooms;
    }
}
