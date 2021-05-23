package com.example.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private EditText register_studentNumber, register_password, register_confirmPassword, register_fullName;
    private DatabaseReference ProductRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_menu);

        initializeUI();

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
                registerNewUser();
            }
        });

    }

    private void registerNewUser() {

        ProductRef = FirebaseDatabase.getInstance().getReference().child("Users");
        String studentNumber, password, confirmPassword, name;
        studentNumber = register_studentNumber.getText().toString();
        password = register_password.getText().toString();
        confirmPassword = register_confirmPassword.getText().toString();
        name = register_fullName.getText().toString();

        if (TextUtils.isEmpty(studentNumber)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Please enter phone number!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Please enter the confirmation password!", Toast.LENGTH_LONG).show();
            return;
        }
        if (checkValidID(studentNumber)) {
            Toast.makeText(getApplicationContext(), "Invalid ID number!", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length() < 8){
            Toast.makeText(getApplicationContext(), "The password should be 8 characters long.", Toast.LENGTH_LONG).show();
            return;
        }
        if (!password.equals(confirmPassword)){
            Toast.makeText(getApplicationContext(), "The passwords do not match!", Toast.LENGTH_LONG).show();
            return;
        }
        validateAccount(studentNumber, name, password);
    }

    private boolean checkValidID(String studentNumber){
        // true = invalid, false = valid
        if(studentNumber.length() != 8) return true;

        int ID = Integer.parseInt(studentNumber);
        int sum = 0;
        for(int i = 1; i <= 8; i++){
            sum = sum + (i * (ID % 10));
            ID /= 10;
        }
        System.out.println(sum);
        if(sum % 11 == 0)
            return false;

        return true;
    }

    private void validateAccount(final String studentNumber, final String name, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Users").child(studentNumber).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("studentnumber", studentNumber);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);

                    RootRef.child("Users").child(studentNumber).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Register.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(Register.this, Login.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(Register.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(Register.this, "This " + studentNumber + " already exists.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Register.this, "Please try again", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Register.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void initializeUI() {
        register_studentNumber = findViewById(R.id.studentNumber);
        register_password = findViewById(R.id.password);
        register_confirmPassword = findViewById(R.id.confirmPassword);
        register_fullName = findViewById(R.id.fullName);
    }

}