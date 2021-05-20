package com.example.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.Application.Models.AdminOrders;
import com.example.Application.Models.Cart;
import com.example.Application.Models.Products;
import com.example.Application.Models.UserHistory;
import com.example.Application.Prevalent.Prevalent;
import com.example.Application.ViewHolder.CartViewHolder;
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

import org.jetbrains.annotations.NotNull;

public class Purchases extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference historyRef;
    final int[] orderNumber = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchases_menu);

        historyRef = FirebaseDatabase.getInstance().getReference()
                .child("History").child(Prevalent.currentOnlineUser.getStudentnumber());

        ImageButton homeButton = (ImageButton)findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Purchases.this, MainMenu.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_right,R.anim.slide_left);
            }
        });

        ImageButton likesButton = (ImageButton)findViewById(R.id.likesButton);
        likesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Purchases.this, Likes.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_right,R.anim.slide_left);
            }
        });

        ImageButton meButton = (ImageButton)findViewById(R.id.meButton);
        meButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Purchases.this, Me.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_left,R.anim.slide_right);
            }
        });

        recyclerView = findViewById(R.id.purchase_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<UserHistory> options =
                new FirebaseRecyclerOptions.Builder<UserHistory>().
                        setQuery(historyRef, UserHistory.class).build();

        FirebaseRecyclerAdapter<UserHistory, AdminNewOrdersActivity.AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<UserHistory, AdminNewOrdersActivity.AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull AdminNewOrdersActivity.AdminOrdersViewHolder adminOrdersViewHolder, int i, @NonNull @NotNull UserHistory adminOrders) {
                historyRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        adminOrders.setName(snapshot.getValue(String.class));
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) { }
                });

                historyRef.child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        adminOrders.setPhone(snapshot.getValue(String.class));
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) { }
                });

                historyRef.child("studentnumber").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        adminOrders.setStudentnumber(snapshot.getValue(String.class));
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) { }
                });

                historyRef.child("totalAmount").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        adminOrders.setTotalAmount(snapshot.getValue(String.class));
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) { }
                });

                historyRef.child("date").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        adminOrders.setDate(snapshot.getValue(String.class));
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) { }
                });

                historyRef.child("time").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        adminOrders.setTime(snapshot.getValue(String.class));
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) { }
                });

                historyRef.child("address").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        adminOrders.setAddress(snapshot.getValue(String.class));
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) { }
                });

                historyRef.child("city").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        adminOrders.setCity(snapshot.getValue(String.class));
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) { }
                });

                historyRef.child("state").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        adminOrders.setState(snapshot.getValue(String.class));
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) { }
                });

                adminOrdersViewHolder.username.setText("Name: " + adminOrders.getName());
                adminOrdersViewHolder.userphone.setText("Phone: " + adminOrders.getPhone());
                adminOrdersViewHolder.userstudentnumber.setText("Student Number: " + adminOrders.getStudentnumber());
                adminOrdersViewHolder.usertotalprice.setText("Total Amount: ₱" + adminOrders.getTotalAmount());
                adminOrdersViewHolder.userdatetime.setText("Orders at: " + adminOrders.getDate() + " "+ adminOrders.getTime());
                adminOrdersViewHolder.usershippingaddress.setText("Shipping Address: " + adminOrders.getAddress() + ", " +  adminOrders.getCity());
                adminOrdersViewHolder.state.setText("State: " + adminOrders.getState());

                String orderNumber = getRef(i).getKey();
                adminOrdersViewHolder.ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String uID = getRef(i).getKey();
                        Intent i = new Intent (Purchases.this, UserHistoryDetails.class);
                        i.putExtra("uid", uID);
                        i.putExtra("order", orderNumber);
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @NotNull
            @Override
            public AdminNewOrdersActivity.AdminOrdersViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                return new AdminNewOrdersActivity.AdminOrdersViewHolder(view);

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
