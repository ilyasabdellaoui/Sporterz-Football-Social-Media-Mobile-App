package com.example.sporterz_mobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sporterz_mobile.databinding.ActivityInformationsBinding;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class InformationsActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ActivityInformationsBinding binding;
    private ImageButton infoImage;
    private Uri imageUri;
    private StorageReference storageReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            infoImage.setImageURI(imageUri);
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Uploading image...");
        progress.show();

        final String userId = auth.getCurrentUser().getUid().toString();
        StorageReference ref = storageReference.child("images/" + userId);
        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progress.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Image uploaded", Snackbar.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
                Toast.makeText(InformationsActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progres_time = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progress.setMessage("Uploaded " + (int) progres_time + "%");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInformationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        binding.signupRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InformationsActivity.this, RegistrationActivity.class));
            }
        });

        infoImage = findViewById(R.id.infoImage);
        infoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
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
                User user = new User(firstname, lastname, username, bio);
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