package com.example.Application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_menu);

        FloatingActionButton backButton2 = (FloatingActionButton)findViewById(R.id.backButton2);
        backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);
            }
        });

        Button loginButton = (Button)findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // CHANGE TO CODE FOR LOGGING IN
//                Intent i = new Intent(Register.this, MainActivity.class);
//                startActivity(i);
            }
        });
    }
}
