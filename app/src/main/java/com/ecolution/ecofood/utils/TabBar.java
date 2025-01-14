package com.ecolution.ecofood.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.ecolution.ecofood.R;
import com.ecolution.ecofood.homepages.HomeActivity;
import com.ecolution.ecofood.homepages.MapActivity;
import com.ecolution.ecofood.productdetail.ProductListActivity;
import com.ecolution.ecofood.profile.ProfileActivity;
import com.ecolution.ecofood.shoplist.ShopListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class TabBar {
    private Context context;
    private BottomNavigationView bottomNavigationView;

    public TabBar(Context context, BottomNavigationView bottomNavigationView) {
        this.context = context;
        this.bottomNavigationView = bottomNavigationView;
    }


    // Maybe we'll need to pass true or false at every activity

    public void setupBottomNavigationMenu(Menu menu, boolean isSeller) {
        // Hide or show seller-specific items
        MenuItem sellerDashboardItem = menu.findItem(R.id.personalShop);

        if (isSeller) sellerDashboardItem.setVisible(true); // Show Seller item
        else          sellerDashboardItem.setVisible(false); // Hide Seller item
    }

    public void handleNavigation(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.personalShop) context.startActivity(new Intent(context, ProductListActivity.class));
        else if(itemId == R.id.shopList) context.startActivity(new Intent(context, ShopListActivity.class));
        else if(itemId == R.id.mapView) context.startActivity(new Intent(context, MapActivity.class));
        else if(itemId == R.id.favorites) context.startActivity(new Intent(context, HomeActivity.class));
        else if(itemId == R.id.profile) context.startActivity(new Intent(context, ProfileActivity.class));
        else Log.e("Error", "Error On Navigation");
    }

    public void updateSelectedItem(int menuItemId) {
        bottomNavigationView.setSelectedItemId(menuItemId);
    }
}
