package com.ecolution.ecofood.profile.manage;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.ecolution.ecofood.R;
import com.ecolution.ecofood.model.UserModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ManageAccountActivity extends AppCompatActivity {

    EditText nameEditText, surnameEditText, emailEditText, passwordEditText;
    RadioGroup roleRadioGroup;
    ImageView profileImageView;
    Button confirmButton;

    private FirebaseFirestore db;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareActivity();

        db = FirebaseFirestore.getInstance();

        getUserModelByIntent();

        updateInterface();
    }

    private void updateInterface() {
        nameEditText.setText(userModel.getFirstName());
        surnameEditText.setText(userModel.getLastName());
        emailEditText.setText(userModel.getEmail());

        Glide.with(this)
                .load(userModel.getImage())
                .into(profileImageView);
    }

    private void prepareActivity() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameEditText = findViewById(R.id.et_nome);
        surnameEditText = findViewById(R.id.et_cognome);
        emailEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.et_password);
        roleRadioGroup = findViewById(R.id.rg_role);
        profileImageView = findViewById(R.id.imageView5);
        confirmButton = findViewById(R.id.btn_conferma);

        confirmButton.setOnClickListener(button -> confirmButtonPressed());
    }

    private void getUserModelByIntent() {
        Serializable userSerializable = getIntent().getSerializableExtra("user");
        if (userSerializable != null) {
            userModel = (UserModel) userSerializable;
        }
    }

    private void confirmButtonPressed() {
        db.collection("users")
                .whereEqualTo("id", userModel.getUser_id())
                .get()
                .addOnSuccessListener(snap -> {
                    for (QueryDocumentSnapshot document: snap) {
                        DocumentReference docRef = document.getReference();

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("nome", nameEditText.getText());
                        updates.put("cognome", surnameEditText.getText());
                        updates.put("email", emailEditText.getText());

                        docRef.update(updates).addOnSuccessListener(x -> backToProfileActivity());
                    }
                });
    }

    private void backToProfileActivity() {

    }
}