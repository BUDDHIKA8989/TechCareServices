package com.techcare.services.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.techcare.services.R;
import com.techcare.services.models.User;
import java.util.List;

public class AdminTechnicianAdapter extends RecyclerView.Adapter<AdminTechnicianAdapter.TechnicianViewHolder> {

    private Context context;
    private List<User> technicianList;
    private OnTechnicianActionListener listener;

    public interface OnTechnicianActionListener {
        void onEditTechnician(User technician);
        void onDeleteTechnician(User technician);
    }

    public AdminTechnicianAdapter(Context context, List<User> technicianList, OnTechnicianActionListener listener) {
        this.context = context;
        this.technicianList = technicianList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TechnicianViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_technician, parent, false);
        return new TechnicianViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TechnicianViewHolder holder, int position) {
        User technician = technicianList.get(position);

        holder.tvTechName.setText(technician.getName());
        holder.tvTechSpecialization.setText("Specialization: " + technician.getSpecialization());
        holder.tvTechPhone.setText("📱 " + technician.getPhone());
        holder.tvTechEmail.setText("✉ " + technician.getEmail());
        holder.tvTechStatus.setText(technician.getStatus());

        // Set status color
        if (technician.getStatus().equals("Available")) {
            holder.tvTechStatus.setBackgroundColor(Color.parseColor("#4CAF50"));
        } else if (technician.getStatus().equals("Busy")) {
            holder.tvTechStatus.setBackgroundColor(Color.parseColor("#FF9800"));
        } else {
            holder.tvTechStatus.setBackgroundColor(Color.parseColor("#F44336"));
        }

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEditTechnician(technician);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteTechnician(technician);
            }
        });
    }

    @Override
    public int getItemCount() {
        return technicianList.size();
    }

    static class TechnicianViewHolder extends RecyclerView.ViewHolder {
        TextView tvTechName, tvTechSpecialization, tvTechPhone, tvTechEmail, tvTechStatus;
        Button btnEdit, btnDelete;

        public TechnicianViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTechName = itemView.findViewById(R.id.tvTechName);
            tvTechSpecialization = itemView.findViewById(R.id.tvTechSpecialization);
            tvTechPhone = itemView.findViewById(R.id.tvTechPhone);
            tvTechEmail = itemView.findViewById(R.id.tvTechEmail);
            tvTechStatus = itemView.findViewById(R.id.tvTechStatus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
