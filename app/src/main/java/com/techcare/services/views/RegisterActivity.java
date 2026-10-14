package com.techcare.services.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.techcare.services.R;
import com.techcare.services.database.UserDAO;
import com.techcare.services.models.User;
import com.techcare.services.utils.SessionManager;

public class RegisterActivity extends Activity {

    private EditText etName, etEmail, etPhone, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private UserDAO userDAO;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        // Initialize DAO and SessionManager
        userDAO = new UserDAO(this);
        sessionManager = new SessionManager(this);

        // Register button click
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Login text click
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty()) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter valid email");
            etEmail.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Phone is required");
            etPhone.requestFocus();
            return;
        }

        if (phone.length() != 10) {
            etPhone.setError("Enter valid 10-digit phone");
            etPhone.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        // Check if email already exists
        if (userDAO.isEmailExists(email)) {
            etEmail.setError("Email already registered");
            etEmail.requestFocus();
            Toast.makeText(this, "This email is already registered!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new user
        User newUser = new User(name, email, phone, password, "customer");
        long userId = userDAO.addUser(newUser);

        if (userId > 0) {
            Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();

            // Auto login after registration
            sessionManager.createLoginSession((int) userId, name, email, phone, "customer");

            // Go to Customer Dashboard
            Intent intent = new Intent(RegisterActivity.this, CustomerDashboardActivity.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "Registration failed. Try again!", Toast.LENGTH_SHORT).show();
        }
    }
}
