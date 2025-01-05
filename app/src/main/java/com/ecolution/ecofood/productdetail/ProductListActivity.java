package com.ecolution.ecofood.productdetail;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.ecolution.ecofood.shopdetail.ShopDetailsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    Button goBack;
    FloatingActionButton btn;
    RecyclerView recyclerView;
    List<ItemModel> itemModels;
    NavItemAdapter navItemAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();

        setContentView(R.layout.activity_product_list);

        SharedPreferences sessionInformations = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isSeller = sessionInformations.getBoolean("userType", false);
        //String userType = isSeller ? "seller" : "customer";
        String uId = sessionInformations.getString("uId", "0");

        recyclerView = findViewById(R.id.product_list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        itemModels = new ArrayList<>();
        populateItemModels(itemModels); // Call a helper method to populate the list

        navItemAdapter = new NavItemAdapter(this, itemModels, isSeller);
        recyclerView.setAdapter(navItemAdapter);

        //inzio codice mio
        if(!isSeller) {
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
                Intent CustomerIntent = new Intent(ProductListActivity.this, ShopDetailsActivity.class);
                CustomerIntent.putExtra("userType", "customer");
                startActivity(CustomerIntent);
            }
        });
    }
    //private void populateItemModels(List<ItemModel> itemModels) {}

    private void populateItemModels(List<ItemModel> itemModels) {
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ItemModel itemModel = document.toObject(ItemModel.class);
                                //if(itemModel.getVenditore().getUser_id() == /*INFORMAZIONE DI SESSIONE)
                                itemModels.add(itemModel);
                                navItemAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(ProductListActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}