package com.example.sporterz_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sporterz_mobile.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.example.sporterz_mobile.databinding.ActivityRegistrationBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    // Declare any other necessary variables.
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private ActivityRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.editTextName.getText().toString().trim();
                String username = binding.editTextUsername.getText().toString().trim();
                String email = binding.signupEmail.getText().toString().trim();
                String password = binding.signupPassword.getText().toString().trim();

                String uid = auth.getCurrentUser().getUid();
                User user = new User(name, username);

                if (name.isEmpty()) {
                    binding.editTextName.setError("Name cannot be empty");
                } else if (username.isEmpty()) {
                    binding.editTextUsername.setError("Username cannot be empty");
                } else if (email.isEmpty()) {
                    binding.signupEmail.setError("Email cannot be empty");
                } else if (password.isEmpty()) {
                    binding.signupPassword.setError("Password cannot be empty");
                } else {
                    // Create user with email and password, passing additional fields
                    if (uid != null) {
                        databaseReference.child(uid).setValue(user);
                    }
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Signup successful
                                        // You may want to store additional user data in Firebase Database here
                                        Toast.makeText(RegistrationActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                    } else {
                                        // Signup failed
                                        Toast.makeText(RegistrationActivity.this, "Signup Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        binding.loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });
    }

}