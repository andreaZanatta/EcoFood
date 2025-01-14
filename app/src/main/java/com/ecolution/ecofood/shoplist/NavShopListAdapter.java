package com.ecolution.ecofood.shoplist;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecolution.ecofood.LoginActivity;
import com.ecolution.ecofood.R;
import com.ecolution.ecofood.model.ReviewModel;
import com.ecolution.ecofood.model.SellerModel;
import com.ecolution.ecofood.model.UserModel;
import com.ecolution.ecofood.productdetail.ProductListActivity;
import com.ecolution.ecofood.shopdetail.NavReviewAdapter;
import com.ecolution.ecofood.shopdetail.ShopDetailsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class NavShopListAdapter extends RecyclerView.Adapter<NavShopListAdapter.ViewHolder> {
    Context context;
    List<SellerModel> sellerModelList;
    String currentUserId;
    List<String> favouriteShops;
    FirebaseFirestore db;

    public NavShopListAdapter(Context context, List<SellerModel> sellerList, List<String> shops, String currentUser){
        this.context = context;
        this.sellerModelList = sellerList;
        this.favouriteShops = shops;
        this.currentUserId = currentUser;

        db = FirebaseFirestore.getInstance();
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
                .placeholder(R.drawable.icona)
                .error(R.drawable.shopimage)
                .into(holder.shopImage);

        holder.shopCard.setOnClickListener(v -> {
            Intent intent = new Intent(context, ShopDetailsActivity.class);
            intent.putExtra("shopId", shop.getUser_id());
            intent.putExtra("shopName", shop.getShopName());
            intent.putExtra("from", "list");
            context.startActivity(intent);
        });

        if(favouriteShops != null) {
            if (checkIfInList(shop.getUser_id())) {
                holder.favourites.setImageResource(R.drawable.fav);
                holder.favourites.setOnClickListener(v -> {
                    holder.favourites.setImageResource(R.drawable.notfav);
                    updateFavourites(shop.getUser_id(), false);
                    notifyItemChanged(position);
                });
            } else {
                holder.favourites.setImageResource(R.drawable.notfav);
                holder.favourites.setOnClickListener(v -> {
                    holder.favourites.setImageResource(R.drawable.fav);
                    updateFavourites(shop.getUser_id(), true);
                    notifyItemChanged(position);
                });
            }
        } else {
            holder.favourites.setVisibility(View.GONE);
        }


                /*-> {

            if(checkIfInList(shop.getUser_id())) {
                holder.favourites.setImageResource(R.drawable.fav);
                Log.d("Debug", "check if in list: " + true);
            }
            else {
                holder.favourites.setImageResource(R.drawable.notfav);
                Log.d("Debug", "check if in list: " + false);
            }
            //holder.favourites.setImageResource();
        });*/
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

    private void updateFavourites(String shopId, boolean toAdd){
        if(toAdd)
            favouriteShops.add(shopId);
        else
            favouriteShops.remove(shopId);

        db.collection("users").document(currentUserId)
                .update("favourites", favouriteShops)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Product updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to update product", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    private boolean checkIfInList(String shopId) {
        Log.wtf("Debug", "Is List null? " + favouriteShops);
        for (String s : favouriteShops) Log.d("Debug", "Look at favourite: " + s);
        return favouriteShops.contains(shopId);
    }

    @Override
    public int getItemCount() {
        return sellerModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName, shopAddress;
        ImageView shopImage;
        RatingBar starsReview;
        LinearLayout shopCard;
        ImageButton favourites;

        public ViewHolder(@NonNull View shopListView) {
            super(shopListView);

            shopName = shopListView.findViewById(R.id.titleView_shopList);
            shopAddress = shopListView.findViewById(R.id.addressView_shopList);
            starsReview = shopListView.findViewById(R.id.ratings_shopList);
            shopImage = shopListView.findViewById(R.id.imageView_shopList);
            shopCard = shopListView.findViewById(R.id.card_for_shop_list);
            favourites = shopListView.findViewById(R.id.favourite_button);
        }
    }
}
