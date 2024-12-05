package com.ecolution.ecofood.productdetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecolution.ecofood.R;

import java.util.List;

public class NavItemAdapter extends RecyclerView.Adapter<NavItemAdapter.ViewHolder> {
    //Context context;
    //List<>

    @NonNull
    @Override
    public NavItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_customer_product_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NavItemAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
