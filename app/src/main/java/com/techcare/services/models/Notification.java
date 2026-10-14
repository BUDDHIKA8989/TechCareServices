package com.techcare.services.models;

public class Notification {
    private int id;
    private int userId;
    private int bookingId;
    private String message;
    private String timestamp;
    private int isRead;  // 0 = unread, 1 = read

    // Constructors
    public Notification() {}

    public Notification(int userId, int bookingId, String message) {
        this.userId = userId;
        this.bookingId = bookingId;
        this.message = message;
        this.isRead = 0;
    }

    public Notification(int id, int userId, int bookingId, String message, String timestamp, int isRead) {
        this.id = id;
        this.userId = userId;
        this.bookingId = bookingId;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    // Helper method to get current timestamp
    private String getCurrentTimestamp() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new java.util.Date());
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

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public boolean isRead() {
        return isRead == 1;
    }
}
