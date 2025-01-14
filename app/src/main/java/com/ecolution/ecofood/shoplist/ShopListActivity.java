package com.ecolution.ecofood.shoplist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.ecolution.ecofood.model.CustomerModel;
import com.ecolution.ecofood.model.SellerModel;
import com.ecolution.ecofood.model.UserModel;
import com.ecolution.ecofood.productdetail.NavItemAdapter;
import com.ecolution.ecofood.productdetail.ProductListActivity;
import com.ecolution.ecofood.shopdetail.ShopDetailsActivity;
import com.ecolution.ecofood.utils.TabBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class ShopListActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    TabBar tabBar;
    RecyclerView recyclerView;
    List<SellerModel> shops;
    NavShopListAdapter nav;
    String userId;
    UserModel currentUser;
    List<String> favourites;

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
        userId = sessionInformations.getString("uId", "");

        // tab bar managed
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        tabBar = new TabBar(this, bottomNav);
        tabBar.updateSelectedItem(R.id.shopList); // Set the appropriate menu item ID
        tabBar.setupBottomNavigationMenu(bottomNav.getMenu(), isSeller);
        bottomNav.setOnItemSelectedListener(item -> {
            tabBar.handleNavigation(item);
            return  true;
        });


        recyclerView = findViewById(R.id.product_shop_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));



        favourites = new ArrayList<>();
        evaluateCurrentUser(() -> {
            Log.wtf("Debug", "USER CHECK LIST: =====================" + favourites);
            nav = new NavShopListAdapter(this, shops, favourites, userId);
            recyclerView.setAdapter(nav);
            shops = new ArrayList<>();
            populateShops(shops);
        });

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
                                if(shop.isSeller() && (!shop.getUser_id().equals(userId))){
                                    shops.add(shop);
                                    nav.notifyDataSetChanged();
                                }
                            }
                        } else Toast.makeText(ShopListActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void evaluateCurrentUser(@NonNull Runnable onComplete){
        if (userId != null) {
            db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            UserModel us = doc.toObject(UserModel.class);

                            if(us.getUser_id().equals(userId)){
                                favourites = us.getFavourites();
                            }
                        }
                    } else Toast.makeText(ShopListActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                    onComplete.run();
                }
            });
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        shops = new ArrayList<>();
        populateShops(shops);
    }
}

