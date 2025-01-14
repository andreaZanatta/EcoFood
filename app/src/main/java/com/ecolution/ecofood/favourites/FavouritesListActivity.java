package com.ecolution.ecofood.favourites;

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
import com.ecolution.ecofood.model.UserModel;
import com.ecolution.ecofood.shoplist.NavShopListAdapter;
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

public class FavouritesListActivity extends AppCompatActivity {
    TabBar tabBar;
    FirebaseFirestore db;
    String userId;
    List<SellerModel> shops;
    List<String> shopsId;
    NavShopListAdapter nav;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favourites_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // db initialization
        db = FirebaseFirestore.getInstance();


        // session information retrieved
        SharedPreferences sessionInformations = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isSeller = sessionInformations.getBoolean("userType", false);
        userId = sessionInformations.getString("uId", "");

        // tab bar managed
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        tabBar = new TabBar(this, bottomNav);
        tabBar.updateSelectedItem(R.id.favorites); // Set the appropriate menu item ID
        tabBar.setupBottomNavigationMenu(bottomNav.getMenu(), isSeller);
        bottomNav.setOnItemSelectedListener(item -> {
            tabBar.handleNavigation(item);
            return  true;
        });

        shops = new ArrayList<>();
        recyclerView = findViewById(R.id.product_shop_list);
        nav = new NavShopListAdapter(this, shops, null, userId);


        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(nav);


        findFavourites(() -> evaluateFavouriteList());
    }

    private void findFavourites(@NonNull Runnable onComplete){
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        UserModel us = doc.toObject(UserModel.class);

                        if(us.getUser_id().equals(userId)){
                            shopsId = us.getFavourites();
                        }
                    }
                } else Toast.makeText(FavouritesListActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                onComplete.run();
            }
        });
    }

    private void evaluateFavouriteList(){
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        SellerModel shop = doc.toObject(SellerModel.class);

                        if(shopsId != null) {
                            if (shopsId.contains(shop.getUser_id())) {
                                shops.add(shop);
                                nav.notifyDataSetChanged();
                            }
                        }
                    }
                } else Toast.makeText(FavouritesListActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}