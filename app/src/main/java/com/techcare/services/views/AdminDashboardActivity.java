package com.techcare.services.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.techcare.services.R;
import com.techcare.services.database.BookingDAO;
import com.techcare.services.database.UserDAO;
import com.techcare.services.models.Booking;
import com.techcare.services.utils.SessionManager;
import java.util.List;

public class AdminDashboardActivity extends Activity {

    private TextView tvWelcome, tvTotalBookings, tvPendingBookings, tvActiveTechnicians, tvTotalCustomers;
    private Button btnLogout;
    private LinearLayout btnManageBookings, btnManageServices, btnManageTechnicians, btnViewCustomers;
    private SessionManager sessionManager;
    private BookingDAO bookingDAO;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        sessionManager = new SessionManager(this);
        bookingDAO = new BookingDAO(this);
        userDAO = new UserDAO(this);

        // Check if logged in and is admin
        if (!sessionManager.isLoggedIn() || !sessionManager.getUserRole().equals("admin")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Initialize views
        tvWelcome = findViewById(R.id.tvWelcome);
        tvTotalBookings = findViewById(R.id.tvTotalBookings);
        tvPendingBookings = findViewById(R.id.tvPendingBookings);
        tvActiveTechnicians = findViewById(R.id.tvActiveTechnicians);
        tvTotalCustomers = findViewById(R.id.tvTotalCustomers);
        btnLogout = findViewById(R.id.btnLogout);
        btnManageBookings = findViewById(R.id.btnManageBookings);
        btnManageServices = findViewById(R.id.btnManageServices);
        btnManageTechnicians = findViewById(R.id.btnManageTechnicians);
        btnViewCustomers = findViewById(R.id.btnViewCustomers);

        // Set welcome message
        tvWelcome.setText("Welcome, " + sessionManager.getUserName());

        // Load statistics
        loadStatistics();

        // Logout button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
                Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Manage Bookings button
        btnManageBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminDashboardActivity.this, "Opening Manage Bookings...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminDashboardActivity.this, AdminBookingsActivity.class);
                startActivity(intent);
            }
        });

        // Manage Services button
        btnManageServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminDashboardActivity.this, "Opening Manage Services...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminDashboardActivity.this, AdminServicesActivity.class);  // ← CORRECT!
                startActivity(intent);
            }
        });



        // Manage Technicians button
        btnManageTechnicians.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminDashboardActivity.this, "Opening Manage Technicians...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminDashboardActivity.this, AdminTechniciansActivity.class);  // ← CORRECT!
                startActivity(intent);
            }
        });


        // View Customers button
        btnViewCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminDashboardActivity.this, "Opening View Customers...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminDashboardActivity.this, AdminCustomersActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadStatistics() {
        // Total bookings
        List<Booking> allBookings = bookingDAO.getAllBookings();
        tvTotalBookings.setText(String.valueOf(allBookings.size()));

        // Pending bookings
        int pendingCount = 0;
        for (Booking booking : allBookings) {
            if (booking.getStatus().equals("Pending")) {
                pendingCount++;
            }
        }
        tvPendingBookings.setText(String.valueOf(pendingCount));

        // Active technicians
        int techCount = userDAO.getAllTechnicians().size();
        tvActiveTechnicians.setText(String.valueOf(techCount));

        // Total customers
        int customerCount = userDAO.getAllCustomers().size();
        tvTotalCustomers.setText(String.valueOf(customerCount));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload statistics when returning to dashboard
        loadStatistics();
    }
}
