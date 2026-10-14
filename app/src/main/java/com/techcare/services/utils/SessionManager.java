package com.techcare.services.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String PREF_NAME = "TechCareSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_PHONE = "userPhone";
    private static final String KEY_USER_ROLE = "userRole";

    private static final String KEY_NAME = "userName";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Create login session
    public void createLoginSession(int userId, String name, String email, String phone, String role) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_PHONE, phone);
        editor.putString(KEY_USER_ROLE, role);
        editor.apply();
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Get user ID
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    // Get user name
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    // Get user email
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
    }

    // Get user phone
    public String getUserPhone() {
        return sharedPreferences.getString(KEY_USER_PHONE, "");
    }

    // Get user role
    public String getUserRole() {
        return sharedPreferences.getString(KEY_USER_ROLE, "");
    }

    // Check if user is admin
    public boolean isAdmin() {
        return "admin".equals(getUserRole());
    }

    // Logout user
    public void logoutUser() {
        editor.clear();
        editor.apply();
    }

    public void updateUserName(String name) {
        editor.putString(KEY_NAME, name);
        editor.commit();
    }

}
