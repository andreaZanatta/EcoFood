package com.ecolution.ecofood.productdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecolution.ecofood.R;
import com.ecolution.ecofood.model.ItemModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NavItemAdapter extends RecyclerView.Adapter<NavItemAdapter.ViewHolder> {
    Context context;
    List<ItemModel> list;
    boolean isSeller;

    public NavItemAdapter(Context context, List<ItemModel> list, boolean userType){
        this.context = context;
        this.list = list;
        this.isSeller = userType;
    }

    @NonNull
    @Override
    public NavItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(isSeller) view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_seller_product_detail  , parent, false);
        else         view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_customer_product_detail, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NavItemAdapter.ViewHolder holder, int position) {
        ItemModel item = list.get(position);

        Glide.with(context).load(list.get(position).getImage()).placeholder(R.drawable.icona).error(R.drawable.icona).into(holder.imageView);
        holder.title.setText(list.get(position).getNome());
        holder.category.setText(list.get(position).getCategoria());
        holder.price.setText(String.format("%s€", String.format(Double.toString(list.get(position).getPrezzo()))));

        holder.dashboardItem.setOnClickListener(v -> {
            showDialog(item);
        });
    }

    private void showDialog(ItemModel it) {
        // Inflate the dialog layout

        if (isSeller) {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_item_page, null);
            // Initialize the dialog
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setView(dialogView);

            EditText editProductName = dialogView.findViewById(R.id.edit_product_name);
            EditText editProductCategory = dialogView.findViewById(R.id.edit_product_category);
            EditText editProductPrice = dialogView.findViewById(R.id.edit_product_price);
            EditText editProductDescription = dialogView.findViewById(R.id.edit_product_description);
            Button saveButton = dialogView.findViewById(R.id.save_button);

            editProductName.setText(it.getNome());
            editProductCategory.setText(it.getCategoria());
            editProductPrice.setText(String.valueOf(it.getPrezzo()));
            editProductDescription.setText(it.getDescrizione());

            android.app.AlertDialog dialog = builder.create();
            dialog.show();

            saveButton.setOnClickListener(v -> {
                // Collect updated data
                String updatedName = editProductName.getText().toString();
                String updatedCategory = editProductCategory.getText().toString();
                double updatedPrice = Double.parseDouble(editProductPrice.getText().toString());
                String updatedDescription = editProductDescription.getText().toString();

                // Save changes to database

                saveProductData(it.getId(), updatedName, updatedCategory, updatedPrice);

                // Update local list and notify adapter
                it.setNome(updatedName);
                it.setCategoria(updatedCategory);
                it.setPrezzo(updatedPrice);
                notifyDataSetChanged();

                // Close dialog
                dialog.dismiss();
            });
        } else {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.customer_dialog_item_page, null);
            // Initialize the dialog
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setView(dialogView);
            TextView productName = dialogView.findViewById(R.id.product_name);
            TextView productDescription = dialogView.findViewById(R.id.product_description);

            productName.setText(it.getNome());
            productDescription.setText(it.getDescrizione());

            android.app.AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void saveProductData(String productId, String name, String category, double price) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products").document(productId)
                .update("nome", name, "categoria", category, "prezzo", price)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Product updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to update product", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, category, price, description;
        ImageButton dashboardItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
            title = itemView.findViewById(R.id.product_title);
            category = itemView.findViewById(R.id.product_description);
            price = itemView.findViewById(R.id.product_price);

            if(isSeller) dashboardItem = itemView.findViewById(R.id.sellerEditButton);
            else dashboardItem = itemView.findViewById(R.id.infoButton);
        }
    }
}
