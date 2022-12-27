package com.example.firebasepractise.model;

public class Venue {

    private String name;
    private String perHourRent;
    private String address;
    private String size;
    private String noGuests;
    private String attachedRooms;
    private String washRooms;

    public Venue () {

    }

    public Venue(String name, String perHourRent, String address, String size, String noGuests, String attachedRooms, String washRooms) {
        this.name = name;
        this.perHourRent = perHourRent;
        this.address = address;
        this.size = size;
        this.noGuests = noGuests;
        this.attachedRooms = attachedRooms;
        this.washRooms = washRooms;
    }

    public String getPerHourRent() {
        return perHourRent;
    }

    public void setPerHourRent(String perHourRent) {
        this.perHourRent = perHourRent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getNoGuests() {
        return noGuests;
    }

    public void setNoGuests(String noGuests) {
        this.noGuests = noGuests;
    }

    public String getAttachedRooms() {
        return attachedRooms;
    }

    public void setAttachedRooms(String attachedRooms) {
        this.attachedRooms = attachedRooms;
    }

    public String getWashRooms() {
        return washRooms;
    }

    public void setWashRooms(String washRooms) {
        this.washRooms = washRooms;
    }
}
