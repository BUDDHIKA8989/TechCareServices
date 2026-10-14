package com.techcare.services.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.techcare.services.R;
import com.techcare.services.adapters.BookingAdapter;
import com.techcare.services.database.BookingDAO;
import com.techcare.services.database.NotificationDAO;
import com.techcare.services.database.UserDAO;
import com.techcare.services.models.Booking;
import com.techcare.services.models.Notification;
import com.techcare.services.models.User;
import java.util.ArrayList;
import java.util.List;

public class AdminBookingsActivity extends Activity {

    private Button btnBack;
    private Spinner spinnerFilterStatus;
    private RecyclerView rvBookings;
    private BookingDAO bookingDAO;
    private UserDAO userDAO;
    private NotificationDAO notificationDAO;
    private BookingAdapter adapter;
    private List<Booking> bookingList;
    private String selectedStatus = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bookings);

        bookingDAO = new BookingDAO(this);
        userDAO = new UserDAO(this);
        notificationDAO = new NotificationDAO(this);

        // Initialize views
        btnBack = findViewById(R.id.btnBack);
        spinnerFilterStatus = findViewById(R.id.spinnerFilterStatus);
        rvBookings = findViewById(R.id.rvBookings);

        // Setup status filter
        setupStatusFilter();

        // Load all bookings initially
        loadBookings("All");

        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Filter spinner
        spinnerFilterStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String status = parent.getItemAtPosition(position).toString();
                loadBookings(status);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupStatusFilter() {
        List<String> statuses = new ArrayList<>();
        statuses.add("All");
        statuses.add("Pending");
        statuses.add("Assigned");
        statuses.add("In Progress");
        statuses.add("Completed");
        statuses.add("Cancelled");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterStatus.setAdapter(adapter);
    }

    private void loadBookings(String status) {
        selectedStatus = status;

        if (status.equals("All")) {
            bookingList = bookingDAO.getAllBookings();
        } else {
            bookingList = bookingDAO.getBookingsByStatus(status);
        }

        adapter = new BookingAdapter(this, bookingList, new BookingAdapter.OnBookingClickListener() {
            @Override
            public void onBookingClick(Booking booking) {
                showBookingOptionsDialog(booking);
            }
        });

        rvBookings.setLayoutManager(new LinearLayoutManager(this));
        rvBookings.setAdapter(adapter);
    }

    private void showBookingOptionsDialog(final Booking booking) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Booking Options");

        String[] options;
        if (booking.getStatus().equals("Pending")) {
            options = new String[]{"Assign Technician", "Update Status", "Cancel Booking"};
        } else {
            options = new String[]{"View Details", "Update Status"};
        }

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (booking.getStatus().equals("Pending")) {
                    switch (which) {
                        case 0:
                            showAssignTechnicianDialog(booking);
                            break;
                        case 1:
                            showUpdateStatusDialog(booking);
                            break;
                        case 2:
                            cancelBooking(booking);
                            break;
                    }
                } else {
                    switch (which) {
                        case 0:
                            showBookingDetails(booking);
                            break;
                        case 1:
                            showUpdateStatusDialog(booking);
                            break;
                    }
                }
            }
        });

        builder.show();
    }

    private void showAssignTechnicianDialog(final Booking booking) {
        final List<User> technicians = userDAO.getAvailableTechnicians();

        if (technicians.isEmpty()) {
            Toast.makeText(this, "No available technicians", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> techNames = new ArrayList<>();
        for (User tech : technicians) {
            techNames.add(tech.getName() + " - " + tech.getSpecialization());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Assign Technician");
        builder.setItems(techNames.toArray(new String[0]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User selectedTech = technicians.get(which);
                booking.setTechnicianId(selectedTech.getId());
                booking.setStatus("Assigned");

                int result = bookingDAO.updateBooking(booking);
                if (result > 0) {
                    // Create notification
                    Notification notification = new Notification(
                            booking.getUserId(),
                            booking.getId(),
                            "Technician " + selectedTech.getName() + " has been assigned to your booking."
                    );
                    notificationDAO.addNotification(notification);

                    Toast.makeText(AdminBookingsActivity.this,
                            "Technician assigned successfully", Toast.LENGTH_SHORT).show();
                    loadBookings(selectedStatus);
                } else {
                    Toast.makeText(AdminBookingsActivity.this,
                            "Failed to assign technician", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.show();
    }

    private void showUpdateStatusDialog(final Booking booking) {
        List<String> statuses = new ArrayList<>();
        statuses.add("Pending");
        statuses.add("Assigned");
        statuses.add("In Progress");
        statuses.add("Completed");
        statuses.add("Cancelled");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Status");
        builder.setItems(statuses.toArray(new String[0]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newStatus = statuses.get(which);
                booking.setStatus(newStatus);

                int result = bookingDAO.updateBooking(booking);
                if (result > 0) {
                    // Create notification
                    Notification notification = new Notification(
                            booking.getUserId(),
                            booking.getId(),
                            "Your booking status has been updated to: " + newStatus
                    );
                    notificationDAO.addNotification(notification);

                    Toast.makeText(AdminBookingsActivity.this,
                            "Status updated successfully", Toast.LENGTH_SHORT).show();
                    loadBookings(selectedStatus);
                } else {
                    Toast.makeText(AdminBookingsActivity.this,
                            "Failed to update status", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.show();
    }

    private void cancelBooking(final Booking booking) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Booking");
        builder.setMessage("Are you sure you want to cancel this booking?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                booking.setStatus("Cancelled");
                int result = bookingDAO.updateBooking(booking);

                if (result > 0) {
                    // Create notification
                    Notification notification = new Notification(
                            booking.getUserId(),
                            booking.getId(),
                            "Your booking has been cancelled by admin."
                    );
                    notificationDAO.addNotification(notification);

                    Toast.makeText(AdminBookingsActivity.this,
                            "Booking cancelled", Toast.LENGTH_SHORT).show();
                    loadBookings(selectedStatus);
                } else {
                    Toast.makeText(AdminBookingsActivity.this,
                            "Failed to cancel booking", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void showBookingDetails(Booking booking) {
        User customer = userDAO.getUserById(booking.getUserId());
        String techName = "Not assigned";
        if (booking.getTechnicianId() > 0) {
            User tech = userDAO.getUserById(booking.getTechnicianId());
            if (tech != null) {
                techName = tech.getName();
            }
        }

        String details = "Booking ID: " + booking.getId() +
                "\nCustomer: " + (customer != null ? customer.getName() : "Unknown") +
                "\nService: " + booking.getServiceName() +
                "\nDate: " + booking.getBookingDate() +
                "\nTime: " + booking.getBookingTime() +
                "\nStatus: " + booking.getStatus() +
                "\nTechnician: " + techName +
                "\nPrice: Rs" + booking.getPrice();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Booking Details");
        builder.setMessage(details);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
