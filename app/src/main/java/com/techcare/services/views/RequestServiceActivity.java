package com.techcare.services.views;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.techcare.services.R;
import com.techcare.services.database.BookingDAO;
import com.techcare.services.database.NotificationDAO;
import com.techcare.services.models.Booking;
import com.techcare.services.models.Notification;
import com.techcare.services.utils.ImageUtils;
import com.techcare.services.utils.SessionManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RequestServiceActivity extends Activity {

    private static final int CAMERA_REQUEST = 100;
    private static final int GALLERY_REQUEST = 101;
    private static final int CAMERA_PERMISSION_CODE = 102;
    private static final int STORAGE_PERMISSION_CODE = 103;

    // ... rest of your existing variables ...

    private TextView tvServiceName, tvDeviceName, tvServicePrice, tvImageStatus, tvAddressLabel;
    private EditText etIssueDescription, etAddress;
    private Button btnBack, btnCamera, btnGallery, btnSelectDate, btnSelectTime, btnSubmitRequest;
    private RadioGroup rgServiceMethod;
    private RadioButton rbPickup, rbDropoff;

    private SessionManager sessionManager;
    private BookingDAO bookingDAO;
    private NotificationDAO notificationDAO;

    private int serviceId;
    private String serviceName;
    private double servicePrice;
    private String deviceName;
    private String imagePath = "";
    private String selectedDate = "";
    private String selectedTime = "";
    private Bitmap capturedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_service);

        sessionManager = new SessionManager(this);
        bookingDAO = new BookingDAO(this);
        notificationDAO = new NotificationDAO(this);

        // Get data from intent
        serviceId = getIntent().getIntExtra("service_id", -1);
        serviceName = getIntent().getStringExtra("service_name");
        servicePrice = getIntent().getDoubleExtra("service_price", 0);
        deviceName = getIntent().getStringExtra("device_name");

        // Initialize views
        initializeViews();

        // Set data
        tvServiceName.setText(serviceName);
        tvDeviceName.setText("Device: " + deviceName);
        tvServicePrice.setText("Estimated Cost: Rs" + String.format("%.0f", servicePrice));

        // Button listeners
        setupListeners();
    }

    // ... existing initializeViews() method ...

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        tvServiceName = findViewById(R.id.tvServiceName);
        tvDeviceName = findViewById(R.id.tvDeviceName);
        tvServicePrice = findViewById(R.id.tvServicePrice);
        etIssueDescription = findViewById(R.id.etIssueDescription);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        tvImageStatus = findViewById(R.id.tvImageStatus);
        rgServiceMethod = findViewById(R.id.rgServiceMethod);
        rbPickup = findViewById(R.id.rbPickup);
        rbDropoff = findViewById(R.id.rbDropoff);
        tvAddressLabel = findViewById(R.id.tvAddressLabel);
        etAddress = findViewById(R.id.etAddress);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        btnSubmitRequest = findViewById(R.id.btnSubmitRequest);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermission();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }
        });

        rgServiceMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbPickup) {
                    tvAddressLabel.setVisibility(View.VISIBLE);
                    etAddress.setVisibility(View.VISIBLE);
                } else {
                    tvAddressLabel.setVisibility(View.GONE);
                    etAddress.setVisibility(View.GONE);
                }
            }
        });

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        btnSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRequest();
            }
        });
    }

    // Check camera permission
    private void checkCameraPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    // Check storage permission
    private void checkStoragePermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            openGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Storage permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } else {
            Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST && data != null) {
                capturedImage = (Bitmap) data.getExtras().get("data");
                tvImageStatus.setText("✓ Image captured from camera");
                tvImageStatus.setTextColor(0xFF4CAF50);
            } else if (requestCode == GALLERY_REQUEST && data != null) {
                try {
                    capturedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    tvImageStatus.setText("✓ Image selected from gallery");
                    tvImageStatus.setTextColor(0xFF4CAF50);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // ... rest of your existing methods (showDatePicker, showTimePicker, submitRequest) ...

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedCal = Calendar.getInstance();
                        selectedCal.set(year, month, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        selectedDate = sdf.format(selectedCal.getTime());
                        btnSelectDate.setText("📅 " + selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        btnSelectTime.setText("🕒 " + selectedTime);
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }

    private void submitRequest() {
        String issueDescription = etIssueDescription.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String serviceMethod = rbPickup.isChecked() ? "pickup" : "dropoff";

        // Validation
        if (issueDescription.isEmpty()) {
            etIssueDescription.setError("Please describe the issue");
            etIssueDescription.requestFocus();
            return;
        }

        if (serviceMethod.equals("pickup") && address.isEmpty()) {
            etAddress.setError("Address is required for pickup");
            etAddress.requestFocus();
            return;
        }

        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedTime.isEmpty()) {
            Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save image if captured
        if (capturedImage != null) {
            String fileName = "booking_" + System.currentTimeMillis() + ".jpg";
            Bitmap compressedImage = ImageUtils.compressBitmap(capturedImage, 800, 800);
            imagePath = ImageUtils.saveImageToInternalStorage(this, compressedImage, fileName);
        }

        // Create booking
        Booking booking = new Booking();
        booking.setUserId(sessionManager.getUserId());
        booking.setServiceId(serviceId);
        booking.setIssueDescription(issueDescription);
        booking.setImagePath(imagePath);
        booking.setServiceMethod(serviceMethod);
        booking.setBookingDate(selectedDate);
        booking.setPickupTime(selectedTime);
        booking.setAddress(serviceMethod.equals("pickup") ? address : "Service Center");
        booking.setStatus("Received");
        booking.setTechnicianId(0);
        booking.setEstimatedCompletion("");

        long bookingId = bookingDAO.addBooking(booking);

        if (bookingId > 0) {
            // Create notification
            Notification notification = new Notification(
                    sessionManager.getUserId(),
                    (int) bookingId,
                    "Your repair request has been received. We will assign a technician soon."
            );
            notificationDAO.addNotification(notification);

            Toast.makeText(this, "Request submitted successfully!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to submit request. Try again!", Toast.LENGTH_SHORT).show();
        }
    }
}
