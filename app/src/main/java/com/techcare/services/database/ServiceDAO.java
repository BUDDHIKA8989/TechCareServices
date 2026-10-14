package com.techcare.services.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.techcare.services.models.Service;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

    private DatabaseHelper dbHelper;

    public ServiceDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Get all services
    public List<Service> getAllServices() {
        List<Service> serviceList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_SERVICES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Service service = new Service();
                service.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                service.setDeviceId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_DEVICE_ID)));
                service.setServiceName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_NAME)));
                service.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_DESCRIPTION)));
                service.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_PRICE)));
                service.setDurationEstimate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_DURATION)));
                serviceList.add(service);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return serviceList;
    }

    // Get services by device ID
    public List<Service> getServicesByDeviceId(int deviceId) {
        List<Service> serviceList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_SERVICES, null,
                DatabaseHelper.COLUMN_SERVICE_DEVICE_ID + " = ?",
                new String[]{String.valueOf(deviceId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Service service = new Service();
                service.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                service.setDeviceId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_DEVICE_ID)));
                service.setServiceName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_NAME)));
                service.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_DESCRIPTION)));
                service.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_PRICE)));
                service.setDurationEstimate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_DURATION)));
                serviceList.add(service);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return serviceList;
    }

    // Get service by ID
    public Service getServiceById(int serviceId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Service service = null;

        Cursor cursor = db.query(DatabaseHelper.TABLE_SERVICES, null,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(serviceId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            service = new Service();
            service.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
            service.setDeviceId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_DEVICE_ID)));
            service.setServiceName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_NAME)));
            service.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_DESCRIPTION)));
            service.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_PRICE)));
            service.setDurationEstimate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_DURATION)));
        }

        cursor.close();
        db.close();
        return service;
    }

    // Add service
    public long addService(Service service) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_SERVICE_DEVICE_ID, service.getDeviceId());
        values.put(DatabaseHelper.COLUMN_SERVICE_NAME, service.getServiceName());
        values.put(DatabaseHelper.COLUMN_SERVICE_DESCRIPTION, service.getDescription());
        values.put(DatabaseHelper.COLUMN_SERVICE_PRICE, service.getPrice());
        values.put(DatabaseHelper.COLUMN_SERVICE_DURATION, service.getDurationEstimate());

        long id = db.insert(DatabaseHelper.TABLE_SERVICES, null, values);
        db.close();
        return id;
    }

    // Update service
    public int updateService(Service service) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_SERVICE_DEVICE_ID, service.getDeviceId());
        values.put(DatabaseHelper.COLUMN_SERVICE_NAME, service.getServiceName());
        values.put(DatabaseHelper.COLUMN_SERVICE_DESCRIPTION, service.getDescription());
        values.put(DatabaseHelper.COLUMN_SERVICE_PRICE, service.getPrice());
        values.put(DatabaseHelper.COLUMN_SERVICE_DURATION, service.getDurationEstimate());

        int rows = db.update(DatabaseHelper.TABLE_SERVICES, values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(service.getId())});
        db.close();
        return rows;
    }

    // Delete service
    public int deleteService(int serviceId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(DatabaseHelper.TABLE_SERVICES,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(serviceId)});
        db.close();
        return rows;
    }
}
