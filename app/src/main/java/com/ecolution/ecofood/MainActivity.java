package com.ecolution.ecofood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


import com.ecolution.ecofood.home.HomeActivity;
import com.ecolution.ecofood.model.UserModel;
import com.ecolution.ecofood.profile.ProfileActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);

        UserModel user = new UserModel(1, "Andrea", "Zanatta", "883464@stud.unive.it", "", false, "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.wikihow.com%2FWhat-Type-of-Person-Am-I&psig=AOvVaw2tXWD_3Ozgzh9oAmFyKpIE&ust=1734286863292000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCMip5Zfwp4oDFQAAAAAdAAAAABAE");

        intent.putExtra("user", user);
        startActivity(intent);
    }
}


