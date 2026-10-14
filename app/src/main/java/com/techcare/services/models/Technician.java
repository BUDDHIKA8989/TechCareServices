
package com.techcare.services.models;
public class Technician {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String specialization;
    private String status; // "available" or "busy"

    public Technician() {
    }

    public Technician(int id, String name, String phone, String email, String specialization, String status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.specialization = specialization;
        this.status = status;
    }

    public Technician(String name, String phone, String email, String specialization, String status) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.specialization = specialization;
        this.status = status;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
