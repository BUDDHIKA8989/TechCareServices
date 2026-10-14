package com.techcare.services.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.techcare.services.R;
import com.techcare.services.database.ServiceDAO;
import com.techcare.services.database.TechnicianDAO;
import com.techcare.services.models.Booking;
import com.techcare.services.models.Service;
import com.techcare.services.models.Technician;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private Context context;
    private List<Booking> bookingList;
    private OnBookingClickListener listener;
    private ServiceDAO serviceDAO;
    private TechnicianDAO technicianDAO;

    public interface OnBookingClickListener {
        void onBookingClick(Booking booking);
    }

    public BookingAdapter(Context context, List<Booking> bookingList, OnBookingClickListener listener) {
        this.context = context;
        this.bookingList = bookingList;
        this.listener = listener;
        this.serviceDAO = new ServiceDAO(context);
        this.technicianDAO = new TechnicianDAO(context);
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.tvBookingId.setText("Booking #" + booking.getId());
        holder.tvBookingStatus.setText(booking.getStatus());

        // Set status color
        int statusColor = getStatusColor(booking.getStatus());
        holder.tvBookingStatus.setBackgroundColor(statusColor);

        // Get service name
        Service service = serviceDAO.getServiceById(booking.getServiceId());
        if (service != null) {
            holder.tvServiceName.setText(service.getServiceName());
        }

        // Set booking date and time
        holder.tvBookingDate.setText("📅 " + booking.getBookingDate() + " • 🕒 " + booking.getPickupTime());

        // Show technician info if assigned
        if (booking.getTechnicianId() > 0) {
            Technician technician = technicianDAO.getTechnicianById(booking.getTechnicianId());
            if (technician != null) {
                holder.tvTechnicianInfo.setVisibility(View.VISIBLE);
                holder.tvTechnicianInfo.setText("👨‍🔧 Technician: " + technician.getName());
            }
        } else {
            holder.tvTechnicianInfo.setVisibility(View.GONE);
        }

        holder.btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBookingClick(booking);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    private int getStatusColor(String status) {
        switch (status) {
            case "Received":
                return 0xFFFF9800; // Orange
            case "Under Repair":
                return 0xFF2196F3; // Blue
            case "Ready for Pickup":
                return 0xFF4CAF50; // Green
            case "Completed":
                return 0xFF9E9E9E; // Gray
            default:
                return 0xFFFF9800;
        }
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookingId, tvBookingStatus, tvServiceName, tvBookingDate, tvTechnicianInfo;
        Button btnViewDetails;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingId = itemView.findViewById(R.id.tvBookingId);
            tvBookingStatus = itemView.findViewById(R.id.tvBookingStatus);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvTechnicianInfo = itemView.findViewById(R.id.tvTechnicianInfo);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }
    }
}
