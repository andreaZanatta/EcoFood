package com.ecolution.ecofood.shopdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecolution.ecofood.R;
import com.ecolution.ecofood.model.ReviewModel;


import java.util.List;

public class NavReviewAdapter extends RecyclerView.Adapter<NavReviewAdapter.ViewHolder> {
    private Context context;
    private List<ReviewModel> reviewModelList;

    public NavReviewAdapter(Context context, List<ReviewModel> revs){
        this.context = context;
        this.reviewModelList = revs;
    }

    @NonNull
    @Override
    public NavReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card, parent, false);
        return new NavReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NavReviewAdapter.ViewHolder holder, int position) {
        ReviewModel review = reviewModelList.get(position);

        // TODO: getCustomer returns customer Id. I want Customer Name
        holder.reviewName.setText(reviewModelList.get(position).getCustomer());
        holder.reviewStars.setRating(reviewModelList.get(position).getGrade());
        holder.reviewText.setText(reviewModelList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return reviewModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewName, reviewText;
        RatingBar reviewStars;

        public ViewHolder(@NonNull View reviewListView) {
            super(reviewListView);

            reviewName = reviewListView.findViewById(R.id.reviewer_name);
            reviewText = reviewListView.findViewById(R.id.review_tex);
            reviewStars = reviewListView.findViewById(R.id.review_ratingbar);
        }
    }
}


/*
Context context;
List<ReviewModel> reviewModelList;

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
    holder.reviewerName.setText(reviewModelList.get(position).getCustomer());
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
} */