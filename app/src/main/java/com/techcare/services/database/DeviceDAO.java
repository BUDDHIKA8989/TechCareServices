package com.techcare.services.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.techcare.services.models.Device;
import java.util.ArrayList;
import java.util.List;

public class DeviceDAO {
    private DatabaseHelper dbHelper;

    public DeviceDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Add new device
    public long addDevice(Device device) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DEVICE_NAME, device.getDeviceName());
        values.put(DatabaseHelper.COLUMN_DEVICE_TYPE, device.getDeviceType());
        values.put(DatabaseHelper.COLUMN_IMAGE_PATH, device.getImagePath());

        long id = db.insert(DatabaseHelper.TABLE_DEVICES, null, values);
        db.close();
        return id;
    }

    // Get all devices
    public List<Device> getAllDevices() {
        List<Device> deviceList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_DEVICES;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Device device = new Device();
                device.setId(cursor.getInt(0));
                device.setDeviceName(cursor.getString(1));
                device.setDeviceType(cursor.getString(2));
                device.setImagePath(cursor.getString(3));
                deviceList.add(device);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return deviceList;
    }

    // Get device by ID
    public Device getDeviceById(int deviceId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Device device = null;

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_DEVICES +
                " WHERE " + DatabaseHelper.COLUMN_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(deviceId)});

        if (cursor.moveToFirst()) {
            device = new Device();
            device.setId(cursor.getInt(0));
            device.setDeviceName(cursor.getString(1));
            device.setDeviceType(cursor.getString(2));
            device.setImagePath(cursor.getString(3));
        }

        cursor.close();
        db.close();
        return device;
    }

    // Update device
    public int updateDevice(Device device) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DEVICE_NAME, device.getDeviceName());
        values.put(DatabaseHelper.COLUMN_DEVICE_TYPE, device.getDeviceType());
        values.put(DatabaseHelper.COLUMN_IMAGE_PATH, device.getImagePath());

        int rows = db.update(DatabaseHelper.TABLE_DEVICES, values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(device.getId())});

        db.close();
        return rows;
    }

    // Delete device
    public void deleteDevice(int deviceId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_DEVICES,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(deviceId)});
        db.close();
    }
}
