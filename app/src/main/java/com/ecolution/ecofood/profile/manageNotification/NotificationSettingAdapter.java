package com.ecolution.ecofood.profile.manageNotification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecolution.ecofood.R;

import java.util.List;

interface NotificationSettingAction {
    void pushNotificationSwitchClicked(int position, boolean value);
    void inAppNotificationSwitchClicked(int position, boolean value);
}

class NotificationSettingAdapter extends RecyclerView.Adapter<NotificationSettingAdapter.NotificationSettingCardViewHolder> {
    private List<NotificationSettingModel> notificationSettings;
    private final NotificationSettingAction delegate;

    NotificationSettingAdapter(List<NotificationSettingModel> notificationSettings, NotificationSettingAction delegate) {
        this.notificationSettings = notificationSettings;
        this.delegate = delegate;
    }

    void updateData(List<NotificationSettingModel> notificationSettings) {
        this.notificationSettings = notificationSettings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationSettingCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_setting_card_view, parent, false);
        return new NotificationSettingCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationSettingCardViewHolder holder, int position) {
        NotificationSettingModel setting = notificationSettings.get(position);
        holder.updateWithSetting(setting, position, delegate);
    }

    @Override
    public int getItemCount() { return notificationSettings.size(); }

    static class NotificationSettingCardViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        Switch pushNotificationSwitch, inAppNotificationSwitch;
        ImageView imageView;

        NotificationSettingCardViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.NotificationTitleTextView);
            pushNotificationSwitch = itemView.findViewById(R.id.PushNotificationSwitch);
            inAppNotificationSwitch = itemView.findViewById(R.id.inAppNotificationSwitch);
            imageView = itemView.findViewById(R.id.cardSettingImageView);

            pushNotificationSwitch.setText("Push Notification");
            inAppNotificationSwitch.setText("In App Notification");
        }

        void updateWithSetting(@NonNull NotificationSettingModel setting, int position, NotificationSettingAction delegate) {
            titleTextView.setText(setting.getTitle());
            pushNotificationSwitch.setChecked(setting.getPushNotificationEnable());
            inAppNotificationSwitch.setChecked(setting.getInAppNotificationEnable());

            pushNotificationSwitch.setOnCheckedChangeListener((button, value) -> delegate.pushNotificationSwitchClicked(position, value));
            inAppNotificationSwitch.setOnCheckedChangeListener((button, value) -> delegate.inAppNotificationSwitchClicked(position, value));
        }
    }
}
