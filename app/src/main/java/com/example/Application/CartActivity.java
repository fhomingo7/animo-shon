package com.example.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.Application.Models.Cart;
import com.example.Application.Models.Products;
import com.example.Application.Models.Users;
import com.example.Application.Prevalent.Prevalent;
import com.example.Application.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button checkoutButton;
    private TextView totalAmount, textMsg1;
    private float totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_menu);

        ImageButton backButton = (ImageButton)findViewById(R.id.cart_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CartActivity.this, MainMenu.class);
                startActivity(i);

            }
        });

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        checkoutButton = (Button)findViewById(R.id.checkoutButton);
        totalAmount = (TextView)findViewById(R.id.totalAmount);
        textMsg1 = (TextView) findViewById(R.id.msg1);

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference cartListRef = FirebaseDatabase.getInstance()
                        .getReference().child("Cart List").child("User View");
                cartListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.child(Prevalent.currentOnlineUser.getStudentnumber()).exists()) {
                            Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                            intent.putExtra("Total Price", String.valueOf(totalPrice));
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Cart is still empty..", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) { }
                });

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View").child(Prevalent.currentOnlineUser.getStudentnumber()).child("Products")
                , Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart sample) {
                cartViewHolder.txtProductPrice.setText(sample.getPrice());
                cartViewHolder.txtProductName.setText(sample.getPname());
                cartViewHolder.productQuantity.setNumber(sample.getQuantity());
                cartViewHolder.productQuantity.setRange(1, Integer.parseInt(sample.getStock()));

                float productPrice = ((Float.parseFloat(sample.getPrice().replace("₱",""))))
                        * (Float.parseFloat(sample.getQuantity()));
                totalPrice += productPrice;
                totalAmount.setText(String.format("₱ %.2f", totalPrice));

                final DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference().child("Products");
                imageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.child(sample.getPid()).exists())
                        {
                            Products productData = dataSnapshot.child(sample.getPid()).getValue(Products.class);
                            Picasso.get().load(productData.getImage()).into(cartViewHolder.itemImage);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

                cartViewHolder.productQuantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                    @Override
                    public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
                        cartRef.child("User View").child(Prevalent.currentOnlineUser.getStudentnumber())
                                .child("Products").child(sample.getPid()).child("quantity").setValue(String.valueOf(newValue));
                        cartRef.child("Admin View").child(Prevalent.currentOnlineUser.getStudentnumber())
                                .child("Products").child(sample.getPid()).child("quantity").setValue(String.valueOf(newValue));
                        totalPrice -= ((Float.parseFloat(sample.getPrice().replace("₱",""))))
                                * (Float.parseFloat(sample.getQuantity()));
                    }
                });

                cartViewHolder.removeItemText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
                        cartRef.child("User View").child(Prevalent.currentOnlineUser.getStudentnumber())
                                .child("Products").child(sample.getPid()).removeValue();
                        cartRef.child("Admin View").child(Prevalent.currentOnlineUser.getStudentnumber())
                                .child("Products").child(sample.getPid()).removeValue();

                        float productPrice = ((Float.parseFloat(sample.getPrice().replace("₱",""))))
                                * (Float.parseFloat(sample.getQuantity()));
                        totalPrice -= productPrice;
                        totalAmount.setText(String.format("₱ %.2f", totalPrice));
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        totalAmount.setText("₱ " + totalPrice);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void CheckOrderState(){
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getStudentnumber());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String shippingState = snapshot.child("state").getValue().toString();
                    String username = snapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")){
                        totalAmount.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);

                        textMsg1.setVisibility(View.VISIBLE);
                        textMsg1.setText("Congratulations, your final order has been shipped successfully. Soon you will receive your order at your doorstep");
                        checkoutButton.setVisibility(View.GONE);

                    }
                    else if (shippingState.equals("not shipped")){
                        recyclerView.setVisibility(View.GONE);

                        textMsg1.setVisibility(View.VISIBLE);
                        checkoutButton.setVisibility(View.GONE);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
