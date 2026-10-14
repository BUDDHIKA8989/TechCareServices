package com.techcare.services.models;

public class Booking {
    private int id;
    private int userId;
    private int serviceId;
    private String issueDescription;
    private String imagePath;
    private String serviceMethod; // "pickup" or "dropoff"
    private String bookingDate;
    private String pickupTime;
    private String address;
    private String status; // "Received", "Under Repair", "Ready for Pickup", "Completed"
    private int technicianId;
    private String estimatedCompletion;
    private String createdAt;

    private String serviceName;

    private String bookingTime;

    private double price;

    public Booking() {
    }

    public Booking(int id, int userId, int serviceId, String issueDescription, String imagePath,
                   String serviceMethod, String bookingDate, String pickupTime, String address,
                   String status, int technicianId, String estimatedCompletion, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.serviceId = serviceId;
        this.issueDescription = issueDescription;
        this.imagePath = imagePath;
        this.serviceMethod = serviceMethod;
        this.bookingDate = bookingDate;
        this.pickupTime = pickupTime;
        this.address = address;
        this.status = status;
        this.technicianId = technicianId;
        this.estimatedCompletion = estimatedCompletion;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getServiceMethod() {
        return serviceMethod;
    }

    public void setServiceMethod(String serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }

    public String getEstimatedCompletion() {
        return estimatedCompletion;
    }

    public void setEstimatedCompletion(String estimatedCompletion) {
        this.estimatedCompletion = estimatedCompletion;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
