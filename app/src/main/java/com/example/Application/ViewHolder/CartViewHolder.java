package com.example.Application.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.Application.Interface.ItemClickListener;
import com.example.Application.R;

import org.jetbrains.annotations.NotNull;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductPrice;
    public ElegantNumberButton productQuantity;
    private ItemClickListener itemClickListner;

    public CartViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.productName);
        txtProductPrice = itemView.findViewById(R.id.productPrice);
        productQuantity = itemView.findViewById(R.id.numberButton);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListener itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
