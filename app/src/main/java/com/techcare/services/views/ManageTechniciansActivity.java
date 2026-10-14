package com.techcare.services.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.techcare.services.R;
import com.techcare.services.adapters.TechnicianAdapter;
import com.techcare.services.database.TechnicianDAO;
import com.techcare.services.models.Technician;
import java.util.List;

public class ManageTechniciansActivity extends Activity {

    private Button btnBack;
    private RecyclerView rvTechnicians;
    private TechnicianDAO technicianDAO;
    private TechnicianAdapter technicianAdapter;
    private List<Technician> technicianList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_technicians);

        technicianDAO = new TechnicianDAO(this);

        btnBack = findViewById(R.id.btnBack);
        rvTechnicians = findViewById(R.id.rvTechnicians);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadTechnicians();
    }

    private void loadTechnicians() {
        technicianList = technicianDAO.getAllTechnicians();

        technicianAdapter = new TechnicianAdapter(this, technicianList);
        rvTechnicians.setLayoutManager(new LinearLayoutManager(this));
        rvTechnicians.setAdapter(technicianAdapter);
    }
}
