package com.techcare.services.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.techcare.services.R;
import com.techcare.services.models.Technician;
import java.util.List;

public class TechnicianAdapter extends RecyclerView.Adapter<TechnicianAdapter.TechnicianViewHolder> {

    private Context context;
    private List<Technician> technicianList;

    public TechnicianAdapter(Context context, List<Technician> technicianList) {
        this.context = context;
        this.technicianList = technicianList;
    }

    @NonNull
    @Override
    public TechnicianViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_technician, parent, false);
        return new TechnicianViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TechnicianViewHolder holder, int position) {
        Technician technician = technicianList.get(position);

        holder.tvTechnicianName.setText(technician.getName());
        holder.tvTechnicianPhone.setText("📞 " + technician.getPhone());
        holder.tvTechnicianSpecialization.setText("Specialization: " + technician.getSpecialization());

        String status = "Status: " + technician.getStatus();
        holder.tvTechnicianStatus.setText(status);

        if (technician.getStatus().equals("available")) {
            holder.tvTechnicianStatus.setTextColor(0xFF4CAF50);
        } else {
            holder.tvTechnicianStatus.setTextColor(0xFFFF9800);
        }
    }

    @Override
    public int getItemCount() {
        return technicianList.size();
    }

    static class TechnicianViewHolder extends RecyclerView.ViewHolder {
        TextView tvTechnicianName, tvTechnicianPhone, tvTechnicianSpecialization, tvTechnicianStatus;

        public TechnicianViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTechnicianName = itemView.findViewById(R.id.tvTechnicianName);
            tvTechnicianPhone = itemView.findViewById(R.id.tvTechnicianPhone);
            tvTechnicianSpecialization = itemView.findViewById(R.id.tvTechnicianSpecialization);
            tvTechnicianStatus = itemView.findViewById(R.id.tvTechnicianStatus);
        }
    }
}
