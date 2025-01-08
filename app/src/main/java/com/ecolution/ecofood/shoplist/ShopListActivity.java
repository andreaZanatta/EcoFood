package com.ecolution.ecofood.shoplist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecolution.ecofood.R;
import com.ecolution.ecofood.model.SellerModel;
import com.ecolution.ecofood.productdetail.NavItemAdapter;
import com.ecolution.ecofood.productdetail.ProductListActivity;
import com.ecolution.ecofood.utils.TabBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShopListActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    TabBar tabBar;
    RecyclerView recyclerView;
    List<SellerModel> shops;
    NavShopListAdapter nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        // db initialization
        db = FirebaseFirestore.getInstance();

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


        recyclerView = findViewById(R.id.product_shop_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        shops = new ArrayList<>();
        populateShops(shops);

        nav = new NavShopListAdapter(this, shops);
        recyclerView.setAdapter(nav);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EdgeToEdge.enable(this);
    }

    private void populateShops(List<SellerModel> shops){
        db.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                SellerModel shop = doc.toObject(SellerModel.class);
                                shops.add(shop);
                                nav.notifyDataSetChanged();
                            }
                        } else Toast.makeText(ShopListActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}