package com.techcare.services.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.techcare.services.R;
import com.techcare.services.adapters.ServiceAdapter;
import com.techcare.services.database.ServiceDAO;
import com.techcare.services.models.Service;
import java.util.List;

public class DeviceServicesActivity extends Activity {

    private TextView tvDeviceName;
    private Button btnBack;
    private RecyclerView rvServices;
    private ServiceDAO serviceDAO;
    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList;
    private int deviceId;
    private String deviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_services);

        serviceDAO = new ServiceDAO(this);

        // Get device info from intent
        deviceId = getIntent().getIntExtra("device_id", -1);
        deviceName = getIntent().getStringExtra("device_name");

        // Initialize views
        tvDeviceName = findViewById(R.id.tvDeviceName);
        btnBack = findViewById(R.id.btnBack);
        rvServices = findViewById(R.id.rvServices);

        // Set device name
        tvDeviceName.setText(deviceName);

        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Load services
        loadServices();
    }

    private void loadServices() {
        serviceList = serviceDAO.getServicesByDeviceId(deviceId);

        if (serviceList.isEmpty()) {
            Toast.makeText(this, "No services available for this device", Toast.LENGTH_SHORT).show();
            return;
        }

        serviceAdapter = new ServiceAdapter(this, serviceList, new ServiceAdapter.OnServiceClickListener() {
            @Override
            public void onServiceClick(Service service) {
                // Navigate to request service screen
                Intent intent = new Intent(DeviceServicesActivity.this, RequestServiceActivity.class);
                intent.putExtra("service_id", service.getId());
                intent.putExtra("service_name", service.getServiceName());
                intent.putExtra("service_price", service.getPrice());
                intent.putExtra("device_name", deviceName);
                startActivity(intent);
            }
        });

        rvServices.setLayoutManager(new LinearLayoutManager(this));
        rvServices.setAdapter(serviceAdapter);
    }
}
