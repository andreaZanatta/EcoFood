package com.ecolution.ecofood.productdetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecolution.ecofood.MainActivity;
import com.ecolution.ecofood.R;
import com.ecolution.ecofood.model.ItemModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    Button goBack;
    FloatingActionButton btn;
    RecyclerView recyclerView;
    List<ItemModel> itemModels;
    NavItemAdapter navItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_list);

        String userType = getIntent().getStringExtra("userType");

        recyclerView = findViewById(R.id.product_list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        itemModels = new ArrayList<>();
        populateItemModels(itemModels); // Call a helper method to populate the list

        navItemAdapter = new NavItemAdapter(this, itemModels, userType);
        recyclerView.setAdapter(navItemAdapter);

        //inzio codice mio
        if("customer".equals(userType)) {
            btn = findViewById(R.id.addButton);
            btn.setVisibility(View.GONE);
        }
        //fine codice mio

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        EdgeToEdge.enable(this);



        goBack = findViewById(R.id.button_with_arrow);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CustomerIntent = new Intent(ProductListActivity.this, MainActivity.class);
                CustomerIntent.putExtra("userType", "customer");
                startActivity(CustomerIntent);
            }
        });
    }

    private void populateItemModels(List<ItemModel> itemModels) {
        itemModels.add(new ItemModel(
                "Apple",
                "Fruit",
                1.99,
                "Fresh red apples",
                "https://example.com/apple.jpg",
                new Date(2024, 12, 31)
        ));

        itemModels.add(new ItemModel(
                "Banana",
                "Fruit",
                0.99,
                "Ripe yellow bananas",
                "https://example.com/banana.jpg",
                new Date(2024, 11, 30)
        ));

        itemModels.add(new ItemModel(
                "Carrot",
                "Vegetable",
                1.49,
                "Organic carrots",
                "https://example.com/carrot.jpg",
                new Date(2024, 12, 15)
        ));

        itemModels.add(new ItemModel(
                "Milk",
                "Dairy",
                2.99,
                "Fresh whole milk",
                "https://example.com/milk.jpg",
                new Date(2024, 12, 10)
        ));

        itemModels.add(new ItemModel(
                "Bread",
                "Bakery",
                3.49,
                "Whole wheat bread",
                "https://example.com/bread.jpg",
                new Date(2024, 12, 05)
        ));
    }
}