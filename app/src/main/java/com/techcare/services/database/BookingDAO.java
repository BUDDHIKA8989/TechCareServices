package com.techcare.services.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.techcare.services.models.Booking;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    private DatabaseHelper dbHelper;

    public BookingDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Add new booking
    public long addBooking(Booking booking) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_BOOKING_USER_ID, booking.getUserId());
        values.put(DatabaseHelper.COLUMN_BOOKING_SERVICE_ID, booking.getServiceId());
        values.put(DatabaseHelper.COLUMN_BOOKING_SERVICE_NAME, booking.getServiceName());
        values.put(DatabaseHelper.COLUMN_BOOKING_ISSUE, booking.getIssueDescription());
        values.put(DatabaseHelper.COLUMN_BOOKING_IMAGE, booking.getImagePath());
        values.put(DatabaseHelper.COLUMN_BOOKING_METHOD, booking.getServiceMethod());
        values.put(DatabaseHelper.COLUMN_BOOKING_DATE, booking.getBookingDate());
        values.put(DatabaseHelper.COLUMN_BOOKING_TIME, booking.getPickupTime());
        values.put(DatabaseHelper.COLUMN_BOOKING_ADDRESS, booking.getAddress());
        values.put(DatabaseHelper.COLUMN_BOOKING_STATUS, booking.getStatus());
        values.put(DatabaseHelper.COLUMN_BOOKING_TECH_ID, booking.getTechnicianId());
        values.put(DatabaseHelper.COLUMN_BOOKING_ESTIMATE, booking.getEstimatedCompletion());
        values.put(DatabaseHelper.COLUMN_BOOKING_PRICE, booking.getPrice());  // ← NEW

        long id = db.insert(DatabaseHelper.TABLE_BOOKINGS, null, values);
        db.close();
        return id;
    }

    // Get all bookings
    public List<Booking> getAllBookings() {
        List<Booking> bookingList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_BOOKINGS +
                " ORDER BY " + DatabaseHelper.COLUMN_ID + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking();
                booking.setId(cursor.getInt(0));
                booking.setUserId(cursor.getInt(1));
                booking.setServiceId(cursor.getInt(2));


                try {
                    booking.setServiceName(cursor.getString(3));
                    booking.setIssueDescription(cursor.getString(4));
                    booking.setImagePath(cursor.getString(5));
                    booking.setServiceMethod(cursor.getString(6));
                    booking.setBookingDate(cursor.getString(7));
                    booking.setPickupTime(cursor.getString(8));
                    booking.setAddress(cursor.getString(9));
                    booking.setStatus(cursor.getString(10));
                    booking.setTechnicianId(cursor.getInt(11));
                    booking.setEstimatedCompletion(cursor.getString(12));

                    if (cursor.getColumnCount() > 13) {
                        booking.setPrice(cursor.getDouble(13));
                    }

                    if (cursor.getColumnCount() > 14) {
                        booking.setCreatedAt(cursor.getString(14));
                    }
                } catch (Exception e) {
                    // Fallback for old schema without service_name and price
                    booking.setIssueDescription(cursor.getString(3));
                    booking.setImagePath(cursor.getString(4));
                    booking.setServiceMethod(cursor.getString(5));
                    booking.setBookingDate(cursor.getString(6));
                    booking.setPickupTime(cursor.getString(7));
                    booking.setAddress(cursor.getString(8));
                    booking.setStatus(cursor.getString(9));
                    booking.setTechnicianId(cursor.getInt(10));
                    booking.setEstimatedCompletion(cursor.getString(11));
                    booking.setCreatedAt(cursor.getString(12));
                    booking.setServiceName("");
                    booking.setPrice(0.0);
                }

                bookingList.add(booking);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return bookingList;
    }

    // Get bookings by user ID (UPDATED)
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookingList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_BOOKINGS +
                " WHERE " + DatabaseHelper.COLUMN_BOOKING_USER_ID + " = ?" +
                " ORDER BY " + DatabaseHelper.COLUMN_ID + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking();
                booking.setId(cursor.getInt(0));
                booking.setUserId(cursor.getInt(1));
                booking.setServiceId(cursor.getInt(2));

                try {
                    booking.setServiceName(cursor.getString(3));
                    booking.setIssueDescription(cursor.getString(4));
                    booking.setImagePath(cursor.getString(5));
                    booking.setServiceMethod(cursor.getString(6));
                    booking.setBookingDate(cursor.getString(7));
                    booking.setPickupTime(cursor.getString(8));
                    booking.setAddress(cursor.getString(9));
                    booking.setStatus(cursor.getString(10));
                    booking.setTechnicianId(cursor.getInt(11));
                    booking.setEstimatedCompletion(cursor.getString(12));

                    if (cursor.getColumnCount() > 13) {
                        booking.setPrice(cursor.getDouble(13));
                    }
                    if (cursor.getColumnCount() > 14) {
                        booking.setCreatedAt(cursor.getString(14));
                    }
                } catch (Exception e) {
                    booking.setIssueDescription(cursor.getString(3));
                    booking.setImagePath(cursor.getString(4));
                    booking.setServiceMethod(cursor.getString(5));
                    booking.setBookingDate(cursor.getString(6));
                    booking.setPickupTime(cursor.getString(7));
                    booking.setAddress(cursor.getString(8));
                    booking.setStatus(cursor.getString(9));
                    booking.setTechnicianId(cursor.getInt(10));
                    booking.setEstimatedCompletion(cursor.getString(11));
                    booking.setCreatedAt(cursor.getString(12));
                    booking.setServiceName("");
                    booking.setPrice(0.0);
                }

                bookingList.add(booking);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return bookingList;
    }

    // Get bookings by customer ID (NEW - for Admin)
    public List<Booking> getBookingsByCustomerId(int customerId) {
        return getBookingsByUserId(customerId);  // Same as getBookingsByUserId
    }

    // Get bookings by status (UPDATED)
    public List<Booking> getBookingsByStatus(String status) {
        List<Booking> bookingList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_BOOKINGS +
                " WHERE " + DatabaseHelper.COLUMN_BOOKING_STATUS + " = ?" +
                " ORDER BY " + DatabaseHelper.COLUMN_ID + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{status});

        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking();
                booking.setId(cursor.getInt(0));
                booking.setUserId(cursor.getInt(1));
                booking.setServiceId(cursor.getInt(2));

                try {
                    booking.setServiceName(cursor.getString(3));
                    booking.setIssueDescription(cursor.getString(4));
                    booking.setImagePath(cursor.getString(5));
                    booking.setServiceMethod(cursor.getString(6));
                    booking.setBookingDate(cursor.getString(7));
                    booking.setPickupTime(cursor.getString(8));
                    booking.setAddress(cursor.getString(9));
                    booking.setStatus(cursor.getString(10));
                    booking.setTechnicianId(cursor.getInt(11));
                    booking.setEstimatedCompletion(cursor.getString(12));

                    if (cursor.getColumnCount() > 13) {
                        booking.setPrice(cursor.getDouble(13));
                    }
                    if (cursor.getColumnCount() > 14) {
                        booking.setCreatedAt(cursor.getString(14));
                    }
                } catch (Exception e) {
                    booking.setIssueDescription(cursor.getString(3));
                    booking.setImagePath(cursor.getString(4));
                    booking.setServiceMethod(cursor.getString(5));
                    booking.setBookingDate(cursor.getString(6));
                    booking.setPickupTime(cursor.getString(7));
                    booking.setAddress(cursor.getString(8));
                    booking.setStatus(cursor.getString(9));
                    booking.setTechnicianId(cursor.getInt(10));
                    booking.setEstimatedCompletion(cursor.getString(11));
                    booking.setCreatedAt(cursor.getString(12));
                    booking.setServiceName("");
                    booking.setPrice(0.0);
                }

                bookingList.add(booking);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return bookingList;
    }

    // Get booking by ID (UPDATED)
    public Booking getBookingById(int bookingId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Booking booking = null;

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_BOOKINGS +
                " WHERE " + DatabaseHelper.COLUMN_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(bookingId)});

        if (cursor.moveToFirst()) {
            booking = new Booking();
            booking.setId(cursor.getInt(0));
            booking.setUserId(cursor.getInt(1));
            booking.setServiceId(cursor.getInt(2));

            try {
                booking.setServiceName(cursor.getString(3));
                booking.setIssueDescription(cursor.getString(4));
                booking.setImagePath(cursor.getString(5));
                booking.setServiceMethod(cursor.getString(6));
                booking.setBookingDate(cursor.getString(7));
                booking.setPickupTime(cursor.getString(8));
                booking.setAddress(cursor.getString(9));
                booking.setStatus(cursor.getString(10));
                booking.setTechnicianId(cursor.getInt(11));
                booking.setEstimatedCompletion(cursor.getString(12));

                if (cursor.getColumnCount() > 13) {
                    booking.setPrice(cursor.getDouble(13));
                }
                if (cursor.getColumnCount() > 14) {
                    booking.setCreatedAt(cursor.getString(14));
                }
            } catch (Exception e) {
                booking.setIssueDescription(cursor.getString(3));
                booking.setImagePath(cursor.getString(4));
                booking.setServiceMethod(cursor.getString(5));
                booking.setBookingDate(cursor.getString(6));
                booking.setPickupTime(cursor.getString(7));
                booking.setAddress(cursor.getString(8));
                booking.setStatus(cursor.getString(9));
                booking.setTechnicianId(cursor.getInt(10));
                booking.setEstimatedCompletion(cursor.getString(11));
                booking.setCreatedAt(cursor.getString(12));
                booking.setServiceName("");
                booking.setPrice(0.0);
            }
        }

        cursor.close();
        db.close();
        return booking;
    }

    // Update booking status
    public void updateBookingStatus(int bookingId, String status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_BOOKING_STATUS, status);

        db.update(DatabaseHelper.TABLE_BOOKINGS, values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(bookingId)});
        db.close();
    }

    // Assign technician to booking
    public void assignTechnician(int bookingId, int technicianId, String estimatedCompletion) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_BOOKING_TECH_ID, technicianId);
        values.put(DatabaseHelper.COLUMN_BOOKING_ESTIMATE, estimatedCompletion);

        db.update(DatabaseHelper.TABLE_BOOKINGS, values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(bookingId)});
        db.close();
    }

    // Update complete booking (UPDATED)
    public int updateBooking(Booking booking) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_BOOKING_STATUS, booking.getStatus());
        values.put(DatabaseHelper.COLUMN_BOOKING_TECH_ID, booking.getTechnicianId());
        values.put(DatabaseHelper.COLUMN_BOOKING_ESTIMATE, booking.getEstimatedCompletion());

        int rows = db.update(DatabaseHelper.TABLE_BOOKINGS, values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(booking.getId())});
        db.close();
        return rows;
    }

    // Get booking count by status
    public int getBookingCountByStatus(String status) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_BOOKINGS +
                " WHERE " + DatabaseHelper.COLUMN_BOOKING_STATUS + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{status});
        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }

    // Get total bookings count
    public int getTotalBookingsCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_BOOKINGS;

        Cursor cursor = db.rawQuery(query, null);
        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }

    // Get bookings by user ID and status
    public List<Booking> getBookingsByUserIdAndStatus(int userId, String status) {
        List<Booking> bookings = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_BOOKINGS +
                " WHERE " + DatabaseHelper.COLUMN_BOOKING_USER_ID + " = ? AND " +
                DatabaseHelper.COLUMN_BOOKING_STATUS + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), status});

        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking();
                booking.setId(cursor.getInt(0));
                booking.setUserId(cursor.getInt(1));
                booking.setServiceId(cursor.getInt(2));

                try {
                    booking.setServiceName(cursor.getString(3));
                    booking.setIssueDescription(cursor.getString(4));
                    booking.setImagePath(cursor.getString(5));
                    booking.setServiceMethod(cursor.getString(6));
                    booking.setBookingDate(cursor.getString(7));
                    booking.setPickupTime(cursor.getString(8));
                    booking.setAddress(cursor.getString(9));
                    booking.setStatus(cursor.getString(10));
                    booking.setTechnicianId(cursor.getInt(11));
                    booking.setEstimatedCompletion(cursor.getString(12));

                    if (cursor.getColumnCount() > 13) {
                        booking.setPrice(cursor.getDouble(13));
                    }
                } catch (Exception e) {
                    booking.setIssueDescription(cursor.getString(3));
                    booking.setImagePath(cursor.getString(4));
                    booking.setServiceMethod(cursor.getString(5));
                    booking.setBookingDate(cursor.getString(6));
                    booking.setPickupTime(cursor.getString(7));
                    booking.setAddress(cursor.getString(8));
                    booking.setStatus(cursor.getString(9));
                    booking.setTechnicianId(cursor.getInt(10));
                    booking.setEstimatedCompletion(cursor.getString(11));
                    booking.setServiceName("");
                    booking.setPrice(0.0);
                }

                bookings.add(booking);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return bookings;
    }

    // Get bookings by technician ID (NEW - for technician dashboard)
    public List<Booking> getBookingsByTechnicianId(int technicianId) {
        List<Booking> bookingList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_BOOKINGS +
                " WHERE " + DatabaseHelper.COLUMN_BOOKING_TECH_ID + " = ?" +
                " ORDER BY " + DatabaseHelper.COLUMN_ID + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(technicianId)});

        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking();
                booking.setId(cursor.getInt(0));
                booking.setUserId(cursor.getInt(1));
                booking.setServiceId(cursor.getInt(2));

                try {
                    booking.setServiceName(cursor.getString(3));
                    booking.setIssueDescription(cursor.getString(4));
                    booking.setImagePath(cursor.getString(5));
                    booking.setServiceMethod(cursor.getString(6));
                    booking.setBookingDate(cursor.getString(7));
                    booking.setPickupTime(cursor.getString(8));
                    booking.setAddress(cursor.getString(9));
                    booking.setStatus(cursor.getString(10));
                    booking.setTechnicianId(cursor.getInt(11));
                    booking.setEstimatedCompletion(cursor.getString(12));

                    if (cursor.getColumnCount() > 13) {
                        booking.setPrice(cursor.getDouble(13));
                    }
                    if (cursor.getColumnCount() > 14) {
                        booking.setCreatedAt(cursor.getString(14));
                    }
                } catch (Exception e) {
                    booking.setIssueDescription(cursor.getString(3));
                    booking.setImagePath(cursor.getString(4));
                    booking.setServiceMethod(cursor.getString(5));
                    booking.setBookingDate(cursor.getString(6));
                    booking.setPickupTime(cursor.getString(7));
                    booking.setAddress(cursor.getString(8));
                    booking.setStatus(cursor.getString(9));
                    booking.setTechnicianId(cursor.getInt(10));
                    booking.setEstimatedCompletion(cursor.getString(11));
                    booking.setCreatedAt(cursor.getString(12));
                    booking.setServiceName("");
                    booking.setPrice(0.0);
                }

                bookingList.add(booking);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return bookingList;
    }

    // Delete booking
    public void deleteBooking(int bookingId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_BOOKINGS,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(bookingId)});
        db.close();
    }
}
