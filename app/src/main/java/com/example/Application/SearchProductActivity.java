package com.example.Application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.Application.Models.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SearchProductActivity extends AppCompatActivity {

    private Button searchProductButton;
    private EditText searchProductBar;
    private RecyclerView searchList;
    private String searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);


        searchProductBar = findViewById(R.id.searchProductBar);
        searchProductButton = findViewById(R.id.searchProductButton);
        searchList = findViewById(R.id.searchList);
        searchList.setLayoutManager(new LinearLayoutManager(SearchProductActivity.this));

        searchProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput = searchProductBar.getText().toString();

                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(reference.orderByChild("name"), Products.class).build();

    }
}