package com.techcare.services.views;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import com.techcare.services.R;
import com.techcare.services.database.BookingDAO;
import com.techcare.services.database.DeviceDAO;
import com.techcare.services.database.NotificationDAO;
import com.techcare.services.database.ServiceDAO;
import com.techcare.services.database.TechnicianDAO;
import com.techcare.services.database.UserDAO;
import com.techcare.services.models.Booking;
import com.techcare.services.models.Device;
import com.techcare.services.models.Notification;
import com.techcare.services.models.Service;
import com.techcare.services.models.Technician;
import com.techcare.services.models.User;
import com.techcare.services.utils.ImageUtils;
import java.util.ArrayList;
import java.util.List;

public class AdminBookingDetailsActivity extends Activity {

    private Button btnBack, btnAssignTechnician, btnUpdateStatus;
    private TextView tvBookingId, tvStatus, tvCustomerName, tvCustomerPhone;
    private TextView tvServiceName, tvDeviceName, tvIssueDescription, tvServiceMethod, tvAddress, tvBookingDateTime;
    private Spinner spinnerTechnician, spinnerStatus;
    private EditText etEstimatedCompletion;
    private CardView cardImage;
    private ImageView ivBookingImage;

    private BookingDAO bookingDAO;
    private UserDAO userDAO;
    private ServiceDAO serviceDAO;
    private DeviceDAO deviceDAO;
    private TechnicianDAO technicianDAO;
    private NotificationDAO notificationDAO;

