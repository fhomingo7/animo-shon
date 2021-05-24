package com.example.Application;

import android.content.Intent;
import java.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.Application.Models.Products;
import com.example.Application.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Item extends AppCompatActivity {
    private String productID = "", state = "Normal";
    private Button addToCartButton;
    private ImageButton likeButton;
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
                Intent i = new Intent(Item.this, CartActivity.class);
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


                if (state.equals("Order Placed") || state.equals("Order Shipped")){
                    Toast.makeText(Item.this, "you can purchase more product once your order is shipped or confirmed", Toast.LENGTH_LONG).show();
                }
                else {
                    addingToCartList();
                }
            }
        });

        likeButton = (ImageButton)findViewById(R.id.likeItemButton);
        checkifLiked();

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference likeReferenceList = FirebaseDatabase.getInstance().getReference().child("Like List");
                likeReferenceList.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(Prevalent.currentOnlineUser.getStudentnumber()).child("Products").child(productID).exists()) {
                            likeButton.setImageResource(R.drawable.button_liked_green);
                            removeFromLikeList();
                            checkifLiked();
                        }
                        else {
                            likeButton.setImageResource(R.drawable.button_like);
                            addingToLikeList();
                            checkifLiked();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }
        });
    }

    private void checkifLiked(){
        final DatabaseReference likeReferenceList = FirebaseDatabase.getInstance().getReference().child("Like List");
        likeReferenceList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Prevalent.currentOnlineUser.getStudentnumber()).child("Products").child(productID).exists()) {
                    likeButton.setImageResource(R.drawable.button_liked_green);
                }
                else{
                    likeButton.setImageResource(R.drawable.button_like);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void removeFromLikeList(){
        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Like List");
        cartRef.child(Prevalent.currentOnlineUser.getStudentnumber()).child("Products")
                .child(productID).removeValue();
        Toast.makeText(Item.this, "Removed from Liked Items.", Toast.LENGTH_SHORT).show();
    }

    private void addingToLikeList(){
        String saveCurrentTime, saveCurrentDate;

        Calendar calendarForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendarForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendarForDate.getTime());

        final DatabaseReference likeReferenceList = FirebaseDatabase.getInstance().getReference().child("Like List");

        final HashMap<String, Object> likeMap = new HashMap<>();
        likeMap.put("pid", productID);
        likeMap.put("pname", item_name.getText().toString());
        likeMap.put("price", item_price.getText().toString());
        likeMap.put("date", saveCurrentDate);
        likeMap.put("time", saveCurrentTime);
        likeMap.put("quantity", numberButton.getNumber());
        likeMap.put("stock", item_stock.getText().toString());
        likeMap.put("brand", item_brand.getText().toString());
        likeMap.put("description", item_description.getText().toString());

        likeReferenceList.child(Prevalent.currentOnlineUser.getStudentnumber()).child("Products").child(productID).updateChildren(likeMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Item.this, "Added to Liked Items.", Toast.LENGTH_SHORT).show();
                }
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

        cartReferenceList.child("User View").child(Prevalent.currentOnlineUser.getStudentnumber()).child("Products").child(productID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()){
                    cartReferenceList.child("Admin View").child(Prevalent.currentOnlineUser.getStudentnumber()).child("Products").child(productID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Item.this, "Added to Cart List.", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Item.this, MainMenu.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
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

                    if (Integer.valueOf(products.getStock()) == 0){
                        numberButton.setNumber("0");
                        numberButton.setVisibility(View.INVISIBLE);
                        addToCartButton.setText("Sold Out");
                        addToCartButton.setEnabled(false);
                    }
                    else {
                        numberButton.setRange(1, Integer.valueOf(products.getStock()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();
    }

    private void CheckOrderState(){
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getStudentnumber());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String shippingState = snapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped")){
                        state = "Order Shipped";

                    }
                    else if (shippingState.equals("not shipped")){
                        state = "Order Placed";
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
