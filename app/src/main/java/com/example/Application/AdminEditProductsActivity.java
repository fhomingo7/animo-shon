package com.example.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.util.HashMap;

public class AdminEditProductsActivity extends AppCompatActivity {

    private Button applyChangesButton, deleteButton;
    private EditText name, price, description, stock, brand;
    private ImageView imageView;
    private String productID = "";

    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_products);

        productID = getIntent().getStringExtra("Product ID");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        applyChangesButton = findViewById(R.id.admin_editProductButton);
        deleteButton = findViewById(R.id.admin_delete);
        name = findViewById(R.id.admin_itemName);
        price = findViewById(R.id.admin_itemPrice);
        description = findViewById(R.id.admin_itemDescription);
        stock = findViewById(R.id.admin_itemStock);
        brand = findViewById(R.id.admin_itemBrand);
        imageView = findViewById(R.id.admin_itemImage);

        displaySpecificProductInfo();

        applyChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });
    }

    private void deleteItem(){
        productsRef.removeValue();

        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View");
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    final DatabaseReference cartRef1 = FirebaseDatabase.getInstance().getReference().
                            child("Cart List").child("User View").child(ds.getKey())
                            .child("Products");
                    cartRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(productID)){
                                cartRef1.child(productID).removeValue();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) { }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });

        cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View");
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    final DatabaseReference cartRef1 = FirebaseDatabase.getInstance().getReference().
                            child("Cart List").child("Admin View").child(ds.getKey())
                            .child("Products");
                    cartRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(productID)){
                                cartRef1.child(productID).removeValue();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) { }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });

        cartRef = FirebaseDatabase.getInstance().getReference().child("Like List");
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    final DatabaseReference cartRef1 = FirebaseDatabase.getInstance().getReference().
                            child("Like List").child(ds.getKey()).child("Products");
                    cartRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(productID)){
                                cartRef1.child(productID).removeValue();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) { }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });

        Toast.makeText(AdminEditProductsActivity.this, "Item is Deleted.", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(AdminEditProductsActivity.this, AdminMenu.class);
        startActivity(i);
    }

    private void applyChanges() {

        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription = description.getText().toString();
        String pStock = stock.getText().toString();
        String pBrand = brand.getText().toString();

        if (pName.equals("")){
            Toast.makeText(this, "Write down Product Name", Toast.LENGTH_SHORT).show();
        }
        else if (pPrice.equals("")){
            Toast.makeText(this, "Write down Product Price", Toast.LENGTH_SHORT).show();
        }
        else if (pDescription.equals("")){
            Toast.makeText(this, "Write down Product Description", Toast.LENGTH_SHORT).show();
        }
        else if (pStock.equals("")){
            Toast.makeText(this, "Write down Product Stock", Toast.LENGTH_SHORT).show();
        }
        else if (pBrand.equals("")){
            Toast.makeText(this, "Write down Product Brand", Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("description", pDescription);
            productMap.put("price", pPrice);
            productMap.put("name", pName);
            productMap.put("stock", pStock);
            productMap.put("brand", pBrand);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AdminEditProductsActivity.this, "Changes Applied Successfully", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(AdminEditProductsActivity.this, AdminMenu.class);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }
    }

    private void displaySpecificProductInfo() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String pName = snapshot.child("name").getValue().toString();
                    String pPrice = snapshot.child("price").getValue().toString();
                    String pDescription = snapshot.child("description").getValue().toString();
                    String pStock = snapshot.child("stock").getValue().toString();
                    String pBrand = snapshot.child("brand").getValue().toString();
                    String pImage = snapshot.child("image").getValue().toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);
                    stock.setText(pStock);
                    brand.setText(pBrand);
                    Picasso.get().load(pImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}