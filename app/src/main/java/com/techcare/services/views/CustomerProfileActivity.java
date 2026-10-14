package com.techcare.services.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.techcare.services.R;
import com.techcare.services.database.BookingDAO;
import com.techcare.services.database.UserDAO;
import com.techcare.services.models.User;
import com.techcare.services.utils.SessionManager;

public class CustomerProfileActivity extends Activity {

    private Button btnBack, btnUpdateProfile, btnChangePassword;
    private EditText etName, etPhone, etCurrentPassword, etNewPassword, etConfirmPassword;
    private TextView tvEmail, tvUserRole, tvTotalBookings, tvCompletedBookings;

    private SessionManager sessionManager;
    private UserDAO userDAO;
    private BookingDAO bookingDAO;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        sessionManager = new SessionManager(this);
        userDAO = new UserDAO(this);
        bookingDAO = new BookingDAO(this);

        // Initialize views
        initializeViews();

        // Load user data
        loadUserProfile();

        // Setup button listeners
        setupListeners();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        tvEmail = findViewById(R.id.tvEmail);
        tvUserRole = findViewById(R.id.tvUserRole);
        tvTotalBookings = findViewById(R.id.tvTotalBookings);
        tvCompletedBookings = findViewById(R.id.tvCompletedBookings);
    }

    private void loadUserProfile() {
        int userId = sessionManager.getUserId();
        currentUser = userDAO.getUserById(userId);

        if (currentUser != null) {
            etName.setText(currentUser.getName());
            tvEmail.setText(currentUser.getEmail());
            etPhone.setText(currentUser.getPhone());
            tvUserRole.setText(currentUser.getRole().toUpperCase());

            // Load statistics
            int totalBookings = bookingDAO.getBookingsByUserId(userId).size();
            int completedBookings = bookingDAO.getBookingsByUserIdAndStatus(userId, "Completed").size();

            tvTotalBookings.setText(String.valueOf(totalBookings));
            tvCompletedBookings.setText(String.valueOf(completedBookings));
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void updateProfile() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // Validation
        if (name.isEmpty()) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Phone is required");
            etPhone.requestFocus();
            return;
        }

        if (phone.length() != 10) {
            etPhone.setError("Phone must be 10 digits");
            etPhone.requestFocus();
            return;
        }

        // Update user object
        currentUser.setName(name);
        currentUser.setPhone(phone);

        // Update in database
        int result = userDAO.updateUser(currentUser);

        if (result > 0) {
            // Update session
            sessionManager.updateUserName(name);
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void changePassword() {
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validation
        if (currentPassword.isEmpty()) {
            etCurrentPassword.setError("Current password is required");
            etCurrentPassword.requestFocus();
            return;
        }

        if (newPassword.isEmpty()) {
            etNewPassword.setError("New password is required");
            etNewPassword.requestFocus();
            return;
        }

        if (newPassword.length() < 6) {
            etNewPassword.setError("Password must be at least 6 characters");
            etNewPassword.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Please confirm password");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        // Verify current password
        if (!currentUser.getPassword().equals(currentPassword)) {
            etCurrentPassword.setError("Current password is incorrect");
            etCurrentPassword.requestFocus();
            return;
        }

        // Update password
        currentUser.setPassword(newPassword);
        int result = userDAO.updateUser(currentUser);

        if (result > 0) {
            Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_LONG).show();

            // Clear password fields
            etCurrentPassword.setText("");
            etNewPassword.setText("");
            etConfirmPassword.setText("");
        } else {
            Toast.makeText(this, "Failed to change password", Toast.LENGTH_SHORT).show();
        }
    }
}
