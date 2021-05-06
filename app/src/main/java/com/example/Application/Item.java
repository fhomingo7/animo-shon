package com.example.Application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Application.Models.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Item extends AppCompatActivity {
    private String productID = "";
    private Button addToCartButton;
    private TextView item_name, item_price, item_stock, item_warranty, item_brand, item_description;
    private ImageView item_image1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_item);

        productID = getIntent().getStringExtra("Product ID");

        ImageButton item_backButton = (ImageButton)findViewById(R.id.item_backButton);
        item_backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Item.this, MainMenu.class);
                startActivity(i);
            }
        });

        ImageButton cartButton = (ImageButton)findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Item.this, Cart.class);
                startActivity(i);
            }
        });

        item_name = (TextView) findViewById(R.id.item_name);
        item_price = (TextView) findViewById(R.id.item_price);
        item_stock = (TextView) findViewById(R.id.item_stock);
        item_warranty = (TextView) findViewById(R.id.item_warranty);
        item_brand = (TextView) findViewById(R.id.item_brand);
        item_description = (TextView) findViewById(R.id.item_description);
        item_image1 = (ImageView) findViewById(R.id.item_image1);
        addToCartButton = (Button) findViewById(R.id.addToCartButton);

        getProductDetails(productID);
    }

    private void getProductDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Products products = snapshot.getValue(Products.class);
                    item_name.setText(products.getName());
                    item_price.setText("₱" + products.getPrice());
                    item_stock.setText(products.getStock());
                    item_description.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(item_image1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
