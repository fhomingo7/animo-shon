package com.example.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Application.Models.Cart;
import com.example.Application.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class Me extends AppCompatActivity {

    private TextView textContact;
    private TextView textAddress;
    private TextView textCard;
    private TextView textBankAcc;
    private Button me_editButton, how_to_use, about_us;

    public Me() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Paper.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_menu);

        initializeUI();

        // Buttons for Footer //
        ImageButton homeButton = (ImageButton)findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Me.this, MainMenu.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_right,R.anim.slide_left);
            }
        });

        ImageButton purchasesButton = (ImageButton)findViewById(R.id.purchasesButton);
        purchasesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Me.this, Purchases.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_right,R.anim.slide_left);
            }
        });

        ImageButton likesButton = (ImageButton)findViewById(R.id.likesButton);
        likesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Me.this, Likes.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_right,R.anim.slide_left);
            }
        });

        Button logoutButton = (Button)findViewById(R.id.log_out);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent i = new Intent(Me.this, MainActivity.class);
                startActivity(i);
            }
        });

        // Buttons for Me Menu //
        me_editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add xml
            }
        });

        how_to_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add xml
            }
        });

        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add xml
            }
        });


        super.onStart();
    }

    private void initializeUI(){
        TextView textName = (TextView) findViewById(R.id.textName);
        TextView textID = (TextView) findViewById(R.id.textID);
        textContact = (TextView)findViewById(R.id.textContact);
        textAddress = (TextView)findViewById(R.id.textAddress);
        textCard = (TextView)findViewById(R.id.textCard);
        textBankAcc = (TextView)findViewById(R.id.textBankAcc);
        me_editButton = (Button)findViewById(R.id.me_editButton);
        how_to_use = (Button)findViewById(R.id.how_to_use);
        about_us = (Button)findViewById(R.id.about_us);

        textName.setText(Prevalent.currentOnlineUser.getName());
        textID.setText(Prevalent.currentOnlineUser.getStudentnumber());
    }
}
