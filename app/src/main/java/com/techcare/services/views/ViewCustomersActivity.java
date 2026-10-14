package com.techcare.services.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.techcare.services.R;
import com.techcare.services.adapters.CustomerAdapter;
import com.techcare.services.database.UserDAO;
import com.techcare.services.models.User;
import java.util.List;

public class ViewCustomersActivity extends Activity {

    private Button btnBack;
    private RecyclerView rvCustomers;
    private UserDAO userDAO;
    private CustomerAdapter customerAdapter;
    private List<User> customerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customers);

        userDAO = new UserDAO(this);

        btnBack = findViewById(R.id.btnBack);
        rvCustomers = findViewById(R.id.rvCustomers);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadCustomers();
    }

    private void loadCustomers() {
        customerList = userDAO.getAllCustomers();

        customerAdapter = new CustomerAdapter(this, customerList);
        rvCustomers.setLayoutManager(new LinearLayoutManager(this));
        rvCustomers.setAdapter(customerAdapter);
    }
}
