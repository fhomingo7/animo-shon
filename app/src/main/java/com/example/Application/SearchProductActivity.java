package com.example.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.Application.Models.Products;
import com.example.Application.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class SearchProductActivity extends AppCompatActivity {

    private Button searchProductButton;
    private EditText searchProductBar;
    private RecyclerView searchList;
    private String searchInput;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);


        searchProductBar = findViewById(R.id.searchProductBar);
        searchProductButton = findViewById(R.id.searchProductButton);
        searchList = findViewById(R.id.searchList);
        layoutManager = new LinearLayoutManager(this);

        searchProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput = searchProductBar.getText().toString().toLowerCase();

                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(reference, Products.class).build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ProductViewHolder productViewHolder, int i, @NonNull @NotNull Products products) {
                if (searchInput != null && products.getName().toLowerCase().contains(searchInput)){
                    productViewHolder.productName.setText(products.getName());
                    productViewHolder.productPrice.setText("₱ " + products.getPrice());
                    Picasso.get().load(products.getImage()).into(productViewHolder.productImage);

                    productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SearchProductActivity.this, Item.class);
                            intent.putExtra("Product ID", products.getPid());
                            startActivity(intent);
                        }
                    });
                }

                else {
                    productViewHolder.itemView.setVisibility(View.GONE);
                    productViewHolder.itemView.setEnabled(false);
                    productViewHolder.itemView.setActivated(false);
                    layoutManager.removeView(productViewHolder.itemView);
                    productViewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }
            }

            @NonNull
            @NotNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        searchList.setLayoutManager(layoutManager);
        searchList.setAdapter(adapter);
        adapter.startListening();

    }
}