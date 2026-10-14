package com.techcare.services.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.techcare.services.R;
import com.techcare.services.adapters.AdminServiceAdapter;
import com.techcare.services.database.DeviceDAO;
import com.techcare.services.database.ServiceDAO;
import com.techcare.services.models.Device;
import com.techcare.services.models.Service;
import java.util.ArrayList;
import java.util.List;

public class AdminServicesActivity extends Activity {

    private Button btnBack, btnAddService;
    private Spinner spinnerFilterDevice;
    private RecyclerView rvServices;
    private ServiceDAO serviceDAO;
    private DeviceDAO deviceDAO;
    private AdminServiceAdapter adapter;
    private List<Service> serviceList;
    private List<Device> deviceList;
    private int selectedDeviceId = 0; // 0 = All devices

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_services);

        serviceDAO = new ServiceDAO(this);
        deviceDAO = new DeviceDAO(this);

        // Initialize views
        btnBack = findViewById(R.id.btnBack);
        btnAddService = findViewById(R.id.btnAddService);
        spinnerFilterDevice = findViewById(R.id.spinnerFilterDevice);
        rvServices = findViewById(R.id.rvServices);

        // Load devices for filter
        loadDeviceFilter();

        // Load all services initially
        loadServices(0);

        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Add service button
        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminServicesActivity.this, "Opening add service dialog", Toast.LENGTH_SHORT).show();
                showAddEditServiceDialog(null);
            }
        });



        // Filter spinner
        spinnerFilterDevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    loadServices(0); // All devices
                } else {
                    Device device = deviceList.get(position - 1);
                    loadServices(device.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadDeviceFilter() {
        deviceList = deviceDAO.getAllDevices();

        List<String> deviceNames = new ArrayList<>();
        deviceNames.add("All Devices");

        for (Device device : deviceList) {
            deviceNames.add(device.getDeviceName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, deviceNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterDevice.setAdapter(adapter);
    }

    private void loadServices(int deviceId) {
        selectedDeviceId = deviceId;

        if (deviceId == 0) {
            serviceList = serviceDAO.getAllServices();
        } else {
            serviceList = serviceDAO.getServicesByDeviceId(deviceId);
        }

        adapter = new AdminServiceAdapter(this, serviceList,
                new AdminServiceAdapter.OnServiceActionListener() {
                    @Override
                    public void onEditService(Service service) {
                        showAddEditServiceDialog(service);
                    }

                    @Override
                    public void onDeleteService(Service service) {
                        showDeleteConfirmDialog(service);
                    }
                });

        rvServices.setLayoutManager(new LinearLayoutManager(this));
        rvServices.setAdapter(adapter);
    }

    private void showAddEditServiceDialog(final Service service) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (service == null) {
            builder.setTitle("Add New Service");
        } else {
            builder.setTitle("Edit Service");
        }

        // Inflate custom dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_edit_service, null);
        builder.setView(dialogView);

        // Get dialog elements
        final EditText etServiceName = dialogView.findViewById(R.id.etServiceName);
        final Spinner spinnerDevice = dialogView.findViewById(R.id.spinnerDevice);
        final EditText etServiceDescription = dialogView.findViewById(R.id.etServiceDescription);
        final EditText etServicePrice = dialogView.findViewById(R.id.etServicePrice);
        final EditText etServiceDuration = dialogView.findViewById(R.id.etServiceDuration);

        // Setup device spinner
        List<String> deviceNames = new ArrayList<>();
        for (Device device : deviceList) {
            deviceNames.add(device.getDeviceName());
        }

        ArrayAdapter<String> deviceAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, deviceNames);
        deviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDevice.setAdapter(deviceAdapter);

        // If editing, populate fields
        if (service != null) {
            etServiceName.setText(service.getServiceName());
            etServiceDescription.setText(service.getDescription());
            etServicePrice.setText(String.valueOf(service.getPrice()));
            etServiceDuration.setText(service.getDurationEstimate());

            // Set spinner selection
            for (int i = 0; i < deviceList.size(); i++) {
                if (deviceList.get(i).getId() == service.getDeviceId()) {
                    spinnerDevice.setSelection(i);
                    break;
                }
            }
        }

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etServiceName.getText().toString().trim();
                String description = etServiceDescription.getText().toString().trim();
                String priceStr = etServicePrice.getText().toString().trim();
                String duration = etServiceDuration.getText().toString().trim();

                if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty() || duration.isEmpty()) {
                    Toast.makeText(AdminServicesActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double price = Double.parseDouble(priceStr);
                int devicePosition = spinnerDevice.getSelectedItemPosition();
                int deviceId = deviceList.get(devicePosition).getId();

                if (service == null) {
                    // Add new service
                    Service newService = new Service();
                    newService.setDeviceId(deviceId);
                    newService.setServiceName(name);
                    newService.setDescription(description);
                    newService.setPrice(price);
                    newService.setDurationEstimate(duration);

                    long result = serviceDAO.addService(newService);
                    if (result > 0) {
                        Toast.makeText(AdminServicesActivity.this, "Service added successfully", Toast.LENGTH_SHORT).show();
                        loadServices(selectedDeviceId);
                    } else {
                        Toast.makeText(AdminServicesActivity.this, "Failed to add service", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Update existing service
                    service.setDeviceId(deviceId);
                    service.setServiceName(name);
                    service.setDescription(description);
                    service.setPrice(price);
                    service.setDurationEstimate(duration);

                    int result = serviceDAO.updateService(service);
                    if (result > 0) {
                        Toast.makeText(AdminServicesActivity.this, "Service updated successfully", Toast.LENGTH_SHORT).show();
                        loadServices(selectedDeviceId);
                    } else {
                        Toast.makeText(AdminServicesActivity.this, "Failed to update service", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDeleteConfirmDialog(final Service service) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Service");
        builder.setMessage("Are you sure you want to delete '" + service.getServiceName() + "'?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int result = serviceDAO.deleteService(service.getId());
                if (result > 0) {
                    Toast.makeText(AdminServicesActivity.this, "Service deleted successfully", Toast.LENGTH_SHORT).show();
                    loadServices(selectedDeviceId);
                } else {
                    Toast.makeText(AdminServicesActivity.this, "Failed to delete service", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
