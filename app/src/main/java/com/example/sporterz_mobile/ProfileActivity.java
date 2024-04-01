package com.example.sporterz_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.sporterz_mobile.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        String uid = auth.getCurrentUser().getUid().toString();

        if (!uid.isEmpty()) {
            databaseReference.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            DataSnapshot dataSnapshot = task.getResult();

                            String username = String.valueOf(dataSnapshot.child("username").getValue());
                            String firstname = String.valueOf(dataSnapshot.child("firstname").getValue());
                            String lastname = String.valueOf(dataSnapshot.child("lastname").getValue());
                            String bio = String.valueOf(dataSnapshot.child("bio").getValue());

                            binding.profileFirstname.setText(firstname);
                            binding.profileLastname.setText(lastname);
                            binding.profileUsername.setText(username);
                            binding.profileBio.setText(bio);
                        } else {
                            Toast.makeText(ProfileActivity.this, "User doesn't exist!", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed to retrieve profile informations!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        binding.mainRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });
    }
}