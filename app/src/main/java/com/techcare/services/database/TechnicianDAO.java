package com.techcare.services.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.techcare.services.models.Technician;
import java.util.ArrayList;
import java.util.List;

public class TechnicianDAO {
    private DatabaseHelper dbHelper;

    public TechnicianDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Add new technician
    public long addTechnician(Technician technician) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TECH_NAME, technician.getName());
        values.put(DatabaseHelper.COLUMN_TECH_PHONE, technician.getPhone());
        values.put(DatabaseHelper.COLUMN_TECH_EMAIL, technician.getEmail());
        values.put(DatabaseHelper.COLUMN_TECH_SPECIALIZATION, technician.getSpecialization());
        values.put(DatabaseHelper.COLUMN_TECH_STATUS, technician.getStatus());

        long id = db.insert(DatabaseHelper.TABLE_TECHNICIANS, null, values);
        db.close();
        return id;
    }

    // Get all technicians
    public List<Technician> getAllTechnicians() {
        List<Technician> technicianList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_TECHNICIANS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Technician technician = new Technician();
                technician.setId(cursor.getInt(0));
                technician.setName(cursor.getString(1));
                technician.setPhone(cursor.getString(2));
                technician.setEmail(cursor.getString(3));
                technician.setSpecialization(cursor.getString(4));
                technician.setStatus(cursor.getString(5));
                technicianList.add(technician);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return technicianList;
    }

    // Get available technicians only
    public List<Technician> getAvailableTechnicians() {
        List<Technician> technicianList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_TECHNICIANS +
                " WHERE " + DatabaseHelper.COLUMN_TECH_STATUS + " = 'available'";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Technician technician = new Technician();
                technician.setId(cursor.getInt(0));
                technician.setName(cursor.getString(1));
                technician.setPhone(cursor.getString(2));
                technician.setEmail(cursor.getString(3));
                technician.setSpecialization(cursor.getString(4));
                technician.setStatus(cursor.getString(5));
                technicianList.add(technician);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return technicianList;
    }

    // Get technician by ID
    public Technician getTechnicianById(int technicianId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Technician technician = null;

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_TECHNICIANS +
                " WHERE " + DatabaseHelper.COLUMN_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(technicianId)});

        if (cursor.moveToFirst()) {
            technician = new Technician();
            technician.setId(cursor.getInt(0));
            technician.setName(cursor.getString(1));
            technician.setPhone(cursor.getString(2));
            technician.setEmail(cursor.getString(3));
            technician.setSpecialization(cursor.getString(4));
            technician.setStatus(cursor.getString(5));
        }

        cursor.close();
        db.close();
        return technician;
    }

    // Update technician
    public int updateTechnician(Technician technician) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TECH_NAME, technician.getName());
        values.put(DatabaseHelper.COLUMN_TECH_PHONE, technician.getPhone());
        values.put(DatabaseHelper.COLUMN_TECH_EMAIL, technician.getEmail());
        values.put(DatabaseHelper.COLUMN_TECH_SPECIALIZATION, technician.getSpecialization());
        values.put(DatabaseHelper.COLUMN_TECH_STATUS, technician.getStatus());

        int rows = db.update(DatabaseHelper.TABLE_TECHNICIANS, values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(technician.getId())});

        db.close();
        return rows;
    }

    // Update technician status
    public void updateTechnicianStatus(int technicianId, String status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TECH_STATUS, status);

        db.update(DatabaseHelper.TABLE_TECHNICIANS, values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(technicianId)});

        db.close();
    }

    // Delete technician
    public void deleteTechnician(int technicianId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_TECHNICIANS,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(technicianId)});
        db.close();
    }
}
