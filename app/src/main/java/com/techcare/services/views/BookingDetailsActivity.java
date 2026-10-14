package com.techcare.services.views;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import com.techcare.services.R;
import com.techcare.services.database.BookingDAO;
import com.techcare.services.database.DeviceDAO;
import com.techcare.services.database.ServiceDAO;
import com.techcare.services.database.TechnicianDAO;
import com.techcare.services.models.Booking;
import com.techcare.services.models.Device;
import com.techcare.services.models.Service;
import com.techcare.services.models.Technician;
import com.techcare.services.utils.ImageUtils;

public class BookingDetailsActivity extends Activity {

    private Button btnBack;
    private TextView tvBookingId, tvStatus, tvServiceName, tvDeviceName, tvServicePrice;
    private TextView tvIssueDescription, tvServiceMethod, tvBookingDate, tvBookingTime, tvAddress;
    private CardView cardTechnician, cardImage;
    private TextView tvTechnicianName, tvTechnicianPhone, tvEstimatedCompletion;
    private ImageView ivBookingImage;
    private TextView tvStatusReceived, tvStatusUnderRepair, tvStatusReady, tvStatusCompleted;

    private BookingDAO bookingDAO;
    private ServiceDAO serviceDAO;
    private DeviceDAO deviceDAO;
    private TechnicianDAO technicianDAO;
    private int bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        // Initialize DAOs
        bookingDAO = new BookingDAO(this);
        serviceDAO = new ServiceDAO(this);
        deviceDAO = new DeviceDAO(this);
        technicianDAO = new TechnicianDAO(this);

        // Get booking ID from intent
        bookingId = getIntent().getIntExtra("booking_id", -1);

        // Initialize views
        initializeViews();

        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Load booking details
        loadBookingDetails();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        tvBookingId = findViewById(R.id.tvBookingId);
        tvStatus = findViewById(R.id.tvStatus);
        tvServiceName = findViewById(R.id.tvServiceName);
        tvDeviceName = findViewById(R.id.tvDeviceName);
        tvServicePrice = findViewById(R.id.tvServicePrice);
        tvIssueDescription = findViewById(R.id.tvIssueDescription);
        tvServiceMethod = findViewById(R.id.tvServiceMethod);
        tvBookingDate = findViewById(R.id.tvBookingDate);
        tvBookingTime = findViewById(R.id.tvBookingTime);
        tvAddress = findViewById(R.id.tvAddress);
        cardTechnician = findViewById(R.id.cardTechnician);
        cardImage = findViewById(R.id.cardImage);
        ivBookingImage = findViewById(R.id.ivBookingImage);
        tvTechnicianName = findViewById(R.id.tvTechnicianName);
        tvTechnicianPhone = findViewById(R.id.tvTechnicianPhone);
        tvEstimatedCompletion = findViewById(R.id.tvEstimatedCompletion);
        tvStatusReceived = findViewById(R.id.tvStatusReceived);
        tvStatusUnderRepair = findViewById(R.id.tvStatusUnderRepair);
        tvStatusReady = findViewById(R.id.tvStatusReady);
        tvStatusCompleted = findViewById(R.id.tvStatusCompleted);
    }

    private void loadBookingDetails() {
        Booking booking = bookingDAO.getBookingById(bookingId);
        if (booking == null) {
            finish();
            return;
        }

        // Set basic info
        tvBookingId.setText("Booking #" + booking.getId());
        tvStatus.setText("Status: " + booking.getStatus());
        tvIssueDescription.setText(booking.getIssueDescription());
        tvServiceMethod.setText("Service Method: " +
                (booking.getServiceMethod().equals("pickup") ? "Home Pickup" : "Service Center Drop-off"));
        tvBookingDate.setText("Date: " + booking.getBookingDate());
        tvBookingTime.setText("Time: " + booking.getPickupTime());
        tvAddress.setText("Address: " + booking.getAddress());

        // Load and display image if exists
        if (booking.getImagePath() != null && !booking.getImagePath().isEmpty()) {
            Bitmap bitmap = ImageUtils.loadImageFromStorage(booking.getImagePath());
            if (bitmap != null) {
                cardImage.setVisibility(View.VISIBLE);
                ivBookingImage.setImageBitmap(bitmap);
            }
        }

        // Get and display service info
        Service service = serviceDAO.getServiceById(booking.getServiceId());
        if (service != null) {
            tvServiceName.setText(service.getServiceName());
            tvServicePrice.setText("Cost: Rs" + String.format("%.0f", service.getPrice()));

            // Get device info
            Device device = deviceDAO.getDeviceById(service.getDeviceId());
            if (device != null) {
                tvDeviceName.setText("Device: " + device.getDeviceName());
            }
        }

        // Display technician info if assigned
        if (booking.getTechnicianId() > 0) {
            cardTechnician.setVisibility(View.VISIBLE);
            Technician technician = technicianDAO.getTechnicianById(booking.getTechnicianId());
            if (technician != null) {
                tvTechnicianName.setText("👨‍🔧 " + technician.getName());
                tvTechnicianPhone.setText("📞 " + technician.getPhone());
            }
            if (!booking.getEstimatedCompletion().isEmpty()) {
                tvEstimatedCompletion.setText("Estimated Completion: " + booking.getEstimatedCompletion());
            }
        } else {
            cardTechnician.setVisibility(View.GONE);
        }

        // Update status progress
        updateStatusProgress(booking.getStatus());
    }

    private void updateStatusProgress(String status) {
        // Reset all
        tvStatusReceived.setTextColor(0xFF999999);
        tvStatusUnderRepair.setTextColor(0xFF999999);
        tvStatusReady.setTextColor(0xFF999999);
        tvStatusCompleted.setTextColor(0xFF999999);

        tvStatusReceived.setText("○ Request Received");
        tvStatusUnderRepair.setText("○ Under Repair");
        tvStatusReady.setText("○ Ready for Pickup");
        tvStatusCompleted.setText("○ Completed");

        // Update based on current status
        switch (status) {
            case "Received":
                tvStatusReceived.setText("✓ Request Received");
                tvStatusReceived.setTextColor(0xFF4CAF50);
                break;
            case "Under Repair":
                tvStatusReceived.setText("✓ Request Received");
                tvStatusReceived.setTextColor(0xFF4CAF50);
                tvStatusUnderRepair.setText("✓ Under Repair");
                tvStatusUnderRepair.setTextColor(0xFF2196F3);
                break;
            case "Ready for Pickup":
                tvStatusReceived.setText("✓ Request Received");
                tvStatusReceived.setTextColor(0xFF4CAF50);
                tvStatusUnderRepair.setText("✓ Under Repair");
                tvStatusUnderRepair.setTextColor(0xFF4CAF50);
                tvStatusReady.setText("✓ Ready for Pickup");
                tvStatusReady.setTextColor(0xFF4CAF50);
                break;
            case "Completed":
                tvStatusReceived.setText("✓ Request Received");
                tvStatusReceived.setTextColor(0xFF4CAF50);
                tvStatusUnderRepair.setText("✓ Under Repair");
                tvStatusUnderRepair.setTextColor(0xFF4CAF50);
                tvStatusReady.setText("✓ Ready for Pickup");
                tvStatusReady.setTextColor(0xFF4CAF50);
                tvStatusCompleted.setText("✓ Completed");
                tvStatusCompleted.setTextColor(0xFF4CAF50);
                break;
        }
    }
}
