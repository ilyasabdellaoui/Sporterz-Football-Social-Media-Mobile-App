package com.example.sporterz_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class LoadingActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        auth = FirebaseAuth.getInstance();
        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (auth.getCurrentUser() != null) {
                    startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(LoadingActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 3000);
    }
}