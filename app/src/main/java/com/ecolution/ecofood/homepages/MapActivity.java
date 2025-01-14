package com.ecolution.ecofood.homepages;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.ecolution.ecofood.R;
import com.ecolution.ecofood.utils.TabBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapActivity extends AppCompatActivity {
    TabBar tabBar;
    private Fragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // session information retrieved
        SharedPreferences sessionInformations = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isSeller = sessionInformations.getBoolean("userType", false);

        // tab bar managed
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        tabBar = new TabBar(this, bottomNav);
        tabBar.updateSelectedItem(R.id.mapView); // Set the appropriate menu item ID
        tabBar.setupBottomNavigationMenu(bottomNav.getMenu(), isSeller);
        bottomNav.setOnItemSelectedListener(item -> {
            tabBar.handleNavigation(item);
            return  true;
        });

        // Initialize and add the MapFragment
        if (savedInstanceState == null) {
            mapFragment = new MapFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map, mapFragment) // Make sure you have a container in your activity_map.xml
                    .commit();
        }
    }
}