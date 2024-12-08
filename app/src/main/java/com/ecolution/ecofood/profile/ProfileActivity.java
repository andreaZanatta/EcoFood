package com.ecolution.ecofood.profile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecolution.ecofood.R;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImage;
    TextView nameTextView;
    TextView emailTextView;
    Button modifyProfileButton;
    RecyclerView notificationRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        ArrayList<NotificationInfo> notificationInfos = getNotifications();
        NotificationAdapter notificationAdapter = new NotificationAdapter(notificationInfos);
        notificationRecyclerView.setAdapter(notificationAdapter);
    }

    public ArrayList<NotificationInfo> getNotifications() {
        ArrayList<NotificationInfo> result = new ArrayList<>();

        result.add(new NotificationInfo("Promotion Ending", "Black Friday Promotion is ending tomorrow!", LocalDateTime.now()));
        result.add(new NotificationInfo("Promotion Ending", "Black Friday Promotion is ending tomorrow!", LocalDateTime.now()));
        result.add(new NotificationInfo("Promotion Ending", "Black Friday Promotion is ending tomorrow!", LocalDateTime.now()));

        return result;
    }
}
