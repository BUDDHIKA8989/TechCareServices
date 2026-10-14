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
import com.techcare.services.adapters.AdminTechnicianAdapter;
import com.techcare.services.database.UserDAO;
import com.techcare.services.models.User;
import java.util.ArrayList;
import java.util.List;

public class AdminTechniciansActivity extends Activity {

    private Button btnBack, btnAddTechnician;
    private Spinner spinnerFilterStatus;
    private RecyclerView rvTechnicians;
    private UserDAO userDAO;
    private AdminTechnicianAdapter adapter;
    private List<User> technicianList;
    private String selectedStatus = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_technicians);

        userDAO = new UserDAO(this);

        // Initialize views
        btnBack = findViewById(R.id.btnBack);
        btnAddTechnician = findViewById(R.id.btnAddTechnician);
        spinnerFilterStatus = findViewById(R.id.spinnerFilterStatus);
        rvTechnicians = findViewById(R.id.rvTechnicians);

        // Setup status filter
        setupStatusFilter();

        // Load all technicians initially
        loadTechnicians("All");

        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Add technician button
        btnAddTechnician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEditTechnicianDialog(null);
            }
        });

        // Filter spinner
        spinnerFilterStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String status = parent.getItemAtPosition(position).toString();
                loadTechnicians(status);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupStatusFilter() {
        List<String> statuses = new ArrayList<>();
        statuses.add("All");
        statuses.add("Available");
        statuses.add("Busy");
        statuses.add("Inactive");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterStatus.setAdapter(adapter);
    }

    private void loadTechnicians(String status) {
        selectedStatus = status;

        if (status.equals("All")) {
            technicianList = userDAO.getAllTechnicians();
        } else {
            technicianList = userDAO.getTechniciansByStatus(status);
        }

        adapter = new AdminTechnicianAdapter(this, technicianList,
                new AdminTechnicianAdapter.OnTechnicianActionListener() {
                    @Override
                    public void onEditTechnician(User technician) {
                        showAddEditTechnicianDialog(technician);
                    }

                    @Override
                    public void onDeleteTechnician(User technician) {
                        showDeleteConfirmDialog(technician);
                    }
                });

        rvTechnicians.setLayoutManager(new LinearLayoutManager(this));
        rvTechnicians.setAdapter(adapter);
    }

    private void showAddEditTechnicianDialog(final User technician) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (technician == null) {
            builder.setTitle("Add New Technician");
        } else {
            builder.setTitle("Edit Technician");
        }

        // Inflate custom dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_edit_technician, null);
        builder.setView(dialogView);

        // Get dialog elements
        final EditText etTechName = dialogView.findViewById(R.id.etTechName);
        final EditText etTechEmail = dialogView.findViewById(R.id.etTechEmail);
        final EditText etTechPhone = dialogView.findViewById(R.id.etTechPhone);
        final EditText etTechPassword = dialogView.findViewById(R.id.etTechPassword);
        final EditText etTechSpecialization = dialogView.findViewById(R.id.etTechSpecialization);
        final Spinner spinnerStatus = dialogView.findViewById(R.id.spinnerStatus);

        // Setup status spinner
        List<String> statuses = new ArrayList<>();
        statuses.add("Available");
        statuses.add("Busy");
        statuses.add("Inactive");

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statuses);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        // If editing, populate fields
        if (technician != null) {
            etTechName.setText(technician.getName());
            etTechEmail.setText(technician.getEmail());
            etTechPhone.setText(technician.getPhone());
            etTechPassword.setText(technician.getPassword());
            etTechSpecialization.setText(technician.getSpecialization());

            // Set status spinner selection
            for (int i = 0; i < statuses.size(); i++) {
                if (statuses.get(i).equals(technician.getStatus())) {
                    spinnerStatus.setSelection(i);
                    break;
                }
            }
        }

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etTechName.getText().toString().trim();
                String email = etTechEmail.getText().toString().trim();
                String phone = etTechPhone.getText().toString().trim();
                String password = etTechPassword.getText().toString().trim();
                String specialization = etTechSpecialization.getText().toString().trim();
                String status = spinnerStatus.getSelectedItem().toString();

                // Validation
                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                        password.isEmpty() || specialization.isEmpty()) {
                    Toast.makeText(AdminTechniciansActivity.this,
                            "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (technician == null) {
                    // Check if email already exists
                    if (userDAO.getUserByEmail(email) != null) {
                        Toast.makeText(AdminTechniciansActivity.this,
                                "Email already exists", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Add new technician
                    User newTechnician = new User();
                    newTechnician.setName(name);
                    newTechnician.setEmail(email);
                    newTechnician.setPhone(phone);
                    newTechnician.setPassword(password);
                    newTechnician.setRole("technician");
                    newTechnician.setSpecialization(specialization);
                    newTechnician.setStatus(status);

                    long result = userDAO.addTechnician(newTechnician);
                    if (result > 0) {
                        Toast.makeText(AdminTechniciansActivity.this,
                                "Technician added successfully", Toast.LENGTH_SHORT).show();
                        loadTechnicians(selectedStatus);
                    } else {
                        Toast.makeText(AdminTechniciansActivity.this,
                                "Failed to add technician", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Update existing technician
                    technician.setName(name);
                    technician.setEmail(email);
                    technician.setPhone(phone);
                    technician.setPassword(password);
                    technician.setSpecialization(specialization);
                    technician.setStatus(status);

                    int result = userDAO.updateTechnician(technician);
                    if (result > 0) {
                        Toast.makeText(AdminTechniciansActivity.this,
                                "Technician updated successfully", Toast.LENGTH_SHORT).show();
                        loadTechnicians(selectedStatus);
                    } else {
                        Toast.makeText(AdminTechniciansActivity.this,
                                "Failed to update technician", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDeleteConfirmDialog(final User technician) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Technician");
        builder.setMessage("Are you sure you want to delete '" + technician.getName() + "'?\n\n" +
                "This will also remove all associated bookings.");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int result = userDAO.deleteTechnician(technician.getId());
                if (result > 0) {
                    Toast.makeText(AdminTechniciansActivity.this,
                            "Technician deleted successfully", Toast.LENGTH_SHORT).show();
                    loadTechnicians(selectedStatus);
                } else {
                    Toast.makeText(AdminTechniciansActivity.this,
                            "Failed to delete technician", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
