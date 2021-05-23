package com.example.Application;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.Application.Models.Products;
import com.example.Application.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainMenu extends AppCompatActivity {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ImageButton CheckOrdersButton, HomeButton, logoutButton, editButton;

    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_menu);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ImageButton cartButton = (ImageButton)findViewById(R.id.cartButton);

        if (bundle != null){
            ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

            type = getIntent().getExtras().get("Admin").toString();
            cartButton.setVisibility(View.INVISIBLE);
            CheckOrdersButton = (ImageButton) findViewById(R.id.purchasesButton);
            HomeButton = (ImageButton) findViewById(R.id.homeButton);
            logoutButton = (ImageButton) findViewById(R.id.meButton);
            editButton = (ImageButton) findViewById(R.id.likesButton);
            CheckOrdersButton.setImageResource(R.drawable.button_deliveries_black);
            HomeButton.setImageResource(R.drawable.button_home_black);
            logoutButton.setImageResource(R.drawable.button_logout_filled);
            editButton.setImageResource(R.drawable.button_products_green);

            CheckOrdersButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainMenu.this, AdminNewOrdersActivity.class);
                    startActivity(i);
                }
            });

            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainMenu.this, MainActivity.class);
                    startActivity(i);
                }
            });

            HomeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainMenu.this, AdminMenu.class);
                    startActivity(i);
                }
            });
            android.widget.SearchView search = (android.widget.SearchView) findViewById(R.id.searchBar);
            search.setVisibility(View.INVISIBLE);
        }

        else {
            ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

            ImageButton purchasesButton = (ImageButton)findViewById(R.id.purchasesButton);
            purchasesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainMenu.this, Purchases.class);
                    startActivity(i);

                }
            });

            ImageButton likesButton = (ImageButton)findViewById(R.id.likesButton);
            likesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainMenu.this, Likes.class);
                    startActivity(i);

                }
            });

            ImageButton meButton = (ImageButton)findViewById(R.id.meButton);
            meButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainMenu.this, Me.class);
                    startActivity(i);

                }
            });

            cartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainMenu.this, CartActivity.class);
                    startActivity(i);
                }
            });
            android.widget.SearchView search = (android.widget.SearchView) findViewById(R.id.searchBar);
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainMenu.this, SearchProductActivity.class);
                    startActivity(i);
                }
            });
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef, Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {
                        productViewHolder.productName.setText(products.getName());
                        productViewHolder.productPrice.setText("₱ " + products.getPrice());
                        Picasso.get().load(products.getImage()).into(productViewHolder.productImage);

                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (type.equals("Admin")){
                                    Intent intent = new Intent(MainMenu.this, AdminEditProductsActivity.class);
                                    intent.putExtra("Product ID", products.getPid());
                                    startActivity(intent);

                                }
                                else{

                                    Intent intent = new Intent(MainMenu.this, Item.class);
                                    intent.putExtra("Product ID", products.getPid());
                                    startActivity(intent);
                                }

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };

            recyclerView.setAdapter(adapter);
            adapter.startListening();
    }

}
