package com.example.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Application.Models.Users;
import com.example.Application.Prevalent.Prevalent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;


public class Login extends AppCompatActivity{
    private EditText studentNumber2, password2;
    private String parentDbName;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_menu);

        initializeUI();

        Paper.init(this);


        FloatingActionButton backButton2 = (FloatingActionButton)findViewById(R.id.backButton2);
        backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);
            }
        });

        Button loginButton = (Button)findViewById(R.id.login);
        parentDbName = "Users";

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parentDbName.equals("Users")){
                    loginUserAccount();
                }
            }
        });
    }

    private void loginUserAccount() {
        String studentNumber, password;
        studentNumber = studentNumber2.getText().toString();
        password = password2.getText().toString();

        if (TextUtils.isEmpty(studentNumber)) {
            Toast.makeText(getApplicationContext(), "Please enter student number...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        if (studentNumber.equals("12345")) {
            if (password.equals("admin")) {
                Toast.makeText(getApplicationContext(), "Welcome Admin!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Login.this, AdminMenu.class);
                startActivity(intent);
            }
        } else {
            AllowAccessToAccount(studentNumber, password);
        }
    }

    private void AllowAccessToAccount(final String studentNumber, final String password) {
        if (checkBox.isChecked()){
            Paper.book().write(Prevalent.UserStudentNumberKey, studentNumber);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(parentDbName).child(studentNumber).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbName).child(studentNumber).getValue(Users.class);

                    if (usersData.getStudentnumber().equals(studentNumber))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            if (parentDbName.equals("Users"))
                            {
                                Toast.makeText(Login.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Login.this, MainMenu.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }

                        }
                        else
                        {
                            Toast.makeText(Login.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(Login.this, "Account for " + studentNumber + " do not exists.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void initializeUI() {
        studentNumber2 = findViewById(R.id.studentNumber2);
        password2 = findViewById(R.id.password2);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
    }
}
