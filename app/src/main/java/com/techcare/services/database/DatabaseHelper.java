package com.techcare.services.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "techcare.db";
    private static final int DATABASE_VERSION = 3;

    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_DEVICES = "devices";
    public static final String TABLE_SERVICES = "services";
    public static final String TABLE_TECHNICIANS = "technicians";
    public static final String TABLE_BOOKINGS = "bookings";
    public static final String TABLE_NOTIFICATIONS = "notifications";

    // Common Column Names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CREATED_AT = "created_at";

    // Users Table Columns
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PHONE = "phone";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_ROLE = "role";
    public static final String COLUMN_USER_SPECIALIZATION = "specialization";
    public static final String COLUMN_USER_STATUS = "status";

    // Devices Table Columns
    public static final String COLUMN_DEVICE_NAME = "device_name";
    public static final String COLUMN_DEVICE_TYPE = "device_type";
    public static final String COLUMN_IMAGE_PATH = "image_path";

    // Services Table Columns
    public static final String COLUMN_SERVICE_DEVICE_ID = "device_id";
    public static final String COLUMN_SERVICE_NAME = "service_name";
    public static final String COLUMN_SERVICE_DESCRIPTION = "description";
    public static final String COLUMN_SERVICE_PRICE = "price";
    public static final String COLUMN_SERVICE_DURATION = "duration_estimate";

    // Technicians Table Columns
    public static final String COLUMN_TECH_NAME = "name";
    public static final String COLUMN_TECH_PHONE = "phone";
    public static final String COLUMN_TECH_EMAIL = "email";
    public static final String COLUMN_TECH_SPECIALIZATION = "specialization";
    public static final String COLUMN_TECH_STATUS = "status";

    // Bookings Table Columns
    public static final String COLUMN_BOOKING_USER_ID = "user_id";
    public static final String COLUMN_BOOKING_CUSTOMER_ID = "user_id";
    public static final String COLUMN_BOOKING_SERVICE_ID = "service_id";
    public static final String COLUMN_BOOKING_SERVICE_NAME = "service_name";
    public static final String COLUMN_BOOKING_ISSUE = "issue_description";
    public static final String COLUMN_BOOKING_IMAGE = "image_path";
    public static final String COLUMN_BOOKING_METHOD = "service_method";
    public static final String COLUMN_BOOKING_DATE = "booking_date";
    public static final String COLUMN_BOOKING_TIME = "pickup_time";
    public static final String COLUMN_BOOKING_ADDRESS = "address";
    public static final String COLUMN_BOOKING_STATUS = "status";
    public static final String COLUMN_BOOKING_TECH_ID = "technician_id";
    public static final String COLUMN_BOOKING_TECHNICIAN_ID = "technician_id";
    public static final String COLUMN_BOOKING_ESTIMATE = "estimated_completion";
    public static final String COLUMN_BOOKING_PRICE = "price";

    // Notifications Table Columns
    public static final String COLUMN_NOTIFICATION_USER_ID = "user_id";
    public static final String COLUMN_NOTIFICATION_BOOKING_ID = "booking_id";
    public static final String COLUMN_NOTIFICATION_MESSAGE = "message";
    public static final String COLUMN_NOTIFICATION_IS_READ = "is_read";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT UNIQUE,"
                + COLUMN_USER_PHONE + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT,"
                + COLUMN_USER_ROLE + " TEXT,"
                + COLUMN_USER_SPECIALIZATION + " TEXT,"
                + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + COLUMN_USER_STATUS + " TEXT DEFAULT 'Available'"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Create Devices Table
        String CREATE_DEVICES_TABLE = "CREATE TABLE " + TABLE_DEVICES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DEVICE_NAME + " TEXT,"
                + COLUMN_DEVICE_TYPE + " TEXT,"
                + COLUMN_IMAGE_PATH + " TEXT"
                + ")";
        db.execSQL(CREATE_DEVICES_TABLE);

        // Create Services Table
        String CREATE_SERVICES_TABLE = "CREATE TABLE " + TABLE_SERVICES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SERVICE_DEVICE_ID + " INTEGER,"
                + COLUMN_SERVICE_NAME + " TEXT,"
                + COLUMN_SERVICE_DESCRIPTION + " TEXT,"
                + COLUMN_SERVICE_PRICE + " REAL,"
                + COLUMN_SERVICE_DURATION + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_SERVICE_DEVICE_ID + ") REFERENCES "
                + TABLE_DEVICES + "(" + COLUMN_ID + ")"
                + ")";
        db.execSQL(CREATE_SERVICES_TABLE);

        // Create Technicians Table
        String CREATE_TECHNICIANS_TABLE = "CREATE TABLE " + TABLE_TECHNICIANS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TECH_NAME + " TEXT,"
                + COLUMN_TECH_PHONE + " TEXT,"
                + COLUMN_TECH_EMAIL + " TEXT,"
                + COLUMN_TECH_SPECIALIZATION + " TEXT,"
                + COLUMN_TECH_STATUS + " TEXT"
                + ")";
        db.execSQL(CREATE_TECHNICIANS_TABLE);

        // Create Bookings Table
        String CREATE_BOOKINGS_TABLE = "CREATE TABLE " + TABLE_BOOKINGS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_BOOKING_USER_ID + " INTEGER,"
                + COLUMN_BOOKING_SERVICE_ID + " INTEGER,"
                + COLUMN_BOOKING_SERVICE_NAME + " TEXT,"
                + COLUMN_BOOKING_ISSUE + " TEXT,"
                + COLUMN_BOOKING_IMAGE + " TEXT,"
                + COLUMN_BOOKING_METHOD + " TEXT,"
                + COLUMN_BOOKING_DATE + " TEXT,"
                + COLUMN_BOOKING_TIME + " TEXT,"
                + COLUMN_BOOKING_ADDRESS + " TEXT,"
                + COLUMN_BOOKING_STATUS + " TEXT,"
                + COLUMN_BOOKING_TECH_ID + " INTEGER,"
                + COLUMN_BOOKING_ESTIMATE + " TEXT,"
                + COLUMN_BOOKING_PRICE + " REAL,"
                + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(" + COLUMN_BOOKING_USER_ID + ") REFERENCES "
                + TABLE_USERS + "(" + COLUMN_ID + "),"
                + "FOREIGN KEY(" + COLUMN_BOOKING_SERVICE_ID + ") REFERENCES "
                + TABLE_SERVICES + "(" + COLUMN_ID + ")"
                + ")";
        db.execSQL(CREATE_BOOKINGS_TABLE);

        // Create Notifications Table
        String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOTIFICATION_USER_ID + " INTEGER,"
                + COLUMN_NOTIFICATION_BOOKING_ID + " INTEGER,"
                + COLUMN_NOTIFICATION_MESSAGE + " TEXT,"
                + COLUMN_NOTIFICATION_IS_READ + " INTEGER DEFAULT 0,"
                + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(" + COLUMN_NOTIFICATION_USER_ID + ") REFERENCES "
                + TABLE_USERS + "(" + COLUMN_ID + ")"
                + ")";
        db.execSQL(CREATE_NOTIFICATIONS_TABLE);

        // Insert sample data
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TECHNICIANS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        onCreate(db);
    }

    // Insert sample data
    private void insertSampleData(SQLiteDatabase db) {
        // Insert Admin User
        db.execSQL("INSERT INTO " + TABLE_USERS + " (" + COLUMN_USER_NAME + ", "
                + COLUMN_USER_EMAIL + ", " + COLUMN_USER_PHONE + ", "
                + COLUMN_USER_PASSWORD + ", " + COLUMN_USER_ROLE + ") VALUES " +
                "('Admin', 'admin@techcare.com', '9876543210', 'admin123', 'admin')");

        // Insert Sample Customer
        db.execSQL("INSERT INTO " + TABLE_USERS + " (" + COLUMN_USER_NAME + ", "
                + COLUMN_USER_EMAIL + ", " + COLUMN_USER_PHONE + ", "
                + COLUMN_USER_PASSWORD + ", " + COLUMN_USER_ROLE + ") VALUES " +
                "('John Doe', 'john@example.com', '9876543211', 'john123', 'customer')");

        // Insert Devices
        db.execSQL("INSERT INTO " + TABLE_DEVICES + " (" + COLUMN_DEVICE_NAME + ", "
                + COLUMN_DEVICE_TYPE + ") VALUES ('Smartphone', 'Mobile')");
        db.execSQL("INSERT INTO " + TABLE_DEVICES + " (" + COLUMN_DEVICE_NAME + ", "
                + COLUMN_DEVICE_TYPE + ") VALUES ('Laptop', 'Computer')");
        db.execSQL("INSERT INTO " + TABLE_DEVICES + " (" + COLUMN_DEVICE_NAME + ", "
                + COLUMN_DEVICE_TYPE + ") VALUES ('Television', 'Electronics')");
        db.execSQL("INSERT INTO " + TABLE_DEVICES + " (" + COLUMN_DEVICE_NAME + ", "
                + COLUMN_DEVICE_TYPE + ") VALUES ('Air Conditioner', 'Appliance')");
        db.execSQL("INSERT INTO " + TABLE_DEVICES + " (" + COLUMN_DEVICE_NAME + ", "
                + COLUMN_DEVICE_TYPE + ") VALUES ('Refrigerator', 'Appliance')");
        db.execSQL("INSERT INTO " + TABLE_DEVICES + " (" + COLUMN_DEVICE_NAME + ", "
                + COLUMN_DEVICE_TYPE + ") VALUES ('Washing Machine', 'Appliance')");

        // Insert Services for Smartphone (device_id = 1)
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (" + COLUMN_SERVICE_DEVICE_ID + ", "
                + COLUMN_SERVICE_NAME + ", " + COLUMN_SERVICE_DESCRIPTION + ", "
                + COLUMN_SERVICE_PRICE + ", " + COLUMN_SERVICE_DURATION + ") VALUES " +
                "(1, 'Screen Replacement', 'Replace broken or cracked screen', 2500.00, '2-3 hours')");
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (" + COLUMN_SERVICE_DEVICE_ID + ", "
                + COLUMN_SERVICE_NAME + ", " + COLUMN_SERVICE_DESCRIPTION + ", "
                + COLUMN_SERVICE_PRICE + ", " + COLUMN_SERVICE_DURATION + ") VALUES " +
                "(1, 'Battery Replacement', 'Replace old or damaged battery', 1500.00, '1-2 hours')");
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (" + COLUMN_SERVICE_DEVICE_ID + ", "
                + COLUMN_SERVICE_NAME + ", " + COLUMN_SERVICE_DESCRIPTION + ", "
                + COLUMN_SERVICE_PRICE + ", " + COLUMN_SERVICE_DURATION + ") VALUES " +
                "(1, 'Charging Port Repair', 'Fix charging port issues', 1200.00, '2 hours')");

        // Insert Services for Laptop (device_id = 2)
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (" + COLUMN_SERVICE_DEVICE_ID + ", "
                + COLUMN_SERVICE_NAME + ", " + COLUMN_SERVICE_DESCRIPTION + ", "
                + COLUMN_SERVICE_PRICE + ", " + COLUMN_SERVICE_DURATION + ") VALUES " +
                "(2, 'Laptop Not Charging', 'Diagnose and fix charging issues', 1800.00, '3-4 hours')");
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (" + COLUMN_SERVICE_DEVICE_ID + ", "
                + COLUMN_SERVICE_NAME + ", " + COLUMN_SERVICE_DESCRIPTION + ", "
                + COLUMN_SERVICE_PRICE + ", " + COLUMN_SERVICE_DURATION + ") VALUES " +
                "(2, 'Keyboard Replacement', 'Replace faulty keyboard', 2200.00, '2-3 hours')");
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (" + COLUMN_SERVICE_DEVICE_ID + ", "
                + COLUMN_SERVICE_NAME + ", " + COLUMN_SERVICE_DESCRIPTION + ", "
                + COLUMN_SERVICE_PRICE + ", " + COLUMN_SERVICE_DURATION + ") VALUES " +
                "(2, 'Virus Removal', 'Remove malware and viruses', 1000.00, '2-3 hours')");

        // Insert Services for Television (device_id = 3)
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (" + COLUMN_SERVICE_DEVICE_ID + ", "
                + COLUMN_SERVICE_NAME + ", " + COLUMN_SERVICE_DESCRIPTION + ", "
                + COLUMN_SERVICE_PRICE + ", " + COLUMN_SERVICE_DURATION + ") VALUES " +
                "(3, 'Display Not Working', 'Fix display issues', 3500.00, '1-2 days')");
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (" + COLUMN_SERVICE_DEVICE_ID + ", "
                + COLUMN_SERVICE_NAME + ", " + COLUMN_SERVICE_DESCRIPTION + ", "
                + COLUMN_SERVICE_PRICE + ", " + COLUMN_SERVICE_DURATION + ") VALUES " +
                "(3, 'Sound Problem', 'Repair audio issues', 2000.00, '3-5 hours')");
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (" + COLUMN_SERVICE_DEVICE_ID + ", "
                + COLUMN_SERVICE_NAME + ", " + COLUMN_SERVICE_DESCRIPTION + ", "
                + COLUMN_SERVICE_PRICE + ", " + COLUMN_SERVICE_DURATION + ") VALUES " +
                "(3, 'Remote Control Issue', 'Fix or replace remote', 800.00, '1 hour')");

        // Insert Services for Air Conditioner (device_id = 4)
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (" + COLUMN_SERVICE_DEVICE_ID + ", "
                + COLUMN_SERVICE_NAME + ", " + COLUMN_SERVICE_DESCRIPTION + ", "
                + COLUMN_SERVICE_PRICE + ", " + COLUMN_SERVICE_DURATION + ") VALUES " +
                "(4, 'AC Not Cooling', 'Diagnose and fix cooling issues', 1500.00, '2-3 hours')");
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (" + COLUMN_SERVICE_DEVICE_ID + ", "
                + COLUMN_SERVICE_NAME + ", " + COLUMN_SERVICE_DESCRIPTION + ", "
                + COLUMN_SERVICE_PRICE + ", " + COLUMN_SERVICE_DURATION + ") VALUES " +
                "(4, 'Gas Refilling', 'Refill AC refrigerant gas', 2500.00, '1-2 hours')");
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (" + COLUMN_SERVICE_DEVICE_ID + ", "
                + COLUMN_SERVICE_NAME + ", " + COLUMN_SERVICE_DESCRIPTION + ", "
                + COLUMN_SERVICE_PRICE + ", " + COLUMN_SERVICE_DURATION + ") VALUES " +
                "(4, 'AC Service', 'Complete AC servicing and cleaning', 1200.00, '2-3 hours')");

        // Insert Services for Refrigerator (device_id = 5)
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (" + COLUMN_SERVICE_DEVICE_ID + ", "
                + COLUMN_SERVICE_NAME + ", " + COLUMN_SERVICE_DESCRIPTION + ", "
                + COLUMN_SERVICE_PRICE + ", " + COLUMN_SERVICE_DURATION + ") VALUES " +
                "(5, 'Cooling Issue', 'Fix refrigerator cooling problems', 2000.00, '3-4 hours')");
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (" + COLUMN_SERVICE_DEVICE_ID + ", "
                + COLUMN_SERVICE_NAME + ", " + COLUMN_SERVICE_DESCRIPTION + ", "
                + COLUMN_SERVICE_PRICE + ", " + COLUMN_SERVICE_DURATION + ") VALUES " +
                "(5, 'Noise Problem', 'Reduce unusual sounds', 1200.00, '2-3 hours')");
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (" + COLUMN_SERVICE_DEVICE_ID + ", "
                + COLUMN_SERVICE_NAME + ", " + COLUMN_SERVICE_DESCRIPTION + ", "
                + COLUMN_SERVICE_PRICE + ", " + COLUMN_SERVICE_DURATION + ") VALUES " +
                "(5, 'Door Seal Replacement', 'Replace damaged door seals', 1500.00, '1-2 hours')");

        // Insert Services for Washing Machine (device_id = 6)
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (" + COLUMN_SERVICE_DEVICE_ID + ", "
                + COLUMN_SERVICE_NAME + ", " + COLUMN_SERVICE_DESCRIPTION + ", "
                + COLUMN_SERVICE_PRICE + ", " + COLUMN_SERVICE_DURATION + ") VALUES " +
                "(6, 'Not Spinning', 'Fix spinning drum issues', 1800.00, '2-3 hours')");
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (" + COLUMN_SERVICE_DEVICE_ID + ", "
                + COLUMN_SERVICE_NAME + ", " + COLUMN_SERVICE_DESCRIPTION + ", "
                + COLUMN_SERVICE_PRICE + ", " + COLUMN_SERVICE_DURATION + ") VALUES " +
                "(6, 'Water Leakage', 'Fix water leakage problems', 1500.00, '2-4 hours')");
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (" + COLUMN_SERVICE_DEVICE_ID + ", "
                + COLUMN_SERVICE_NAME + ", " + COLUMN_SERVICE_DESCRIPTION + ", "
                + COLUMN_SERVICE_PRICE + ", " + COLUMN_SERVICE_DURATION + ") VALUES " +
                "(6, 'Drainage Problem', 'Fix water drainage issues', 1300.00, '2-3 hours')");

        // Insert Technicians
        db.execSQL("INSERT INTO " + TABLE_TECHNICIANS + " (" + COLUMN_TECH_NAME + ", "
                + COLUMN_TECH_PHONE + ", " + COLUMN_TECH_EMAIL + ", "
                + COLUMN_TECH_SPECIALIZATION + ", " + COLUMN_TECH_STATUS + ") VALUES " +
                "('Rajesh Kumar', '9876501234', 'rajesh@techcare.com', 'Mobile & Laptop', 'available')");
        db.execSQL("INSERT INTO " + TABLE_TECHNICIANS + " (" + COLUMN_TECH_NAME + ", "
                + COLUMN_TECH_PHONE + ", " + COLUMN_TECH_EMAIL + ", "
                + COLUMN_TECH_SPECIALIZATION + ", " + COLUMN_TECH_STATUS + ") VALUES " +
                "('Priya Sharma', '9876502345', 'priya@techcare.com', 'Home Appliances', 'available')");
        db.execSQL("INSERT INTO " + TABLE_TECHNICIANS + " (" + COLUMN_TECH_NAME + ", "
                + COLUMN_TECH_PHONE + ", " + COLUMN_TECH_EMAIL + ", "
                + COLUMN_TECH_SPECIALIZATION + ", " + COLUMN_TECH_STATUS + ") VALUES " +
                "('Amit Patel', '9876503456', 'amit@techcare.com', 'Electronics', 'available')");
    }
}
