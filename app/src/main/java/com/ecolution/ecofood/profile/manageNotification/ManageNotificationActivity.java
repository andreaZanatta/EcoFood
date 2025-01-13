package com.ecolution.ecofood.profile.manageNotification;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecolution.ecofood.R;
import com.ecolution.ecofood.model.CustomerModel;
import com.ecolution.ecofood.model.NotificationType;
import com.ecolution.ecofood.model.SellerModel;
import com.ecolution.ecofood.model.UserModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageNotificationActivity extends AppCompatActivity implements NotificationSettingAction {

    RecyclerView notificationSettingsRecyclerView;
    NotificationSettingAdapter notificationSettingAdapter;

    FirebaseFirestore db;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareActivity();

        db = FirebaseFirestore.getInstance();
        var serializableUser = getIntent().getSerializableExtra("user");
        if (serializableUser instanceof UserModel) {
            userModel = (UserModel) serializableUser;
        }

        notificationSettingsRecyclerView = findViewById(R.id.notificationSettingsRecyclerView);
        notificationSettingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<NotificationSettingModel> notificationSettings = getNotificationSettings();

        notificationSettingAdapter = new NotificationSettingAdapter(notificationSettings, this);
        notificationSettingsRecyclerView.setAdapter(notificationSettingAdapter);
    }

    private void prepareActivity() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.manageNotificationToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage Notifications");
    }

    private List<NotificationSettingModel> getNotificationSettings() {
        List<NotificationSettingModel> result = new ArrayList<>();
        if (userModel instanceof CustomerModel) {
            CustomerModel customerModel = (CustomerModel) userModel;
            result.add(new NotificationSettingModel("New Product",
                    customerModel.getNotification() == NotificationType.OnlyPush || customerModel.getNotification() == NotificationType.All,
                    customerModel.getNotification() == NotificationType.OnlyInApp || customerModel.getNotification() == NotificationType.All));
        } else if (userModel instanceof SellerModel) {
            SellerModel sellerModel = (SellerModel) userModel;
            result.add(new NotificationSettingModel("New Reviews",
                    sellerModel.getNotification() == NotificationType.OnlyPush || sellerModel.getNotification() == NotificationType.All,
                    sellerModel.getNotification() == NotificationType.OnlyInApp || sellerModel.getNotification() == NotificationType.All));
        }

        return result;
    }


    public void pushNotificationSwitchClicked(int position, boolean value) {
        if (userModel instanceof CustomerModel) {
            CustomerModel customerModel = (CustomerModel) userModel;
            NotificationType notification;
            if (value) {
                notification = customerModel.getNotification() == NotificationType.OnlyInApp ? NotificationType.All : NotificationType.OnlyPush;
            } else {
                notification = customerModel.getNotification() == NotificationType.All ? NotificationType.OnlyInApp : NotificationType.None;
            }
            customerModel.setNotification(notification);
        } else if (userModel instanceof SellerModel) {
            SellerModel sellerModel = (SellerModel) userModel;
            NotificationType notification;
            if (value) {
                notification = sellerModel.getNotification() == NotificationType.OnlyInApp ? NotificationType.All : NotificationType.OnlyPush;
            } else {
                notification = sellerModel.getNotification() == NotificationType.All ? NotificationType.OnlyInApp : NotificationType.None;
            }
            sellerModel.setNotification(notification);
        }
        updateNotificationForUser();
    }

    public void inAppNotificationSwitchClicked(int position, boolean value) {
        if (userModel instanceof CustomerModel) {
            CustomerModel customerModel = (CustomerModel) userModel;
            NotificationType notification;
            if (value) {
                notification = customerModel.getNotification() == NotificationType.OnlyPush ? NotificationType.All : NotificationType.OnlyInApp;
            } else {
                notification = customerModel.getNotification() == NotificationType.All ? NotificationType.OnlyPush : NotificationType.None;
            }
            customerModel.setNotification(notification);
        } else if (userModel instanceof SellerModel) {
            SellerModel sellerModel = (SellerModel) userModel;
            NotificationType notification;
            if (value) {
                notification = sellerModel.getNotification() == NotificationType.OnlyPush ? NotificationType.All : NotificationType.OnlyInApp;
            } else {
                notification = sellerModel.getNotification() == NotificationType.All ? NotificationType.OnlyPush : NotificationType.None;
            }
            sellerModel.setNotification(notification);
        }
        updateNotificationForUser();
    }

    void updateNotificationForUser() {
        Map<String, Object> updates = new HashMap<>();
        if (userModel instanceof CustomerModel) {
            CustomerModel customerModel = (CustomerModel) userModel;
            updates.put("notification", customerModel.getNotification());
        } else if (userModel instanceof SellerModel) {
            SellerModel sellerModel = (SellerModel) userModel;
            updates.put("notification", sellerModel.getNotification());
        }

        if (!updates.isEmpty()) {
            db.collection("users")
                    .document(userModel.getUser_id())
                    .update(updates)
                    .addOnCompleteListener(result -> notificationSettingAdapter.updateData(getNotificationSettings()));
        }
    }
}