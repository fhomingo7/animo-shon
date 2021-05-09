package com.example.Application.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Application.Interface.ItemClickListener;
import com.example.Application.R;

import org.jetbrains.annotations.NotNull;

public class LikeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductPrice, removeItemText;
    private ItemClickListener itemClickListner;
    public ImageView itemImage;

    public LikeViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.productName1);
        txtProductPrice = itemView.findViewById(R.id.productPrice1);
        itemImage = itemView.findViewById(R.id.likeImage);
        removeItemText = itemView.findViewById(R.id.removeItem1);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListener itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
