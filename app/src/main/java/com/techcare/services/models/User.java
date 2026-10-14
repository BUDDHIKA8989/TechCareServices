package com.techcare.services.models;

public class User {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role; // "customer" or "admin"
    private String createdAt;

    private String specialization;

    private String status;


    // Empty constructor
    public User() {
    }

    // Constructor with all fields
    public User(int id, String name, String email, String phone, String password, String role, String createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }


    // Constructor without id (for new users)
    public User(String name, String email, String phone, String password, String role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.specialization = "";
        this.status = "Available";
    }



    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSpecialization() {
        return specialization != null ? specialization : "";
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getStatus() {
        return status != null ? status : "Available";
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

