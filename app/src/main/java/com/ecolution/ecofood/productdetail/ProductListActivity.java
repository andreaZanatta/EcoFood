package com.ecolution.ecofood.productdetail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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

import com.ecolution.ecofood.MainActivity;
import com.ecolution.ecofood.R;
import com.ecolution.ecofood.model.ItemModel;

import com.ecolution.ecofood.shopdetail.ShopDetailsActivity;
import com.ecolution.ecofood.utils.TabBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    FloatingActionButton btn;
    RecyclerView recyclerView;
    List<ItemModel> items;
    NavItemAdapter navItemAdapter;
    private FirebaseFirestore db;
    TextView shopNameView;
    TabBar tabBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // db initialization
        db = FirebaseFirestore.getInstance();

        setContentView(R.layout.activity_product_list);

        // session information retrieved
        SharedPreferences sessionInformations = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isSeller = sessionInformations.getBoolean("userType", false);
        String shopName = sessionInformations.getString("shopName", "");


        // tab bar managed
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        tabBar = new TabBar(this, bottomNav);
        tabBar.setupBottomNavigationMenu(bottomNav.getMenu(), isSeller);
        bottomNav.setOnItemSelectedListener(item -> {
            tabBar.handleNavigation(item);
            return  true;
        });


        // populate and manage the item array retrieved from db
        recyclerView = findViewById(R.id.product_list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        items = new ArrayList<>();
        populateItemModels(items); // Call a helper method to populate the list

        btn = findViewById(R.id.addButton);
        btn.setOnClickListener(v -> {
            addItemTodb();

            //fetch from database to load the last element added but it doesnt work
            //items = new ArrayList<>();
            //populateItemModels(items);
        });

        navItemAdapter = new NavItemAdapter(this, items, isSeller);
        recyclerView.setAdapter(navItemAdapter);

        shopNameView = findViewById(R.id.shopName);
        shopNameView.setText(shopName);





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EdgeToEdge.enable(this);
    }
    //private void populateItemModels(List<ItemModel> itemModels) {}

    private void populateItemModels(List<ItemModel> it) {
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        SharedPreferences sessionInformations = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                        String uId = sessionInformations.getString("uId", "0");

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ItemModel itemModel = document.toObject(ItemModel.class);
                                Log.d("debug", "UID PRINT: " + uId);

                                Log.d("debug", "=======================\nITEM ID PRINT: " + itemModel.getId());
                                if(itemModel.getVenditoreId().equals(uId)) {
                                    it.add(itemModel);
                                    navItemAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            Toast.makeText(ProductListActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    private void addItemTodb(){
        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_item_to_shop, null);

        // Initialize the dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(dialogView);

        android.app.AlertDialog dialog = builder.create();
        dialog.show();

        EditText productName = dialogView.findViewById(R.id.product_name);
        EditText productCategory = dialogView.findViewById(R.id.product_category);
        EditText productPrice = dialogView.findViewById(R.id.product_price);
        EditText productDescription = dialogView.findViewById(R.id.product_description);
        //TODO: photo and data
        //RelativeLayout uploadPhoto = findViewById(R.id.upload_photo_of_product);
        //Date
        Button saveButton = dialogView.findViewById(R.id.save_button);

        saveButton.setOnClickListener(v -> {
            // Collect updated data
            String name = productName.getText().toString();
            String category = productCategory.getText().toString();
            double price = Double.parseDouble(productPrice.getText().toString());
            String description = productDescription.getText().toString();
            //TODO: photo and data
            //String photo
            //Date


            // Save changes to database
            createItem(name, category, price, description, null, null);

            // Close dialog
            dialog.dismiss();
        });
    }

    private void createItem(String name, String category, double price, String description, String img, Date timestamp) {
        SharedPreferences info = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        ItemModel product = new ItemModel(null, name, category, price, description, img, timestamp, info.getString("uId", "-1"));

        db.collection("products").add(product).addOnSuccessListener(docReference -> {
            product.setId(docReference.getId());

            docReference.update("id", product.getId()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    navItemAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

}