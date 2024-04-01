package com.example.sporterz_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.sporterz_mobile.databinding.ActivityInformationsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InformationsActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ActivityInformationsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInformationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        binding.signupRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InformationsActivity.this, RegistrationActivity.class));
            }
        });

        binding.infoCreate.setOnClickListener(view -> {
            String username = binding.infoUsername.getText().toString();
            String firstname = binding.infoFirstname.getText().toString();
            String lastname = binding.infoLastname.getText().toString();
            String bio = binding.infoBio.getText().toString();

            String uid = auth.getCurrentUser().getUid().toString();

            if (username.isEmpty()) {
                binding.infoUsername.setError("Username required!");
            } else if (firstname.isEmpty()) {
                binding.infoFirstname.setError("Firstname required!");
            } else if (lastname.isEmpty()) {
                binding.infoLastname.setError("Lastname required!");
            } else {
                User user = new User(username, firstname, lastname, bio);
                if (!uid.isEmpty()) {
                    databaseReference.child(uid).setValue(user).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Signup successful
                            // You may want to store additional user data in Firebase Database here
                            Toast.makeText(InformationsActivity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(InformationsActivity.this, LoginActivity.class));
                        } else {
                            // Signup failed
                            Toast.makeText(InformationsActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}