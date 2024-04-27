package com.example.sporterz_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sporterz_mobile.databinding.ActivityMainBinding;
import com.example.sporterz_mobile.fragements.ExploreFragment;
import com.example.sporterz_mobile.fragements.HomeFragment;
import com.example.sporterz_mobile.fragements.MatchesFragment;
import com.example.sporterz_mobile.fragements.MessagesFragment;
import com.example.sporterz_mobile.fragements.NotificationsFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeFragment(new HomeFragment());
        setSupportActionBar(binding.toolbar2);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    changeFragment(new HomeFragment());
                    break;
                case R.id.explore:
                    changeFragment(new ExploreFragment());
                    break;
                case R.id.matches:
                    changeFragment(new MatchesFragment());
                    break;
                case R.id.notifications:
                    changeFragment(new NotificationsFragment());
                    break;
                case R.id.messages:
                    changeFragment(new MessagesFragment());
                    break;
            }
            return true;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLogout:
                auth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                Toast.makeText(MainActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuProfile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
        }

        return true;
    }

    private void changeFragment(Fragment frag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, frag);
        fragmentTransaction.commit();
    }

}