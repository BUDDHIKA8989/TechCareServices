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
import com.techcare.services.models.User;
import java.util.List;
import java.util.Map;

public class AdminCustomerAdapter extends RecyclerView.Adapter<AdminCustomerAdapter.CustomerViewHolder> {

    private Context context;
    private List<User> customerList;
    private Map<Integer, Integer> bookingCounts;
    private OnCustomerClickListener listener;

    public interface OnCustomerClickListener {
        void onViewBookings(User customer);
    }

    public AdminCustomerAdapter(Context context, List<User> customerList,
                                Map<Integer, Integer> bookingCounts,
                                OnCustomerClickListener listener) {
        this.context = context;
        this.customerList = customerList;
        this.bookingCounts = bookingCounts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        User customer = customerList.get(position);

        holder.tvCustomerName.setText(customer.getName());
        holder.tvCustomerEmail.setText("✉ " + customer.getEmail());
        holder.tvCustomerPhone.setText("📱 " + customer.getPhone());

        // Get booking count for this customer
        int bookingCount = 0;
        if (bookingCounts != null && bookingCounts.containsKey(customer.getId())) {
            bookingCount = bookingCounts.get(customer.getId());
        }
        holder.tvCustomerBookings.setText("📋 Total Bookings: " + bookingCount);

        holder.btnViewBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onViewBookings(customer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName, tvCustomerEmail, tvCustomerPhone, tvCustomerBookings;
        Button btnViewBookings;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvCustomerEmail = itemView.findViewById(R.id.tvCustomerEmail);
            tvCustomerPhone = itemView.findViewById(R.id.tvCustomerPhone);
            tvCustomerBookings = itemView.findViewById(R.id.tvCustomerBookings);
            btnViewBookings = itemView.findViewById(R.id.btnViewBookings);
        }
    }
}
