package com.ecolution.ecofood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecolution.ecofood.model.ItemModel;
import com.ecolution.ecofood.productdetail.NavItemAdapter;
import com.ecolution.ecofood.productdetail.ProductListActivity;
import com.ecolution.ecofood.shopdetail.ShopDetailsActivity;
import com.ecolution.ecofood.shoplist.ShopListActivity;
import com.ecolution.ecofood.utils.TabBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CustomerProductListActivity extends AppCompatActivity {
    TextView shopNameTextView;
    RecyclerView productList;
    private FirebaseFirestore db;
    List<ItemModel> items;
    NavItemAdapter navItemAdapter;
    String shopName;
    String shopId;
    Button goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_product_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // db initialization
        db = FirebaseFirestore.getInstance();

        // populate and manage the item array retrieved from db
        productList = findViewById(R.id.product_list_view);
        productList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        //intent informations (shopId)
        Intent intentV1 = getIntent();
        shopId = intentV1.getStringExtra("shopId");
        shopName = intentV1.getStringExtra("shopName");


        items = new ArrayList<>();
        populateItemModels(items); // Call a helper method to populate the list

        navItemAdapter = new NavItemAdapter(CustomerProductListActivity.this, items, false);
        productList.setAdapter(navItemAdapter);

        shopNameTextView = findViewById(R.id.shopName);
        shopNameTextView.setText(shopName);


        goBack = findViewById(R.id.button_with_arrow);
        goBack.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerProductListActivity.this, ShopDetailsActivity.class);
            intent.putExtra("shopId", intentV1.getStringExtra("shopId"));
            intent.putExtra("shopName", intentV1.getStringExtra("shopName"));
            this.startActivity(intent);
        });
    }


    private void populateItemModels(List<ItemModel> it) {
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ItemModel itemModel = document.toObject(ItemModel.class);
                                //Log.d("debug", "UID PRINT: " + uId);

                                Log.d("debug", "=======================\nITEM ID PRINT: " + itemModel.getId());
                                if (itemModel.getVenditoreId().equals(shopId)) {
                                    it.add(itemModel);
                                    navItemAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            Toast.makeText(CustomerProductListActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}