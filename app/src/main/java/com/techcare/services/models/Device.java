package com.techcare.services.models;

public class Device {
    private int id;
    private String deviceName;
    private String deviceType;
    private String imagePath;

    public Device() {
    }

    public Device(int id, String deviceName, String deviceType, String imagePath) {
        this.id = id;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.imagePath = imagePath;
    }

    public Device(String deviceName, String deviceType, String imagePath) {
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
