package com.example.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEdit, phoneEdit, addressEdit, cityEdit, studentIDEdit;
    private Button confirmBtn;
    private RadioButton gcash, cashondelivery;

    private String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price =  ₱ " + totalAmount, Toast.LENGTH_SHORT).show();

        confirmBtn = (Button)findViewById(R.id.shipment_confirmBtn);
        nameEdit = (EditText)findViewById(R.id.shipment_name);
        phoneEdit = (EditText)findViewById(R.id.shipment_phone_number);
        addressEdit = (EditText)findViewById(R.id.shipment_address);
        cityEdit = (EditText)findViewById(R.id.shipment_city);
        studentIDEdit = (EditText)findViewById(R.id.shipment_studentID);
        gcash = (RadioButton)findViewById(R.id.GCash);
        cashondelivery = (RadioButton)findViewById(R.id.CashOnDelivery);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });
        final DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getStudentnumber());
        imageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                nameEdit.setText(dataSnapshot.child("name").getValue(String.class));
                addressEdit.setText(dataSnapshot.child("address").getValue(String.class));
                phoneEdit.setText(dataSnapshot.child("phonenumber").getValue(String.class));
                cityEdit.setText(dataSnapshot.child("city").getValue(String.class));
                studentIDEdit.setText(dataSnapshot.child("studentnumber").getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void Check() {
        if (TextUtils.isEmpty(nameEdit.getText().toString())){
            Toast.makeText(this, "Please provide your name.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneEdit.getText().toString())){
            Toast.makeText(this, "Please provide your phone number.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEdit.getText().toString())){
            Toast.makeText(this, "Please provide your address.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cityEdit.getText().toString())){
            Toast.makeText(this, "Please provide your city.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(studentIDEdit.getText().toString())){
            Toast.makeText(this, "Please provide your Student ID.", Toast.LENGTH_SHORT).show();
        }
        else if (!gcash.isChecked() && !cashondelivery.isChecked()){
            Toast.makeText(this, "Please choose your delivery method", Toast.LENGTH_SHORT).show();
        }
        else {
            confirmOrder();
        }
    }

    private void confirmOrder() {
        final String saveCurrentTime, saveCurrentDate;

        Calendar calendarForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendarForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendarForDate.getTime());

        final DatabaseReference ordersReference = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getStudentnumber());
        final DatabaseReference historyReference = FirebaseDatabase.getInstance().getReference().child("History").child(Prevalent.currentOnlineUser.getStudentnumber());
        final DatabaseReference detailsReference = FirebaseDatabase.getInstance().getReference().child("History Details").child(Prevalent.currentOnlineUser.getStudentnumber());

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name", nameEdit.getText().toString());
        ordersMap.put("phone", phoneEdit.getText().toString());
        ordersMap.put("studentnumber", studentIDEdit.getText().toString());
        ordersMap.put("address", addressEdit.getText().toString());
        ordersMap.put("city", cityEdit.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "not shipped");

        ordersReference.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(Prevalent.currentOnlineUser.getStudentnumber()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ConfirmFinalOrderActivity.this, "Your final order has been placed successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ConfirmFinalOrderActivity.this, MainMenu.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });

        historyReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int orderNumber = 0;
                for (int i = 0; i < snapshot.getChildrenCount(); i++){
                    orderNumber += 1;
                }

                historyReference.child(String.valueOf("Order " + orderNumber)).updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        detailsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int orderNumber = 0;
                for (int i = 0; i < snapshot.getChildrenCount(); i++){
                    orderNumber += 1;
                }

                String userID = String.valueOf(Prevalent.currentOnlineUser.getStudentnumber());
                final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(userID).child("Products");

                int finalOrderNumber = orderNumber;
                cartListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        int productCount = 0;
                            for(DataSnapshot ds : snapshot.getChildren()) {
                                HashMap<String, Object> items = new HashMap<>();
                                items.put("pname", ds.child("pname").getValue(String.class));
                                items.put("quantity", ds.child("quantity").getValue(String.class));
                                items.put("price", ds.child("price").getValue(String.class));
                                items.put("image", ds.child("price").getValue(String.class));
                                detailsReference.child(String.valueOf("Order " + finalOrderNumber)).child(String.valueOf("Item " + productCount)).updateChildren(items).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                                    }
                                });
                                productCount += 1;
                            }

                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        final DatabaseReference updateStock = FirebaseDatabase.getInstance().getReference().child("Products");
        updateStock.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                String userID = String.valueOf(Prevalent.currentOnlineUser.getStudentnumber());
                final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference()
                        .child("Cart List").child("Admin View").child(userID).child("Products");

                for (DataSnapshot ds1 : snapshot.getChildren()){
                    cartListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for(DataSnapshot ds : snapshot.getChildren()) {
                                if (ds.child("pname").getValue(String.class).equals(ds1.child("name").getValue(String.class))){
                                    int initialValue = Integer.valueOf(ds1.child("stock").getValue(String.class));
                                    int minusValue = Integer.valueOf(ds.child("quantity").getValue(String.class));
                                    int finalValue = initialValue - minusValue;
                                    updateStock.child(ds1.getKey()).child("stock").setValue(String.valueOf(finalValue));
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }

            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}