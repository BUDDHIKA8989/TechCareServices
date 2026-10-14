package com.techcare.services.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.techcare.services.R;
import com.techcare.services.models.Device;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    private Context context;
    private List<Device> deviceList;
    private OnDeviceClickListener listener;

    public interface OnDeviceClickListener {
        void onDeviceClick(Device device);
    }

    public DeviceAdapter(Context context, List<Device> deviceList, OnDeviceClickListener listener) {
        this.context = context;
        this.deviceList = deviceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        Device device = deviceList.get(position);

        holder.tvDeviceName.setText(device.getDeviceName());
        holder.tvDeviceType.setText(device.getDeviceType());

        // Set emoji icons based on device name
        String icon = getDeviceIcon(device.getDeviceName());
        holder.tvDeviceIcon.setText(icon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeviceClick(device);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    private String getDeviceIcon(String deviceName) {
        if (deviceName.contains("Smartphone")) return "📱";
        if (deviceName.contains("Laptop")) return "💻";
        if (deviceName.contains("Television")) return "📺";
        if (deviceName.contains("Air Conditioner")) return "❄️";
        if (deviceName.contains("Refrigerator")) return "🧊";
        if (deviceName.contains("Washing Machine")) return "🧺";
        return "🔧";
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeviceIcon, tvDeviceName, tvDeviceType;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDeviceIcon = itemView.findViewById(R.id.tvDeviceIcon);
            tvDeviceName = itemView.findViewById(R.id.tvDeviceName);
            tvDeviceType = itemView.findViewById(R.id.tvDeviceType);
        }
    }
}
