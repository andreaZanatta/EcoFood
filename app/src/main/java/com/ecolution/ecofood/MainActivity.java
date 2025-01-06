package com.ecolution.ecofood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.ecolution.ecofood.homepages.HomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button venditoreButton = findViewById(R.id.button_venditore);
        Button compratoreButton = findViewById(R.id.button_compratore);

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
    }

    private void openHome(boolean isVenditore) {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("isVenditore", isVenditore);
        startActivity(intent);
    }
}


