package com.techcare.services.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.techcare.services.R;
import com.techcare.services.adapters.AdminCustomerAdapter;
import com.techcare.services.database.BookingDAO;
import com.techcare.services.database.UserDAO;
import com.techcare.services.models.Booking;
import com.techcare.services.models.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminCustomersActivity extends Activity {

    private Button btnBack;
    private TextView tvCustomerCount;
    private RecyclerView rvCustomers;
    private UserDAO userDAO;
    private BookingDAO bookingDAO;
    private AdminCustomerAdapter adapter;
    private List<User> customerList;
    private Map<Integer, Integer> bookingCounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_customers);

        userDAO = new UserDAO(this);
        bookingDAO = new BookingDAO(this);

        // Initialize views
        btnBack = findViewById(R.id.btnBack);
        tvCustomerCount = findViewById(R.id.tvCustomerCount);
        rvCustomers = findViewById(R.id.rvCustomers);

        // Load customers
        loadCustomers();

        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadCustomers() {
        customerList = userDAO.getAllCustomers();
        tvCustomerCount.setText(String.valueOf(customerList.size()));

        // Calculate booking counts for each customer
        bookingCounts = new HashMap<>();
        for (User customer : customerList) {
            List<Booking> bookings = bookingDAO.getBookingsByUserId(customer.getId());
            bookingCounts.put(customer.getId(), bookings.size());
        }

        adapter = new AdminCustomerAdapter(this, customerList, bookingCounts,
                new AdminCustomerAdapter.OnCustomerClickListener() {
                    @Override
                    public void onViewBookings(User customer) {
                        showCustomerBookings(customer);
                    }
                });

        rvCustomers.setLayoutManager(new LinearLayoutManager(this));
        rvCustomers.setAdapter(adapter);
    }

    private void showCustomerBookings(User customer) {
        List<Booking> bookings = bookingDAO.getBookingsByUserId(customer.getId());

        if (bookings.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(customer.getName() + "'s Bookings");
            builder.setMessage("This customer has no bookings yet.");
            builder.setPositiveButton("OK", null);
            builder.show();
            return;
        }

        // Build booking list message
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            message.append((i + 1)).append(". ")
                    .append(booking.getServiceName())
                    .append("\n   Date: ").append(booking.getBookingDate())
                    .append("\n   Status: ").append(booking.getStatus())
                    .append("\n   Price: Rs").append(booking.getPrice())
                    .append("\n\n");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(customer.getName() + "'s Bookings (" + bookings.size() + ")");
        builder.setMessage(message.toString().trim());
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
