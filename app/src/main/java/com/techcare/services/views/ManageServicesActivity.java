package com.techcare.services.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.techcare.services.R;
import com.techcare.services.adapters.ServiceAdapter;
import com.techcare.services.database.ServiceDAO;
import com.techcare.services.models.Service;
import java.util.List;

public class ManageServicesActivity extends Activity {

    private Button btnBack;
    private RecyclerView rvServices;
    private ServiceDAO serviceDAO;
    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_services);

        serviceDAO = new ServiceDAO(this);

        btnBack = findViewById(R.id.btnBack);
        rvServices = findViewById(R.id.rvServices);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadServices();
    }

    private void loadServices() {
        serviceList = serviceDAO.getAllServices();

        serviceAdapter = new ServiceAdapter(this, serviceList, new ServiceAdapter.OnServiceClickListener() {
            @Override
            public void onServiceClick(Service service) {
                // For now, just show service details
                // You can add edit/delete functionality later
            }
        });

        rvServices.setLayoutManager(new LinearLayoutManager(this));
        rvServices.setAdapter(serviceAdapter);
    }
}
