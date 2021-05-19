package com.example.Application.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.Application.Interface.ItemClickListener;
import com.example.Application.R;

import org.jetbrains.annotations.NotNull;

public class AdminCartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    private ItemClickListener itemClickListner;
    public ImageView itemImage;

    public AdminCartViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        txtProductPrice = itemView.findViewById(R.id.admin_productPrice);
        txtProductName = itemView.findViewById(R.id.admin_productName);
        txtProductQuantity = itemView.findViewById(R.id.admin_productQuantity);
        itemImage = itemView.findViewById(R.id.cartImage);

    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListener itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
