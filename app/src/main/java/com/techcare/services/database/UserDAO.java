package com.techcare.services.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.techcare.services.models.User;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Add new user
    public long addUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, user.getName());
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COLUMN_USER_PHONE, user.getPhone());
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, user.getPassword());
        values.put(DatabaseHelper.COLUMN_USER_ROLE, user.getRole());

        long id = db.insert(DatabaseHelper.TABLE_USERS, null, values);
        db.close();
        return id;
    }

    // Get user by email and password (for login)
    public User getUserByEmailAndPassword(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_USERS +
                " WHERE " + DatabaseHelper.COLUMN_USER_EMAIL + " = ? AND " +
                DatabaseHelper.COLUMN_USER_PASSWORD + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(0));
            user.setName(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setPhone(cursor.getString(3));
            user.setPassword(cursor.getString(4));
            user.setRole(cursor.getString(5));
            user.setCreatedAt(cursor.getString(6));

            // Add specialization and status (columns 7 and 8)
            try {
                if (cursor.getColumnCount() > 7) {
                    String specialization = cursor.getString(7);
                    user.setSpecialization(specialization != null ? specialization : "");
                }
                if (cursor.getColumnCount() > 8) {
                    String status = cursor.getString(8);
                    user.setStatus(status != null ? status : "Available");
                }
            } catch (Exception e) {
                // If columns don't exist, set defaults
                user.setSpecialization("");
                user.setStatus("Available");
            }
        }

        cursor.close();
        db.close();
        return user;
    }

    // Get user by ID
    public User getUserById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_USERS +
                " WHERE " + DatabaseHelper.COLUMN_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            user = new User();

            user.setId(cursor.getInt(0));
            user.setName(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setPhone(cursor.getString(3));
            user.setPassword(cursor.getString(4));
            user.setRole(cursor.getString(5));
            user.setCreatedAt(cursor.getString(6));

            // Add specialization and status (columns 7 and 8)
            try {
                if (cursor.getColumnCount() > 7) {
                    String specialization = cursor.getString(7);
                    user.setSpecialization(specialization != null ? specialization : "");
                }
                if (cursor.getColumnCount() > 8) {
                    String status = cursor.getString(8);
                    user.setStatus(status != null ? status : "Available");
                }
            } catch (Exception e) {
                // If columns don't exist, set defaults
                user.setSpecialization("");
                user.setStatus("Available");
            }
        }

        cursor.close();
        db.close();
        return user;
    }


    // Check if email already exists
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_USERS +
                " WHERE " + DatabaseHelper.COLUMN_USER_EMAIL + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();
        return exists;
    }

    // Get all customers
    public List<User> getAllCustomers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_USERS +
                " WHERE " + DatabaseHelper.COLUMN_USER_ROLE + " = 'customer'";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(0));
                user.setName(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setPhone(cursor.getString(3));
                user.setPassword(cursor.getString(4));
                user.setRole(cursor.getString(5));
                user.setCreatedAt(cursor.getString(6));
                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userList;
    }

    // Update user profile
    public int updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, user.getName());
        values.put(DatabaseHelper.COLUMN_USER_PHONE, user.getPhone());
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, user.getPassword());

        int rows = db.update(DatabaseHelper.TABLE_USERS, values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});

        db.close();
        return rows;
    }

    // Get all technicians
    public List<User> getAllTechnicians() {
        List<User> techList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, null,
                DatabaseHelper.COLUMN_USER_ROLE + " = ?",
                new String[]{"technician"},
                null, null,
                DatabaseHelper.COLUMN_USER_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL)));
                user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PHONE)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ROLE)));
                user.setSpecialization(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_SPECIALIZATION)));
                user.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_STATUS)));
                techList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return techList;
    }

    // Get available technicians
    public List<User> getAvailableTechnicians() {
        List<User> techList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, null,
                DatabaseHelper.COLUMN_USER_ROLE + " = ? AND " + DatabaseHelper.COLUMN_USER_STATUS + " = ?",
                new String[]{"technician", "Available"},
                null, null,
                DatabaseHelper.COLUMN_USER_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL)));
                user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PHONE)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ROLE)));

                int specIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_SPECIALIZATION);
                int statusIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_STATUS);

                if (specIndex != -1) {
                    user.setSpecialization(cursor.getString(specIndex));
                }
                if (statusIndex != -1) {
                    user.setStatus(cursor.getString(statusIndex));
                }

                techList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return techList;
    }


    // Get technicians by status
    public List<User> getTechniciansByStatus(String status) {
        List<User> techList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, null,
                DatabaseHelper.COLUMN_USER_ROLE + " = ? AND " + DatabaseHelper.COLUMN_USER_STATUS + " = ?",
                new String[]{"technician", status},
                null, null,
                DatabaseHelper.COLUMN_USER_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL)));
                user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PHONE)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ROLE)));
                user.setSpecialization(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_SPECIALIZATION)));
                user.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_STATUS)));
                techList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return techList;
    }

    // Update technician
    public int updateTechnician(User technician) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_USER_NAME, technician.getName());
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, technician.getEmail());
        values.put(DatabaseHelper.COLUMN_USER_PHONE, technician.getPhone());
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, technician.getPassword());
        values.put(DatabaseHelper.COLUMN_USER_SPECIALIZATION, technician.getSpecialization());
        values.put(DatabaseHelper.COLUMN_USER_STATUS, technician.getStatus());

        int rows = db.update(DatabaseHelper.TABLE_USERS, values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(technician.getId())});
        db.close();
        return rows;
    }

    // Delete technician
    public int deleteTechnician(int technicianId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(DatabaseHelper.TABLE_USERS,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(technicianId)});
        db.close();
        return rows;
    }

    // Add technician
    public long addTechnician(User technician) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_USER_NAME, technician.getName());
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, technician.getEmail());
        values.put(DatabaseHelper.COLUMN_USER_PHONE, technician.getPhone());
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, technician.getPassword());
        values.put(DatabaseHelper.COLUMN_USER_ROLE, "technician");
        values.put(DatabaseHelper.COLUMN_USER_SPECIALIZATION, technician.getSpecialization());
        values.put(DatabaseHelper.COLUMN_USER_STATUS, technician.getStatus());

        long id = db.insert(DatabaseHelper.TABLE_USERS, null, values);
        db.close();
        return id;
    }

    // Get user by email
    public User getUserByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_USERS +
                " WHERE " + DatabaseHelper.COLUMN_USER_EMAIL + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(0));
            user.setName(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setPhone(cursor.getString(3));
            user.setPassword(cursor.getString(4));
            user.setRole(cursor.getString(5));
            user.setCreatedAt(cursor.getString(6));

            // Add specialization and status (columns 7 and 8)
            try {
                if (cursor.getColumnCount() > 7) {
                    String specialization = cursor.getString(7);
                    user.setSpecialization(specialization != null ? specialization : "");
                }
                if (cursor.getColumnCount() > 8) {
                    String status = cursor.getString(8);
                    user.setStatus(status != null ? status : "Available");
                }
            } catch (Exception e) {
                // If columns don't exist, set defaults
                user.setSpecialization("");
                user.setStatus("Available");
            }
        }

        cursor.close();
        db.close();
        return user;
    }



    // Delete user
    public void deleteUser(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_USERS,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(userId)});
        db.close();
    }
}
