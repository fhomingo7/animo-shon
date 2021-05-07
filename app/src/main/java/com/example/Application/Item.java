package com.example.Application;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.Application.Models.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Item extends AppCompatActivity {
    private String productID = "";
    private Button addToCartButton;
    private ElegantNumberButton numberButton;
    private TextView item_name, item_price, item_stock, item_brand, item_description;
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
        item_brand = (TextView) findViewById(R.id.item_brand);
        item_description = (TextView) findViewById(R.id.item_description);
        item_image1 = (ImageView) findViewById(R.id.item_image1);
        addToCartButton = (Button) findViewById(R.id.addToCartButton);
        numberButton = (ElegantNumberButton) findViewById(R.id. numberButton);

        getProductDetails(productID);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addingToCartList() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calendarForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendarForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendarForDate.getTime());

        final DatabaseReference cartReferenceList = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", item_name.getText().toString());
        cartMap.put("price", item_price.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("stock", item_stock.getText().toString());
        cartMap.put("brand", item_brand.getText().toString());
        cartMap.put("description", item_description.getText().toString());

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
                    item_brand.setText(products.getBrand());
                    Picasso.get().load(products.getImage()).into(item_image1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
