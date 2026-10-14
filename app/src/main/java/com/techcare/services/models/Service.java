package com.techcare.services.models;

public class Service {
    private int id;
    private int deviceId;
    private String serviceName;
    private String description;
    private double price;
    private String durationEstimate;

    public Service() {
    }

    public Service(int id, int deviceId, String serviceName, String description, double price, String durationEstimate) {
        this.id = id;
        this.deviceId = deviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.price = price;
        this.durationEstimate = durationEstimate;
    }

    public Service(int deviceId, String serviceName, String description, double price, String durationEstimate) {
        this.deviceId = deviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.price = price;
        this.durationEstimate = durationEstimate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDurationEstimate() {
        return durationEstimate;
    }

    public void setDurationEstimate(String durationEstimate) {
        this.durationEstimate = durationEstimate;
    }
}
