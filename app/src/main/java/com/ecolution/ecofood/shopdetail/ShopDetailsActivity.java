package com.ecolution.ecofood.shopdetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecolution.ecofood.MainActivity;
import com.ecolution.ecofood.R;
import com.ecolution.ecofood.home.HomeActivity;
import com.ecolution.ecofood.model.CustomerModel;
import com.ecolution.ecofood.model.ItemModel;
import com.ecolution.ecofood.model.ReviewModel;
import com.ecolution.ecofood.model.SellerModel;
import com.ecolution.ecofood.productdetail.NavItemAdapter;
import com.ecolution.ecofood.productdetail.ProductListActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShopDetailsActivity extends AppCompatActivity {
    Button goToProductList;
    Button goBack;
    List<ReviewModel> reviewList;
    NavReviewAdapter navReviewAdapter;
    RecyclerView reviewView;
    RatingBar absoluteRatings;
    String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_shop_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.shop_detail_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        goToProductList = findViewById(R.id.myCustomButton);
        goToProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CustomerIntent = new Intent(ShopDetailsActivity.this, ProductListActivity.class);
                CustomerIntent.putExtra("userType", "customer");
                startActivity(CustomerIntent);
            }
        });

        goBack = findViewById(R.id.button_with_arrow);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CustomerIntent = new Intent(ShopDetailsActivity.this, MainActivity.class);
                CustomerIntent.putExtra("userType", "customer");
                startActivity(CustomerIntent);
            }
        });

        reviewView = findViewById(R.id.reviews_list);
        reviewView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        reviewList = new ArrayList<>();
        populateItemModels(reviewList);

        navReviewAdapter = new NavReviewAdapter(this, reviewList);
        reviewView.setAdapter(navReviewAdapter);

        absoluteRatings = findViewById(R.id.ratings);
        absoluteRatings.setRating(avgRatings());
    }

    private void populateItemModels(List<ReviewModel> reviews) {

        reviewList.add(new ReviewModel(
                "Great Service",
                "I had an excellent experience shopping here!",
                4.5f,
                // TODO: SISTEMARE id
                new CustomerModel(null, "John", "Doe", "john.doe@example.com"),
                new SellerModel(null, "Alice", "Brown", "alice.brown@example.com", "Alice's Shop", "123 Market Street")
        ));

        reviewList.add(new ReviewModel(
                "Amazing Products",
                "The products are top-notch, will definitely recmmend",
                5.0f,
                new CustomerModel(null, "Jane", "Smith", "jane.smith@example.com"),
                new SellerModel(null, "Bob", "White", "bob.white@example.com", "Bob's Emporium", "459 Commerce Evenue")
        ));

        reviewList.add(new ReviewModel(
                "Good but Expensive",
                "The products were good, but the price was a bit high",
                3.5f,
                new CustomerModel(null, "John", "Doe", "john.doe@example.com"),
                new SellerModel(null, "Bob", "White", "bob.white@example.com", "Bob's Emporium", "459 Commerce Evenue")
        ));
    }

    private float avgRatings(){
        float avg = 0;
        for(ReviewModel e : reviewList)
            avg += e.getGrade();
        return avg / reviewList.size();
    }
}