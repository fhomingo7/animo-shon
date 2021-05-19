package com.example.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.Application.Models.Cart;
import com.example.Application.Models.Products;
import com.example.Application.ViewHolder.AdminCartViewHolder;
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

public class AdminUserProductsActivity extends AppCompatActivity {

    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;

    private String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        userID = getIntent().getStringExtra("uid");

        productsList = findViewById(R.id.admin_userProducts_list);
        productsList.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);

        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(userID).child("Products");

        ImageButton backButton = (ImageButton)findViewById(R.id.admin_userProducts_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AdminUserProductsActivity.this, AdminNewOrdersActivity.class);
                startActivity(i);

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef, Cart.class).build();

        FirebaseRecyclerAdapter<Cart, AdminCartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, AdminCartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull AdminCartViewHolder cartViewHolder, int i, @NonNull @NotNull Cart cart) {
                cartViewHolder.txtProductPrice.setText("Price: " + cart.getPrice());
                cartViewHolder.txtProductName.setText("Name: " + cart.getPname());
                cartViewHolder.txtProductQuantity.setText("Quantity: " + cart.getQuantity());

                final DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference().child("Products");
                imageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.child(cart.getPid()).exists())
                        {
                            Products productData = dataSnapshot.child(cart.getPid()).getValue(Products.class);
                            Picasso.get().load(productData.getImage()).into(cartViewHolder.itemImage);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }

            @NonNull
            @NotNull
            @Override
            public AdminCartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_cart_items_layout, parent, false);
                AdminCartViewHolder holder = new AdminCartViewHolder(view);
                return holder;
            }
        };

        productsList.setAdapter(adapter);
        adapter.startListening();
    }
}