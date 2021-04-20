package com.example.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private EditText emailAddress2, password2;
    private TextView admin;
    private TextView notAdmin;
    private String parentDbName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_menu);

        initializeUI();

        FloatingActionButton backButton2 = (FloatingActionButton)findViewById(R.id.backButton2);
        backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);
            }
        });

        Button loginButton = (Button)findViewById(R.id.login);
        parentDbName = "User";
        admin = (TextView)findViewById(R.id.admin);
        notAdmin = (TextView)findViewById(R.id.notAdmin);
        notAdmin.setVisibility(View.GONE);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin.setVisibility(View.GONE);
                notAdmin.setVisibility(View.VISIBLE);
                loginButton.setText("Admin Log-in");
                parentDbName = "Admins";

            }
        });

        notAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notAdmin.setVisibility(View.GONE);
                admin.setVisibility(View.VISIBLE);
                loginButton.setText("Log-in");
                parentDbName = "User";
            }
        });

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parentDbName.equals("User")){
                    loginUserAccount();
                }
                else if (parentDbName.equals("Admins")){
                    loginAdminAccount();
                }
            }
        });
    }

    private void loginUserAccount() {
        String email, password;
        email = emailAddress2.getText().toString();
        password = password2.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(Login.this, MainMenu.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void loginAdminAccount(){
        String username, password;
        username = emailAddress2.getText().toString();
        password = password2.getText().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "Please enter admin username!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }

        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(username).exists()){

                    Toast.makeText(getApplicationContext(), "Admin Login Successful!", Toast.LENGTH_LONG).show();
                    return;

                }
                else {
                    Toast.makeText(getApplicationContext(), "Admin Login Failed!", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeUI() {
        emailAddress2 = findViewById(R.id.emailAddress2);
        password2 = findViewById(R.id.password2);
    }
}
