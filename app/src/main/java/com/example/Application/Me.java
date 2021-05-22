package com.example.Application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import io.paperdb.Paper;

public class Me extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Paper.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_menu);



        

        ImageButton homeButton = (ImageButton)findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Me.this, MainMenu.class);
                startActivity(i);
            }
        });

        ImageButton purchasesButton = (ImageButton)findViewById(R.id.purchasesButton);
        purchasesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Me.this, Purchases.class);
                startActivity(i);
            }
        });

        ImageButton likesButton = (ImageButton)findViewById(R.id.likesButton);
        likesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Me.this, Likes.class);
                startActivity(i);
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

        Button profileButton = (Button)findViewById(R.id.myProfile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Me.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        Button aboutUsButton = (Button) findViewById(R.id.about_us);
        aboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Me.this, AboutUsActivity.class);
                startActivity(i);
            }
        });
        Button faqButton = (Button) findViewById(R.id.faq);
        faqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Me.this, FAQActivity.class);
                startActivity(i);
            }
        });

    }

}
