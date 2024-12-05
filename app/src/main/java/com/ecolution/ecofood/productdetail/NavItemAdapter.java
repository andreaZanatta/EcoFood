package com.ecolution.ecofood.productdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecolution.ecofood.R;
import com.ecolution.ecofood.model.ItemModel;

import java.util.List;

public class NavItemAdapter extends RecyclerView.Adapter<NavItemAdapter.ViewHolder> {
    Context context;
    List<ItemModel> list;
    String userType;

    public NavItemAdapter(Context context, List<ItemModel> list, String userType){
        this.context = context;
        this.list = list;
        this.userType = userType;
    }

    @NonNull
    @Override
    public NavItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if("customer".equals(userType)) view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_customer_product_detail, parent, false);
        else if("seller".equals(userType)) view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_seller_product_detail, parent, false);
        else view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_customer_product_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NavItemAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImage()).into(holder.imageView);
        holder.title.setText(list.get(position).getNome());
        holder.category.setText(list.get(position).getCategoria());
        holder.price.setText( String.format(Double.toString( list.get(position).getPrezzo())) );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, category, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
            title = itemView.findViewById(R.id.product_title);
            category = itemView.findViewById(R.id.product_description);
            price = itemView.findViewById(R.id.product_price);
        }
    }
}
