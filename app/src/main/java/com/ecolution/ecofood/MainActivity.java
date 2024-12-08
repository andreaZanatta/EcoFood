package com.ecolution.ecofood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ecolution.ecofood.productdetail.ProductListActivity;
import com.ecolution.ecofood.profile.ProfileActivity;
import com.ecolution.ecofood.shopdetail.ShopDetailsActivity;


public class MainActivity extends AppCompatActivity {


    //private ActivityMainBinding binding;
    Button CustomerBtn;
    Button SellerBtn;
    Button goToShop;
    Button goToProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding = ActivityMainBinding.inflate(getLayoutInflater());


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CustomerBtn = findViewById(R.id.goToCustomer);
        SellerBtn = findViewById(R.id.goToSeller);
        goToShop = findViewById(R.id.goToShop);
        goToProfile = findViewById(R.id.goToProfile);

        CustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CustomerIntent = new Intent(MainActivity.this, ProductListActivity.class);
                CustomerIntent.putExtra("userType", "customer");
                startActivity(CustomerIntent);
            }
        });

        SellerBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent SellerIntent = new Intent(MainActivity.this, ProductListActivity.class);
                 SellerIntent.putExtra("userType", "seller");
                 startActivity(SellerIntent);
             }
         });

        goToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ShopIntent = new Intent(MainActivity.this, ShopDetailsActivity.class);
                startActivity(ShopIntent);
            }
        });

        goToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(MainActivity.this, ProductListActivity.class);
                startActivity(switchActivityIntent);

            }
        });*/

    }
}