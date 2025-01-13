package com.ecolution.ecofood.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecolution.ecofood.R;
import com.ecolution.ecofood.model.SellerModel;
import com.ecolution.ecofood.model.UserModel;
import com.ecolution.ecofood.profile.manage.ManageAccountActivity;
import com.ecolution.ecofood.utils.TabBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Optional;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImage;
    TextView nameTextView;
    TextView emailTextView;
    Button modifyProfileButton;
    RecyclerView notificationRecyclerView;
    UserModel userModel;
    ArrayList<NotificationModel> notifications = new ArrayList<>();
    TabBar tabBar;

    private FirebaseFirestore db;

    NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();

        prepareActivity();

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
        notificationRecyclerView = findViewById(R.id.notificationRecyclerView);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        modifyProfileButton.setOnClickListener(button -> openManageProfile());

        notificationAdapter = new NotificationAdapter(notifications);
        notificationRecyclerView.setAdapter(notificationAdapter);
    }

    private void getUserModel() {
        // session information retrieved
        SharedPreferences sessionInformations = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String uId = sessionInformations.getString("uId", "");

        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot us : task.getResult()){
                        UserModel user = us.toObject(UserModel.class);
                        if(user.getUser_id().equals(uId)) {
                            userModel = user;

                            getNotifications();
                            updateInterface();
                        }
                    }
                }
            }
        });
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
        startActivity(intent);
    }
}
