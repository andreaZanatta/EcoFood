package com.ecolution.ecofood.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecolution.ecofood.LoginActivity;
import com.ecolution.ecofood.R;
import com.ecolution.ecofood.model.*;
import com.ecolution.ecofood.profile.manageAccount.ManageAccountActivity;
import com.ecolution.ecofood.profile.manageNotification.ManageNotificationActivity;
import com.ecolution.ecofood.utils.TabBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Optional;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImage, manageNotificationButton;
    TextView nameTextView, emailTextView;
    Button modifyProfileButton, logoutButton;
    RecyclerView notificationRecyclerView;
    UserModel userModel;
    ArrayList<NotificationModel> notifications = new ArrayList<>();
    TabBar tabBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    NotificationAdapter notificationAdapter;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareActivity();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        SharedPreferences sessionInformations = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isSeller = sessionInformations.getBoolean("userType", false);

        // tab bar managed
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        tabBar = new TabBar(this, bottomNav);
        tabBar.setupBottomNavigationMenu(bottomNav.getMenu(), isSeller);
        tabBar.updateSelectedItem(R.id.profile); // Set the appropriate menu item ID
        bottomNav.setOnItemSelectedListener(item -> {
            tabBar.handleNavigation(item);
            return  true;
        });


        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> getUserModel()
        );

        logoutButton.setOnClickListener(v -> logout());

        getUserModel();
    }

    private void prepareActivity() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        profileImage = findViewById(R.id.profileImage);
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        modifyProfileButton = findViewById(R.id.modifyProfileButton);
        manageNotificationButton = findViewById(R.id.manageNotificationButton);
        notificationRecyclerView = findViewById(R.id.notificationRecyclerView);
        logoutButton = findViewById(R.id.logoutButton);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        modifyProfileButton.setOnClickListener(button -> openManageProfile());
        manageNotificationButton.setOnClickListener(view -> openManageNotificationSetting());

        notificationAdapter = new NotificationAdapter(notifications);
        notificationRecyclerView.setAdapter(notificationAdapter);
    }

    private void getUserModel() {
        String idUtente = mAuth.getUid();
        if (idUtente != null) {
            db.collection("users")
                    .document(idUtente)
                    .get()
                    .addOnSuccessListener(value -> {
                        if (value.exists()) {
                            boolean isSeller = value.getBoolean("seller");

                            if (isSeller) {
                                userModel = value.toObject(SellerModel.class);
                            } else {
                                userModel = value.toObject(CustomerModel.class);
                            }

                            getNotifications();
                            updateInterface();
                        }
                    });
        }
    }

    private void updateInterface() {
        if (userModel != null) {
            nameTextView.setText(userModel.getFullName());
            emailTextView.setText(userModel.getEmail());

            Glide.with(this)
                    .load(userModel.getImage())
                    .into(profileImage);
        }
    }

    private void getNotifications() {
        db.collection("notifications")
                .whereEqualTo("idUtente", userModel.getUser_id())
                .orderBy("dataDiCreazione", Query.Direction.DESCENDING)
                .limit(10)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }

                    for (DocumentChange change: value.getDocumentChanges()) {
                        Integer idNotifica = (Integer) change.getDocument().getData().getOrDefault("idNotifica", 0);
                        switch (change.getType()) {
                            case ADDED:
                                String title = (String) change.getDocument().getData().getOrDefault("titolo", "");
                                String description = (String) change.getDocument().getData().getOrDefault("descrizione", "");
                                Timestamp dataTimeSpan = (Timestamp) change.getDocument().getData().getOrDefault("dataDiCreazione", Timestamp.now());

                                LocalDateTime dataDiCreazione = getLocalDateTime(dataTimeSpan);

                                NotificationModel model = new NotificationModel(idNotifica, title, description, dataDiCreazione);
                                notifications.add(model);
                                break;
                            case REMOVED:
                                notifications.removeIf((notifica) -> notifica.getIdNotifica().intValue() == idNotifica.intValue());
                                break;
                            case MODIFIED:
                                Optional<NotificationModel> notification = notifications.stream().filter(x -> x.getIdNotifica() == idNotifica.intValue()).findFirst();

                                notification.ifPresent(x -> {
                                    x.setTitolo((String) change.getDocument().getData().getOrDefault("title", ""));
                                    x.setDescrizione((String) change.getDocument().getData().getOrDefault("description", ""));
                                    x.setDataDiCreazione(getLocalDateTime((Timestamp) change.getDocument().getData().getOrDefault("dataDiCreazione", Timestamp.now())));
                                });
                                break;
                        }
                    }

                    notificationAdapter.updateData(notifications);
                });
    }

    private LocalDateTime getLocalDateTime(Timestamp dataTimeSpan) {
        return dataTimeSpan.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private void openManageProfile() {
        Intent intent = new Intent(ProfileActivity.this, ManageAccountActivity.class);
        intent.putExtra("user", userModel);
        activityResultLauncher.launch(intent);
    }

    private void openManageNotificationSetting() {
        Intent intent = new Intent(ProfileActivity.this, ManageNotificationActivity.class);
        intent.putExtra("user", userModel);
        activityResultLauncher.launch(intent);
    }

    private void logout(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("AppPrefs", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(intent);
    }
}
