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
import com.techcare.services.models.Notification;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private List<Notification> notificationList;
    private OnNotificationClickListener listener;

    public interface OnNotificationClickListener {
        void onViewBookingClick(Notification notification);
    }

    public NotificationAdapter(Context context, List<Notification> notificationList, OnNotificationClickListener listener) {
        this.context = context;
        this.notificationList = notificationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        holder.tvNotificationMessage.setText(notification.getMessage());
        holder.tvNotificationTime.setText(getTimeAgo(notification.getTimestamp()));

        // Change appearance for unread notifications
        if (!notification.isRead()) {
            holder.itemView.setBackgroundColor(0xFFE3F2FD);
            holder.tvNotificationIcon.setText("🔔");
        } else {
            holder.itemView.setBackgroundColor(0xFFFFFFFF);
            holder.tvNotificationIcon.setText("📬");
        }

        holder.btnViewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onViewBookingClick(notification);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    private String getTimeAgo(String timestamp) {
        if (timestamp == null || timestamp.isEmpty()) {
            return "Just now";
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = sdf.parse(timestamp);
            long time = date.getTime();
            long now = System.currentTimeMillis();
            long diff = now - time;

            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (seconds < 60) {
                return "Just now";
            } else if (minutes < 60) {
                return minutes + " min ago";
            } else if (hours < 24) {
                return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
            } else if (days < 7) {
                return days + " day" + (days > 1 ? "s" : "") + " ago";
            } else {
                SimpleDateFormat displayFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                return displayFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "Recently";
        }
    }


    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvNotificationIcon, tvNotificationMessage, tvNotificationTime;
        Button btnViewBooking;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotificationIcon = itemView.findViewById(R.id.tvNotificationIcon);
            tvNotificationMessage = itemView.findViewById(R.id.tvNotificationMessage);
            tvNotificationTime = itemView.findViewById(R.id.tvNotificationTime);
            btnViewBooking = itemView.findViewById(R.id.btnViewBooking);
        }
    }
}
