package com.techcare.services.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.techcare.services.R;
import com.techcare.services.database.BookingDAO;
import com.techcare.services.database.UserDAO;
import com.techcare.services.utils.SessionManager;

public class MainActivity extends Activity {

    private TextView tvWelcome, tvTotalBookings, tvPendingBookings, tvCompletedBookings, tvTotalCustomers;
    private Button btnLogout, btnViewBookings, btnManageServices, btnManageTechnicians, btnViewCustomers;
    private SessionManager sessionManager;
    private BookingDAO bookingDAO;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        bookingDAO = new BookingDAO(this);
        userDAO = new UserDAO(this);

        // Check if logged in and is admin
        if (!sessionManager.isLoggedIn() || !sessionManager.isAdmin()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Initialize views
        initializeViews();

        // Set welcome message
        tvWelcome.setText("Welcome, " + sessionManager.getUserName());

        // Load statistics
        loadStatistics();

        // Button listeners
        setupListeners();
    }

    private void initializeViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvTotalBookings = findViewById(R.id.tvTotalBookings);
        tvPendingBookings = findViewById(R.id.tvPendingBookings);
        tvCompletedBookings = findViewById(R.id.tvCompletedBookings);
        tvTotalCustomers = findViewById(R.id.tvTotalCustomers);
        btnLogout = findViewById(R.id.btnLogout);
        btnViewBookings = findViewById(R.id.btnViewBookings);
        btnManageServices = findViewById(R.id.btnManageServices);
        btnManageTechnicians = findViewById(R.id.btnManageTechnicians);
        btnViewCustomers = findViewById(R.id.btnViewCustomers);
    }

    private void loadStatistics() {
        int totalBookings = bookingDAO.getTotalBookingsCount();
        int pendingBookings = bookingDAO.getBookingCountByStatus("Received");
        int completedBookings = bookingDAO.getBookingCountByStatus("Completed");
        int totalCustomers = userDAO.getAllCustomers().size();

        tvTotalBookings.setText(String.valueOf(totalBookings));
        tvPendingBookings.setText(String.valueOf(pendingBookings));
        tvCompletedBookings.setText(String.valueOf(completedBookings));
        tvTotalCustomers.setText(String.valueOf(totalCustomers));
    }

    private void setupListeners() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnViewBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FIXED: Use AdminBookingsActivity instead of AdminViewBookingsActivity
                Intent intent = new Intent(MainActivity.this, AdminBookingsActivity.class);
                startActivity(intent);
            }
        });

        btnManageServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FIXED: Use AdminServicesActivity instead of ManageServicesActivity
                Intent intent = new Intent(MainActivity.this, AdminServicesActivity.class);
                startActivity(intent);
            }
        });

        btnManageTechnicians.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FIXED: Use AdminTechniciansActivity instead of ManageTechniciansActivity
                Intent intent = new Intent(MainActivity.this, AdminTechniciansActivity.class);
                startActivity(intent);
            }
        });

        btnViewCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FIXED: Use AdminCustomersActivity instead of ViewCustomersActivity
                Intent intent = new Intent(MainActivity.this, AdminCustomersActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload statistics when returning to dashboard
        loadStatistics();
    }
}
