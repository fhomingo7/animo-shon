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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.Application.Models.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class AdminNewOrdersActivity extends AppCompatActivity {


    private RecyclerView ordersList;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));

        ImageButton backButton = (ImageButton)findViewById(R.id.admin_newOrders_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AdminNewOrdersActivity.this, AdminMenu.class);
                startActivity(i);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(ordersRef, AdminOrders.class).build();

        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull AdminNewOrdersActivity.AdminOrdersViewHolder adminOrdersViewHolder, int i, @NonNull @NotNull AdminOrders adminOrders) {
                adminOrdersViewHolder.username.setText("Name: " + adminOrders.getName());
                adminOrdersViewHolder.userphone.setText("Phone: " + adminOrders.getPhone());
                adminOrdersViewHolder.userstudentnumber.setText("Student Number: " + adminOrders.getStudentnumber());
                adminOrdersViewHolder.usertotalprice.setText("Total Amount: ₱" + adminOrders.getTotalAmount());
                adminOrdersViewHolder.userdatetime.setText("Orders at: " + adminOrders.getDate() + " "+ adminOrders.getTime());
                adminOrdersViewHolder.usershippingaddress.setText("Shipping Address: " + adminOrders.getAddress() + ", " +  adminOrders.getCity());

                adminOrdersViewHolder.ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String uID = getRef(i).getKey();

                        Intent i = new Intent (AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                        i.putExtra("uid", uID);
                        startActivity(i);
                    }
                });

            }

            @NonNull
            @NotNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                return new AdminOrdersViewHolder(view);

            }
        };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{

        public TextView username, userphone, usertotalprice, userdatetime, usershippingaddress, userstudentnumber;
        public Button ShowOrdersBtn;

        public AdminOrdersViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.order_userName);
            userphone = itemView.findViewById(R.id.order_phoneNumber);
            usertotalprice = itemView.findViewById(R.id.order_totalPrice);
            userdatetime = itemView.findViewById(R.id.order_date_time);
            usershippingaddress = itemView.findViewById(R.id.order_address);
            userstudentnumber = itemView.findViewById(R.id.order_studentnumber);
            ShowOrdersBtn = itemView.findViewById(R.id.order_button);
        }
    }
}