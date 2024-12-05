package com.ecolution.ecofood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ecolution.ecofood.databinding.ActivityMainBinding;
import com.ecolution.ecofood.productdetail.ProductListActivity;


public class MainActivity extends AppCompatActivity {


    //private ActivityMainBinding binding;
    Button CustomerBtn;
    Button SellerBtn;

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


        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(MainActivity.this, ProductListActivity.class);
                startActivity(switchActivityIntent);

            }
        });*/

    }
}