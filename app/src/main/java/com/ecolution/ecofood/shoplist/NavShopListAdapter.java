package com.ecolution.ecofood.shoplist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecolution.ecofood.R;
import com.ecolution.ecofood.model.ReviewModel;
import com.ecolution.ecofood.model.SellerModel;
import com.ecolution.ecofood.productdetail.ProductListActivity;
import com.ecolution.ecofood.shopdetail.NavReviewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class NavShopListAdapter extends RecyclerView.Adapter<NavShopListAdapter.ViewHolder> {
    Context context;
    List<SellerModel> sellerModelList;

    public NavShopListAdapter(Context context, List<SellerModel> sellerList){
        this.context = context;
        this.sellerModelList = sellerList;
    }

    @NonNull
    @Override
    public NavShopListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_for_shop_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NavShopListAdapter.ViewHolder holder, int position) {
        SellerModel shop = sellerModelList.get(position);

        holder.shopName.setText(sellerModelList.get(position).getShopName());
        holder.shopAddress.setText(sellerModelList.get(position).getAddress());
        //holder.shopImagePath.setText(sellerModelList.get(position).getLogo());
        evaluateStars(shop.getUser_id(), holder.starsReview);

        String shopImagePath = shop.getLogo();
        Glide.with(context)
                .load("file://" + shopImagePath)
                .placeholder(R.drawable.insalata)
                .error(R.drawable.carrot)
                .into(holder.shopImage);
    }

    private void evaluateStars(String shopId, RatingBar rtgs){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reviews").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        float mean = 0;
                        int length = 0;

                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                ReviewModel review = doc.toObject(ReviewModel.class);

                                if(review.getSeller().equals(shopId)){
                                    mean += review.getGrade();
                                    length++;
                                }
                            }
                            mean = mean / length;
                            rtgs.setRating(mean);
                            Log.d("Debug", "Mean value of ratings: " + rtgs.getRating());

                        } else Log.e("Error", "Query for shop list failed");
                    }
                });
    }

    @Override
    public int getItemCount() {
        return sellerModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName, shopAddress;
        ImageView shopImage;
        RatingBar starsReview;

        public ViewHolder(@NonNull View shopListView) {
            super(shopListView);

            shopName = shopListView.findViewById(R.id.titleView_shopList);
            shopAddress = shopListView.findViewById(R.id.addressView_shopList);
            starsReview = shopListView.findViewById(R.id.ratings_shopList);
            shopImage = shopListView.findViewById(R.id.imageView_shopList);
        //shopListView.findViewById(R.id.imageView_shopList);

        }
    }
}
