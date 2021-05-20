package com.example.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.Application.Models.Cart;
import com.example.Application.Models.Products;
import com.example.Application.Prevalent.Prevalent;
import com.example.Application.ViewHolder.LikeViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Likes extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.likes_menu);

        ImageButton homeButton = (ImageButton)findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Likes.this, MainMenu.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_right,R.anim.slide_left);
            }
        });

        ImageButton purchasesButton = (ImageButton)findViewById(R.id.purchasesButton);
        purchasesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Likes.this, Purchases.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_left,R.anim.slide_right);
            }
        });

        ImageButton meButton = (ImageButton)findViewById(R.id.meButton);
        meButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Likes.this, Me.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_left,R.anim.slide_right);
            }
        });

        recyclerView = findViewById(R.id.likedItems);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference likeListRef = FirebaseDatabase.getInstance().getReference().child("Like List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(likeListRef.child(Prevalent.currentOnlineUser.getStudentnumber()).child("Products")
                        , Cart.class).build();

        FirebaseRecyclerAdapter<Cart, LikeViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, LikeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull LikeViewHolder likeViewHolder, int i, @NonNull Cart sample) {
                likeViewHolder.txtProductPrice.setText(sample.getPrice());
                likeViewHolder.txtProductName.setText(sample.getPname());

                final DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference().child("Products");
                imageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.child(sample.getPid()).exists())
                        {
                            Products productData = dataSnapshot.child(sample.getPid()).getValue(Products.class);
                            Picasso.get().load(productData.getImage()).into(likeViewHolder.itemImage);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

                likeViewHolder.removeItemText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Like List");
                        cartRef.child(Prevalent.currentOnlineUser.getStudentnumber()).child("Products")
                                .child(sample.getPid()).removeValue();
                        Toast.makeText(Likes.this, "Removed from Liked Items.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public LikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.liked_items_layout, parent, false);
                LikeViewHolder holder = new LikeViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}
