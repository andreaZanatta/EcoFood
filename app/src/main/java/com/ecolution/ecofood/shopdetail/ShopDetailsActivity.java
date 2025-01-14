package com.ecolution.ecofood.shopdetail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

import com.ecolution.ecofood.CustomerProductListActivity;
import com.ecolution.ecofood.LoginActivity;
import com.ecolution.ecofood.MainActivity;
import com.ecolution.ecofood.R;
import com.ecolution.ecofood.homepages.MapActivity;
import com.ecolution.ecofood.model.CustomerModel;
import com.ecolution.ecofood.model.ReviewModel;
import com.ecolution.ecofood.model.SellerModel;
import com.ecolution.ecofood.productdetail.ProductListActivity;
import com.ecolution.ecofood.shoplist.ShopListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShopDetailsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    Button goToProductList, goBack, reviewButton;
    List<ReviewModel> reviewList;
    NavReviewAdapter navReviewAdapter;
    RecyclerView reviewView;
    RatingBar absoluteRatings;
    private String shopId;
    TextView shopName;
    String userName;
    String fromWhere;

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

        // db initialization
        db = FirebaseFirestore.getInstance();


        // session information retrieved
        SharedPreferences sessionInformations = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        userName = sessionInformations.getString("firstName", "");


        // variables initialization
        goToProductList = findViewById(R.id.myCustomButton); // Lista prodotti
        goBack = findViewById(R.id.button_with_arrow);       // Lista negozi
        reviewView = findViewById(R.id.reviews_list);        // Show reviews
        shopName = findViewById(R.id.textView);              // Shop name
        absoluteRatings = findViewById(R.id.ratings);        // Rating Bar
        reviewButton = findViewById(R.id.ratingsButton);     // Review Button for adding review


        //intent informations (shopId)
        Intent intentV1 = getIntent();
        shopId = intentV1.getStringExtra("shopId");
        shopName.setText(intentV1.getStringExtra("shopName"));
        fromWhere = intentV1.getStringExtra("from");


        // Evaluation Review List
        showReviews();


        goToProductList.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomerProductListActivity.class);
            intent.putExtra("shopId", intentV1.getStringExtra("shopId"));
            intent.putExtra("shopName", intentV1.getStringExtra("shopName"));
            this.startActivity(intent);
        });

        reviewButton.setOnClickListener(v -> addReviewToDb());


        // Go Back Button On Click
        goBack.setOnClickListener(v -> {
            Intent intent;
            Log.d("Debug", "FROM WHERE?? " + fromWhere);
            if(fromWhere == null || fromWhere.equals("list")) intent = new Intent(ShopDetailsActivity.this, ShopListActivity.class);
            else intent = new Intent(ShopDetailsActivity.this, MapActivity.class);
            startActivity(intent);
        });

        // TODO: Go to product list of specific shop
        /*goToProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CustomerIntent = new Intent(ShopDetailsActivity.this, ProductListActivity.class);
                CustomerIntent.putExtra("userType", "customer");
                startActivity(CustomerIntent);
            }
        });*/
    }

    private void populateReviews(@NonNull Runnable onComplete){
        db.collection("reviews").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        ReviewModel review = doc.toObject(ReviewModel.class);
                        if(review.getSeller().equals(shopId)) {
                            reviewList.add(review);
                            //nav.notifyDataSetChanged();
                        }
                    }
                } else Toast.makeText(ShopDetailsActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                onComplete.run();
            }
        });
    }

    private float getAverage(){
        float avg = 0;
        int i = 0;
        for(ReviewModel rev : reviewList){
            avg += rev.getGrade();
            i++;
        }
        avg /= i;
        return avg;
    }

    private void addReviewToDb() {
        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_review, null);

        // Initialize the dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(dialogView);

        android.app.AlertDialog dialog = builder.create();
        dialog.show();

        RatingBar addReviewGrade = dialog.findViewById(R.id.review_rating_bar);
        EditText addReviewDescription = dialog.findViewById(R.id.review_description_field);
        Button saveButton = dialogView.findViewById(R.id.postReviewButton);

        saveButton.setOnClickListener(v -> {
            String description = addReviewDescription.getText().toString();
            Float grade = addReviewGrade.getRating();
            String customer = userName;
            String seller = shopId;

            // Collect updated data
            /*String name = productName.getText().toString();
            String category = productCategory.getText().toString();
            double price = Double.parseDouble(productPrice.getText().toString());
            String description = productDescription.getText().toString();*/

            //TODO: photo and data
            //String photo
            //Date

            if (description.isEmpty() || grade.isNaN()) {
                Toast.makeText(this, "Compila tutti i campi obbligatori!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Save changes to database
            createReview(description, grade.floatValue(), customer, seller);

            // Close dialog
            dialog.dismiss();
            showReviews();
        });
    }

    private void createReview(String descr, float grade, String customer, String seller){
        ReviewModel review = new ReviewModel(null, descr, grade, customer, seller);

        db.collection("reviews").add(review).addOnSuccessListener(documentReference -> Toast.makeText(this, "Review added successfully", Toast.LENGTH_SHORT).show());
    }

    private void showReviews(){
        reviewList = new ArrayList<>();
        populateReviews(() -> {
            absoluteRatings.setRating(getAverage());

            // Nav Adapter for Review List (Horizontal Scroll)
            navReviewAdapter = new NavReviewAdapter(this, reviewList);
            reviewView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            reviewView.setAdapter(navReviewAdapter);
        });
    }
}