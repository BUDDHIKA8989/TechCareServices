package com.techcare.services.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.techcare.services.R;
import com.techcare.services.adapters.DeviceAdapter;
import com.techcare.services.database.DeviceDAO;
import com.techcare.services.database.NotificationDAO;
import com.techcare.services.models.Device;
import com.techcare.services.utils.SessionManager;
import java.util.List;

public class CustomerDashboardActivity extends Activity {

    private TextView tvWelcome, tvNotificationBadge;
    private Button btnLogout, btnHome, btnBookings, btnProfile, btnNotifications;
    private RecyclerView rvDevices;
    private SessionManager sessionManager;
    private DeviceDAO deviceDAO;
    private NotificationDAO notificationDAO;
    private DeviceAdapter deviceAdapter;
    private List<Device> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        sessionManager = new SessionManager(this);
        deviceDAO = new DeviceDAO(this);
        notificationDAO = new NotificationDAO(this);

        // Check if logged in
        if (!sessionManager.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Initialize views
        tvWelcome = findViewById(R.id.tvWelcome);
        btnLogout = findViewById(R.id.btnLogout);
        btnHome = findViewById(R.id.btnHome);
        btnBookings = findViewById(R.id.btnBookings);
        btnProfile = findViewById(R.id.btnProfile);
        btnNotifications = findViewById(R.id.btnNotifications);
        tvNotificationBadge = findViewById(R.id.tvNotificationBadge);
        rvDevices = findViewById(R.id.rvDevices);

        // Set welcome message
        tvWelcome.setText("Welcome, " + sessionManager.getUserName());

        // Load devices
        loadDevices();

        // Button clicks
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
                Intent intent = new Intent(CustomerDashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Already on home
            }
        });

        btnBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerDashboardActivity.this, MyBookingsActivity.class);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerDashboardActivity.this, CustomerProfileActivity.class);
                startActivity(intent);
            }
        });

        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerDashboardActivity.this, NotificationsActivity.class);
                startActivity(intent);
            }
        });

        // Update notification badge
        updateNotificationBadge();
    }

    private void loadDevices() {
        deviceList = deviceDAO.getAllDevices();

        deviceAdapter = new DeviceAdapter(this, deviceList, new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onDeviceClick(Device device) {
                // Navigate to services list for this device
                Intent intent = new Intent(CustomerDashboardActivity.this, DeviceServicesActivity.class);
                intent.putExtra("device_id", device.getId());
                intent.putExtra("device_name", device.getDeviceName());
                startActivity(intent);
            }
        });

        rvDevices.setLayoutManager(new LinearLayoutManager(this));
        rvDevices.setAdapter(deviceAdapter);
    }

    private void updateNotificationBadge() {
        int unreadCount = notificationDAO.getUnreadCount(sessionManager.getUserId());
        if (unreadCount > 0) {
            tvNotificationBadge.setVisibility(View.VISIBLE);
            tvNotificationBadge.setText(String.valueOf(unreadCount));
        } else {
            tvNotificationBadge.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update notification badge when returning to dashboard
        updateNotificationBadge();
    }
}