    private int bookingId;
    private Booking currentBooking;
    private List<Technician> technicianList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_booking_details);

        // Initialize DAOs
        bookingDAO = new BookingDAO(this);
        userDAO = new UserDAO(this);
        serviceDAO = new ServiceDAO(this);
        deviceDAO = new DeviceDAO(this);
        technicianDAO = new TechnicianDAO(this);
        notificationDAO = new NotificationDAO(this);

        // Get booking ID
        bookingId = getIntent().getIntExtra("booking_id", -1);

        // Initialize views
        initializeViews();

        // Load data
        loadBookingDetails();
        loadTechnicians();
        setupStatusSpinner();

        // Button listeners
        setupListeners();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnAssignTechnician = findViewById(R.id.btnAssignTechnician);
        btnUpdateStatus = findViewById(R.id.btnUpdateStatus);
        tvBookingId = findViewById(R.id.tvBookingId);
        tvStatus = findViewById(R.id.tvStatus);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        tvServiceName = findViewById(R.id.tvServiceName);
        tvDeviceName = findViewById(R.id.tvDeviceName);
        tvIssueDescription = findViewById(R.id.tvIssueDescription);
        tvServiceMethod = findViewById(R.id.tvServiceMethod);
        tvAddress = findViewById(R.id.tvAddress);
        tvBookingDateTime = findViewById(R.id.tvBookingDateTime);
        spinnerTechnician = findViewById(R.id.spinnerTechnician);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        etEstimatedCompletion = findViewById(R.id.etEstimatedCompletion);
        cardImage = findViewById(R.id.cardImage);
        ivBookingImage = findViewById(R.id.ivBookingImage);
    }

    private void loadBookingDetails() {
        currentBooking = bookingDAO.getBookingById(bookingId);
        if (currentBooking == null) {
            finish();
            return;
        }

        // Set booking info
        tvBookingId.setText("Booking #" + currentBooking.getId());
        tvStatus.setText("Status: " + currentBooking.getStatus());
        tvIssueDescription.setText("Issue: " + currentBooking.getIssueDescription());
        tvServiceMethod.setText("Method: " +
                (currentBooking.getServiceMethod().equals("pickup") ? "Home Pickup" : "Service Center Drop-off"));
        tvAddress.setText("Address: " + currentBooking.getAddress());
        tvBookingDateTime.setText("📅 " + currentBooking.getBookingDate() + " • 🕒 " + currentBooking.getPickupTime());

        // Load customer info
        User customer = userDAO.getUserById(currentBooking.getUserId());
        if (customer != null) {
            tvCustomerName.setText("Name: " + customer.getName());
            tvCustomerPhone.setText("📞 " + customer.getPhone());
        }

        // Load service info
        Service service = serviceDAO.getServiceById(currentBooking.getServiceId());
        if (service != null) {
            tvServiceName.setText(service.getServiceName());

            Device device = deviceDAO.getDeviceById(service.getDeviceId());
            if (device != null) {
                tvDeviceName.setText("Device: " + device.getDeviceName());
            }
        }

        // Load image
        if (currentBooking.getImagePath() != null && !currentBooking.getImagePath().isEmpty()) {
            Bitmap bitmap = ImageUtils.loadImageFromStorage(currentBooking.getImagePath());
            if (bitmap != null) {
                cardImage.setVisibility(View.VISIBLE);
                ivBookingImage.setImageBitmap(bitmap);
            }
        }

        // Set estimated completion if exists
        if (!currentBooking.getEstimatedCompletion().isEmpty()) {
            etEstimatedCompletion.setText(currentBooking.getEstimatedCompletion());
        }
    }

    private void loadTechnicians() {
        technicianList = technicianDAO.getAvailableTechnicians();

        List<String> technicianNames = new ArrayList<>();
        technicianNames.add("Select Technician");
        for (Technician tech : technicianList) {
            technicianNames.add(tech.getName() + " - " + tech.getSpecialization());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, technicianNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTechnician.setAdapter(adapter);

        // Select current technician if assigned
        if (currentBooking.getTechnicianId() > 0) {
            for (int i = 0; i < technicianList.size(); i++) {
                if (technicianList.get(i).getId() == currentBooking.getTechnicianId()) {
                    spinnerTechnician.setSelection(i + 1);
                    break;
                }
            }
        }
    }

    private void setupStatusSpinner() {
        String[] statuses = {"Received", "Under Repair", "Ready for Pickup", "Completed"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        // Select current status
        for (int i = 0; i < statuses.length; i++) {
            if (statuses[i].equals(currentBooking.getStatus())) {
                spinnerStatus.setSelection(i);
                break;
            }
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAssignTechnician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignTechnician();
            }
        });

        btnUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBookingStatus();
            }
        });
    }

    private void assignTechnician() {
        int selectedPosition = spinnerTechnician.getSelectedItemPosition();

        if (selectedPosition == 0) {
            Toast.makeText(this, "Please select a technician", Toast.LENGTH_SHORT).show();
            return;
        }

        Technician selectedTechnician = technicianList.get(selectedPosition - 1);
        currentBooking.setTechnicianId(selectedTechnician.getId());

        int updated = bookingDAO.updateBooking(currentBooking);  // ← CHANGED TO int

        if (updated > 0) {  // ← CHANGED TO > 0
            // Create notification for customer
            Notification notification = new Notification(
                    currentBooking.getUserId(),
                    currentBooking.getId(),
                    "Technician " + selectedTechnician.getName() + " has been assigned to your booking #" + currentBooking.getId()
            );
            notificationDAO.addNotification(notification);

            Toast.makeText(this, "Technician assigned successfully!", Toast.LENGTH_SHORT).show();
            loadBookingDetails();
        } else {
            Toast.makeText(this, "Failed to assign technician", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateBookingStatus() {
        String newStatus = spinnerStatus.getSelectedItem().toString();
        String estimatedCompletion = etEstimatedCompletion.getText().toString().trim();

        currentBooking.setStatus(newStatus);
        currentBooking.setEstimatedCompletion(estimatedCompletion);

        int updated = bookingDAO.updateBooking(currentBooking);  // ← CHANGED TO int

        if (updated > 0) {  // ← CHANGED TO > 0
            // Create notification for customer
            Notification notification = new Notification(
                    currentBooking.getUserId(),
                    currentBooking.getId(),
                    "Your booking #" + currentBooking.getId() + " status updated to: " + newStatus
            );
            notificationDAO.addNotification(notification);

            Toast.makeText(this, "Status updated successfully!", Toast.LENGTH_SHORT).show();
            tvStatus.setText("Status: " + newStatus);
        } else {
            Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show();
        }
    }

}
