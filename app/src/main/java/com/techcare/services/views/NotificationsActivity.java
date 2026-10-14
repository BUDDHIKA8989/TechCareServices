package com.techcare.services.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.techcare.services.R;
import com.techcare.services.adapters.NotificationAdapter;
import com.techcare.services.database.NotificationDAO;
import com.techcare.services.models.Notification;
import com.techcare.services.utils.SessionManager;
import java.util.List;

public class NotificationsActivity extends Activity {

    private Button btnBack, btnClearAll, btnFilterAll, btnFilterUnread, btnFilterRead;
    private RecyclerView rvNotifications;
    private LinearLayout emptyState;
    private SessionManager sessionManager;
    private NotificationDAO notificationDAO;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;
    private String currentFilter = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        sessionManager = new SessionManager(this);
        notificationDAO = new NotificationDAO(this);

        // Initialize views
        initializeViews();

        // Setup listeners
        setupListeners();

        // Load notifications
        loadNotifications(currentFilter);
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnClearAll = findViewById(R.id.btnClearAll);
        btnFilterAll = findViewById(R.id.btnFilterAll);
        btnFilterUnread = findViewById(R.id.btnFilterUnread);
        btnFilterRead = findViewById(R.id.btnFilterRead);
        rvNotifications = findViewById(R.id.rvNotifications);
        emptyState = findViewById(R.id.emptyState);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClearAllDialog();
            }
        });

        btnFilterAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilter = "all";
                setActiveFilter(btnFilterAll);
                loadNotifications(currentFilter);
            }
        });

        btnFilterUnread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilter = "unread";
                setActiveFilter(btnFilterUnread);
                loadNotifications(currentFilter);
            }
        });

        btnFilterRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilter = "read";
                setActiveFilter(btnFilterRead);
                loadNotifications(currentFilter);
            }
        });
    }

    private void setActiveFilter(Button activeButton) {
        // Reset all buttons to inactive state
        btnFilterAll.setBackgroundColor(0xFFE3F2FD);
        btnFilterAll.setTextColor(0xFF2196F3);

        btnFilterUnread.setBackgroundColor(0xFFE3F2FD);
        btnFilterUnread.setTextColor(0xFF2196F3);

        btnFilterRead.setBackgroundColor(0xFFE3F2FD);
        btnFilterRead.setTextColor(0xFF2196F3);

        // Set active button to highlighted state
        activeButton.setBackgroundColor(0xFF2196F3);
        activeButton.setTextColor(0xFFFFFFFF);
    }

    private void loadNotifications(String filter) {
        int userId = sessionManager.getUserId();

        switch (filter) {
            case "all":
                notificationList = notificationDAO.getNotificationsByUserId(userId);
                break;
            case "unread":
                notificationList = notificationDAO.getUnreadNotifications(userId);
                break;
            case "read":
                notificationList = notificationDAO.getReadNotifications(userId);
                break;
            default:
                notificationList = notificationDAO.getNotificationsByUserId(userId);
        }

        if (notificationList == null || notificationList.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            rvNotifications.setVisibility(View.GONE);
            btnClearAll.setEnabled(false);
        } else {
            emptyState.setVisibility(View.GONE);
            rvNotifications.setVisibility(View.VISIBLE);
            btnClearAll.setEnabled(true);

            notificationAdapter = new NotificationAdapter(this, notificationList,
                    new NotificationAdapter.OnNotificationClickListener() {
                        @Override
                        public void onViewBookingClick(Notification notification) {
                            // Mark as read
                            notificationDAO.markAsRead(notification.getId());

                            // Navigate to booking details
                            Intent intent = new Intent(NotificationsActivity.this, BookingDetailsActivity.class);
                            intent.putExtra("booking_id", notification.getBookingId());
                            startActivity(intent);

                            // Reload notifications after returning
                            finish();
                        }
                    });

            rvNotifications.setLayoutManager(new LinearLayoutManager(this));
            rvNotifications.setAdapter(notificationAdapter);
        }
    }

    private void showClearAllDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Clear All Notifications");
        builder.setMessage("Are you sure you want to delete all notifications?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int userId = sessionManager.getUserId();
                int deleted = notificationDAO.clearAllNotifications(userId);
                Toast.makeText(NotificationsActivity.this,
                        deleted + " notifications cleared", Toast.LENGTH_SHORT).show();
                loadNotifications(currentFilter);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload notifications when returning to this screen
        loadNotifications(currentFilter);
    }
}
