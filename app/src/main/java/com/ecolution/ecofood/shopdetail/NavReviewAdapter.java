package com.ecolution.ecofood.shopdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecolution.ecofood.R;
import com.ecolution.ecofood.model.ReviewModel;
import com.ecolution.ecofood.productdetail.NavItemAdapter;

import java.util.List;

public class NavReviewAdapter extends RecyclerView.Adapter<NavReviewAdapter.ViewHolder> {
    Context context;
    List<ReviewModel> reviewModelList;

    public NavReviewAdapter() { }

    public NavReviewAdapter(Context context, List<ReviewModel> reviewModelList) {
        this.context = context;
        this.reviewModelList = reviewModelList;
    }

    @NonNull
    @Override
    public NavReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NavReviewAdapter.ViewHolder holder, int position) {
        holder.reviewerName.setText(reviewModelList.get(position).getCustomer().getFirstName());
        holder.review.setText(reviewModelList.get(position).getDescription());
        holder.ratingBar.setRating(reviewModelList.get(position).getGrade());
    }

    @Override
    public int getItemCount() { return reviewModelList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RatingBar ratingBar;
        TextView reviewerName, review;

        public ViewHolder(@NonNull View reviewView) {
            super(reviewView);
            reviewerName = reviewView.findViewById(R.id.reviewer_name);
            review = reviewView.findViewById(R.id.review_tex);
            ratingBar = reviewView.findViewById(R.id.review_ratingbar);
        }
    }
}
