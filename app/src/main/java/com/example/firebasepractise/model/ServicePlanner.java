package com.example.firebasepractise.model;

public class ServicePlanner {

    String serviceDescription;
    String serviceName;
    String serviceParentCategory;
    String serviceChildCategory;
    int servicePrice;

    public ServicePlanner () {

    }

    public ServicePlanner(String serviceDescription, String serviceName, String serviceParentCategory, String serviceChildCategory, int servicePrice) {
        this.serviceDescription = serviceDescription;
        this.serviceName = serviceName;
        this.serviceParentCategory = serviceParentCategory;
        this.serviceChildCategory = serviceChildCategory;
        this.servicePrice = servicePrice;
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
