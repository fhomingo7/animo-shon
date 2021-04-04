package com.example.Application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_menu);

        FloatingActionButton backButton = (FloatingActionButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this, MainActivity.class);
                startActivity(i);
            }
        });

        Button registerButton = (Button)findViewById(R.id.nextButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // CHANGE TO CODE FOR REGISTERING
//                Intent i = new Intent(Register.this, MainActivity.class);
//                startActivity(i);
            }
        });
    }
}