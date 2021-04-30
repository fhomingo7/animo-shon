package com.example.Application.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Application.Interface.ItemClickListener;
import com.example.Application.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView productName, productPrice;
    public ImageView productImage;
    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        productImage = (ImageView) itemView.findViewById(R.id.itemImage);
        productName = (TextView) itemView.findViewById(R.id.itemName);
        productPrice = (TextView) itemView.findViewById(R.id.itemPrice);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
