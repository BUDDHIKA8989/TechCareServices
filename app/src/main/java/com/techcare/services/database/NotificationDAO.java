package com.techcare.services.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.techcare.services.models.Notification;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    private DatabaseHelper dbHelper;

    public NotificationDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Add notification
    public long addNotification(Notification notification) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_NOTIFICATION_USER_ID, notification.getUserId());
        values.put(DatabaseHelper.COLUMN_NOTIFICATION_BOOKING_ID, notification.getBookingId());
        values.put(DatabaseHelper.COLUMN_NOTIFICATION_MESSAGE, notification.getMessage());
        values.put(DatabaseHelper.COLUMN_NOTIFICATION_IS_READ, notification.getIsRead());
        // Don't put timestamp - let database set it with DEFAULT CURRENT_TIMESTAMP

        long id = db.insert(DatabaseHelper.TABLE_NOTIFICATIONS, null, values);
        db.close();
        return id;
    }

    // Get all notifications for a user
    public List<Notification> getNotificationsByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT " +
                DatabaseHelper.COLUMN_ID + ", " +
                DatabaseHelper.COLUMN_NOTIFICATION_USER_ID + ", " +
                DatabaseHelper.COLUMN_NOTIFICATION_BOOKING_ID + ", " +
                DatabaseHelper.COLUMN_NOTIFICATION_MESSAGE + ", " +
                DatabaseHelper.COLUMN_CREATED_AT + ", " +
                DatabaseHelper.COLUMN_NOTIFICATION_IS_READ +
                " FROM " + DatabaseHelper.TABLE_NOTIFICATIONS +
                " WHERE " + DatabaseHelper.COLUMN_NOTIFICATION_USER_ID + " = ? " +
                " ORDER BY " + DatabaseHelper.COLUMN_ID + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int notifUserId = cursor.getInt(1);
                int bookingId = cursor.getInt(2);
                String message = cursor.getString(3);
                String timestamp = cursor.getString(4);
                int isRead = cursor.getInt(5);

                Notification notification = new Notification(id, notifUserId, bookingId, message, timestamp, isRead);
                notifications.add(notification);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notifications;
    }

    // Get unread notifications
    public List<Notification> getUnreadNotifications(int userId) {
        List<Notification> notifications = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT " +
                DatabaseHelper.COLUMN_ID + ", " +
                DatabaseHelper.COLUMN_NOTIFICATION_USER_ID + ", " +
                DatabaseHelper.COLUMN_NOTIFICATION_BOOKING_ID + ", " +
                DatabaseHelper.COLUMN_NOTIFICATION_MESSAGE + ", " +
                DatabaseHelper.COLUMN_CREATED_AT + ", " +
                DatabaseHelper.COLUMN_NOTIFICATION_IS_READ +
                " FROM " + DatabaseHelper.TABLE_NOTIFICATIONS +
                " WHERE " + DatabaseHelper.COLUMN_NOTIFICATION_USER_ID + " = ? AND " +
                DatabaseHelper.COLUMN_NOTIFICATION_IS_READ + " = 0 " +
                " ORDER BY " + DatabaseHelper.COLUMN_ID + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int notifUserId = cursor.getInt(1);
                int bookingId = cursor.getInt(2);
                String message = cursor.getString(3);
                String timestamp = cursor.getString(4);
                int isRead = cursor.getInt(5);

                Notification notification = new Notification(id, notifUserId, bookingId, message, timestamp, isRead);
                notifications.add(notification);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notifications;
    }

    // Get read notifications
    public List<Notification> getReadNotifications(int userId) {
        List<Notification> notifications = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT " +
                DatabaseHelper.COLUMN_ID + ", " +
                DatabaseHelper.COLUMN_NOTIFICATION_USER_ID + ", " +
                DatabaseHelper.COLUMN_NOTIFICATION_BOOKING_ID + ", " +
                DatabaseHelper.COLUMN_NOTIFICATION_MESSAGE + ", " +
                DatabaseHelper.COLUMN_CREATED_AT + ", " +
                DatabaseHelper.COLUMN_NOTIFICATION_IS_READ +
                " FROM " + DatabaseHelper.TABLE_NOTIFICATIONS +
                " WHERE " + DatabaseHelper.COLUMN_NOTIFICATION_USER_ID + " = ? AND " +
                DatabaseHelper.COLUMN_NOTIFICATION_IS_READ + " = 1 " +
                " ORDER BY " + DatabaseHelper.COLUMN_ID + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int notifUserId = cursor.getInt(1);
                int bookingId = cursor.getInt(2);
                String message = cursor.getString(3);
                String timestamp = cursor.getString(4);
                int isRead = cursor.getInt(5);

                Notification notification = new Notification(id, notifUserId, bookingId, message, timestamp, isRead);
                notifications.add(notification);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notifications;
    }

    // Mark notification as read
    public int markAsRead(int notificationId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOTIFICATION_IS_READ, 1);

        int rows = db.update(DatabaseHelper.TABLE_NOTIFICATIONS, values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(notificationId)});
        db.close();
        return rows;
    }

    // Delete notification
    public int deleteNotification(int notificationId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(DatabaseHelper.TABLE_NOTIFICATIONS,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(notificationId)});
        db.close();
        return rows;
    }

    // Clear all notifications for a user
    public int clearAllNotifications(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(DatabaseHelper.TABLE_NOTIFICATIONS,
                DatabaseHelper.COLUMN_NOTIFICATION_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});
        db.close();
        return rows;
    }

    // Get unread count
    public int getUnreadCount(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_NOTIFICATIONS +
                " WHERE " + DatabaseHelper.COLUMN_NOTIFICATION_USER_ID + " = ? AND " +
                DatabaseHelper.COLUMN_NOTIFICATION_IS_READ + " = 0";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    // Get all notifications (for admin)
    public List<Notification> getAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT " +
                DatabaseHelper.COLUMN_ID + ", " +
                DatabaseHelper.COLUMN_NOTIFICATION_USER_ID + ", " +
                DatabaseHelper.COLUMN_NOTIFICATION_BOOKING_ID + ", " +
                DatabaseHelper.COLUMN_NOTIFICATION_MESSAGE + ", " +
                DatabaseHelper.COLUMN_CREATED_AT + ", " +
                DatabaseHelper.COLUMN_NOTIFICATION_IS_READ +
                " FROM " + DatabaseHelper.TABLE_NOTIFICATIONS +
                " ORDER BY " + DatabaseHelper.COLUMN_ID + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int userId = cursor.getInt(1);
                int bookingId = cursor.getInt(2);
                String message = cursor.getString(3);
                String timestamp = cursor.getString(4);
                int isRead = cursor.getInt(5);

                Notification notification = new Notification(id, userId, bookingId, message, timestamp, isRead);
                notifications.add(notification);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notifications;
    }
}
