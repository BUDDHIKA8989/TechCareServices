package com.techcare.services.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.techcare.services.R;
import com.techcare.services.adapters.BookingAdapter;
import com.techcare.services.database.BookingDAO;
import com.techcare.services.models.Booking;
import java.util.ArrayList;
import java.util.List;

public class AdminViewBookingsActivity extends Activity {

    private Button btnBack, btnFilterAll, btnFilterReceived, btnFilterOngoing, btnFilterReady, btnFilterCompleted;
    private RecyclerView rvBookings;
    private LinearLayout emptyState;
    private BookingDAO bookingDAO;
    private BookingAdapter bookingAdapter;
    private List<Booking> allBookings;
    private List<Booking> filteredBookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_bookings);

        bookingDAO = new BookingDAO(this);

        // Initialize views
        btnBack = findViewById(R.id.btnBack);
        btnFilterAll = findViewById(R.id.btnFilterAll);
        btnFilterReceived = findViewById(R.id.btnFilterReceived);
        btnFilterOngoing = findViewById(R.id.btnFilterOngoing);
        btnFilterReady = findViewById(R.id.btnFilterReady);
        btnFilterCompleted = findViewById(R.id.btnFilterCompleted);
        rvBookings = findViewById(R.id.rvBookings);
        emptyState = findViewById(R.id.emptyState);

        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Filter buttons
        setupFilterButtons();

        // Load bookings
        loadBookings("All");
    }

    private void setupFilterButtons() {
        btnFilterAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveFilter(btnFilterAll);
                loadBookings("All");
            }
        });

        btnFilterReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveFilter(btnFilterReceived);
                loadBookings("Received");
            }
        });

        btnFilterOngoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveFilter(btnFilterOngoing);
                loadBookings("Under Repair");
            }
        });

        btnFilterReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveFilter(btnFilterReady);
                loadBookings("Ready for Pickup");
            }
        });

        btnFilterCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveFilter(btnFilterCompleted);
                loadBookings("Completed");
            }
        });
    }

    private void setActiveFilter(Button activeButton) {
        btnFilterAll.setBackgroundTintList(getColorStateList(android.R.color.transparent));
        btnFilterAll.setTextColor(0xFF2196F3);
        btnFilterReceived.setBackgroundTintList(getColorStateList(android.R.color.transparent));
        btnFilterReceived.setTextColor(0xFF2196F3);
        btnFilterOngoing.setBackgroundTintList(getColorStateList(android.R.color.transparent));
        btnFilterOngoing.setTextColor(0xFF2196F3);
        btnFilterReady.setBackgroundTintList(getColorStateList(android.R.color.transparent));
        btnFilterReady.setTextColor(0xFF2196F3);
        btnFilterCompleted.setBackgroundTintList(getColorStateList(android.R.color.transparent));
        btnFilterCompleted.setTextColor(0xFF2196F3);

        activeButton.setBackgroundTintList(getColorStateList(android.R.color.holo_blue_light));
        activeButton.setTextColor(0xFFFFFFFF);
    }

    private void loadBookings(String filter) {
        allBookings = bookingDAO.getAllBookings();

        if (filter.equals("All")) {
            filteredBookings = allBookings;
        } else {
            filteredBookings = new ArrayList<>();
            for (Booking booking : allBookings) {
                if (booking.getStatus().equals(filter)) {
                    filteredBookings.add(booking);
                }
            }
        }

        if (filteredBookings.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            rvBookings.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            rvBookings.setVisibility(View.VISIBLE);

            bookingAdapter = new BookingAdapter(this, filteredBookings, new BookingAdapter.OnBookingClickListener() {
                @Override
                public void onBookingClick(Booking booking) {
                    Intent intent = new Intent(AdminViewBookingsActivity.this, AdminBookingDetailsActivity.class);
                    intent.putExtra("booking_id", booking.getId());
                    startActivity(intent);
                }
            });

            rvBookings.setLayoutManager(new LinearLayoutManager(this));
            rvBookings.setAdapter(bookingAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookings("All");
        setActiveFilter(btnFilterAll);
    }
}
