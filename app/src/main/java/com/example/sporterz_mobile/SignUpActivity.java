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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    // Declare any other necessary variables.
    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword;
    private Button signupButton;
    private ImageButton loginRedirect;
    private EditText editTextName, editTextUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Initialize the FirebaseAuth instance in the onCreate()
        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirect = findViewById(R.id.loginRedirect);

        // Initialize additional EditText fields
        editTextName = findViewById(R.id.editTextName);
        editTextUsername = findViewById(R.id.editTextUsername);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String username = editTextUsername.getText().toString().trim();
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();

                if (name.isEmpty()) {
                    editTextName.setError("Name cannot be empty");
                } else if (username.isEmpty()) {
                    editTextUsername.setError("Username cannot be empty");
                } else if (user.isEmpty()) {
                    signupEmail.setError("Email cannot be empty");
                } else if (pass.isEmpty()) {
                    signupPassword.setError("Password cannot be empty");
                } else {
                    // Create user with email and password, passing additional fields
                    auth.createUserWithEmailAndPassword(user, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Signup successful
                                        // You may want to store additional user data in Firebase Database here
                                        Toast.makeText(SignUpActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                    } else {
                                        // Signup failed
                                        Toast.makeText(SignUpActivity.this, "Signup Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

}