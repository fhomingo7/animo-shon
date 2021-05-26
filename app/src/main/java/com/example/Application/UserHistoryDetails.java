package com.example.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.Application.Models.Cart;
import com.example.Application.Models.HistoryDetails;
import com.example.Application.Models.Products;
import com.example.Application.Models.UserHistory;
import com.example.Application.Prevalent.Prevalent;
import com.example.Application.ViewHolder.AdminCartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class UserHistoryDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference detailsRef;

    private String userID = "";
    private String orderNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_history_details);

        recyclerView = findViewById(R.id.user_ProductsList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        userID = getIntent().getStringExtra("uid");
        orderNumber = getIntent().getStringExtra("order");
        detailsRef = FirebaseDatabase.getInstance().getReference().child("History Details")
                .child(Prevalent.currentOnlineUser.getStudentnumber()).child(orderNumber);

        ImageButton backButton = (ImageButton) findViewById(R.id.userProducts_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(UserHistoryDetails.this, Purchases.class);
                startActivity(i);

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<HistoryDetails> options =
                new FirebaseRecyclerOptions.Builder<HistoryDetails>().
                        setQuery(detailsRef, HistoryDetails.class).build();

        FirebaseRecyclerAdapter<HistoryDetails, AdminCartViewHolder> adapter = new FirebaseRecyclerAdapter<HistoryDetails, AdminCartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull AdminCartViewHolder cartViewHolder, int i, @NonNull @NotNull HistoryDetails cart) {
                detailsRef.child("pname").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        cart.setPname(snapshot.getValue(String.class));
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) { }
                });
                detailsRef.child("price").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        cart.setPrice(snapshot.getValue(String.class));
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) { }
                });
                detailsRef.child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        cart.setQuantity(snapshot.getValue(String.class));
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) { }
                });
                cartViewHolder.txtProductName.setText("Name: " + cart.getPname());
                cartViewHolder.txtProductPrice.setText("Price: " + cart.getPrice());
                cartViewHolder.txtProductQuantity.setText("Quantity: " + cart.getQuantity());

                String name = cart.getPname();
                final DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference().child("Products");
                imageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            System.out.println("HELLO" + ds.child("name").getValue());
                            System.out.println(name);
                            if (ds.child("name").getValue().equals(name)) {
                                Picasso.get().load(String.valueOf(ds.child("image").getValue())).into(cartViewHolder.itemImage);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
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

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}