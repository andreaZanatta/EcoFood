package com.ecolution.ecofood.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecolution.ecofood.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationCardViewHolder> {
    private List<NotificationModel> notificationModels;

    public NotificationAdapter(List<NotificationModel> notificationModels) {
        this.notificationModels = notificationModels;
    }

    public void updateData(List<NotificationModel> notificationModels) {
        this.notificationModels = notificationModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card_view, parent, false);
        return new NotificationCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationCardViewHolder holder, int position) {
        NotificationModel info = notificationModels.get(position);
        holder.mainTextView.setText(info.getTitolo());
        holder.descriptionTextView.setText(info.getDescrizione());
        holder.timeTextView.setText("1 Hours Ago");
        //holder.notificationImageView.setImageIcon();
    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    public static class NotificationCardViewHolder extends RecyclerView.ViewHolder {
        TextView mainTextView, descriptionTextView, timeTextView;
        ImageView notificationImageView;

        public NotificationCardViewHolder(@NonNull View itemView) {
            super(itemView);
            mainTextView = itemView.findViewById(R.id.mainTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            notificationImageView = itemView.findViewById(R.id.notificationImageView);
        }
    }
}