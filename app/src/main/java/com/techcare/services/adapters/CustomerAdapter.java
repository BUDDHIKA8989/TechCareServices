package com.techcare.services.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.techcare.services.R;
import com.techcare.services.database.BookingDAO;
import com.techcare.services.models.User;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private Context context;
    private List<User> customerList;
    private BookingDAO bookingDAO;

    public CustomerAdapter(Context context, List<User> customerList) {
        this.context = context;
        this.customerList = customerList;
        this.bookingDAO = new BookingDAO(context);
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        User customer = customerList.get(position);

        holder.tvCustomerName.setText(customer.getName());
        holder.tvCustomerEmail.setText("📧 " + customer.getEmail());
        holder.tvCustomerPhone.setText("📞 " + customer.getPhone());

        int bookingCount = bookingDAO.getBookingsByUserId(customer.getId()).size();
        holder.tvCustomerBookings.setText("Total Bookings: " + bookingCount);
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName, tvCustomerEmail, tvCustomerPhone, tvCustomerBookings;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvCustomerEmail = itemView.findViewById(R.id.tvCustomerEmail);
            tvCustomerPhone = itemView.findViewById(R.id.tvCustomerPhone);
            tvCustomerBookings = itemView.findViewById(R.id.tvCustomerBookings);
        }
    }
}
