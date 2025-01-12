package com.ecolution.ecofood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.ecolution.ecofood.home.HomeActivity;
import com.ecolution.ecofood.model.UserModel;
import com.ecolution.ecofood.profile.ProfileActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    String idUtente = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idUtente = getIntent().getStringExtra("idUtente");
        db = FirebaseFirestore.getInstance();

        Button venditoreButton = findViewById(R.id.button_venditore);
        Button compratoreButton = findViewById(R.id.button_compratore);
        Button profiloButton = findViewById(R.id.button_profilo_main);

        venditoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome(true);
            }
        });

        compratoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome(false);
            }
        });

        profiloButton.setOnClickListener(v -> openProfile());
    }

    private void openHome(boolean isVenditore) {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("isVenditore", isVenditore);
        startActivity(intent);
    }

    private void openProfile() {
        db.collection("users")
                .document(idUtente)
                .get()
                .addOnSuccessListener(value -> {
                    if (value.exists()) {
                        UserModel user = value.toObject(UserModel.class);

                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    } else {

                    }
                });
    }
}


