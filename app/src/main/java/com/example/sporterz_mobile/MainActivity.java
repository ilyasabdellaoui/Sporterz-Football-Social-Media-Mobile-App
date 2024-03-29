package com.example.sporterz_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.sporterz_mobile.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeFragment(new HomeFragment());

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

    private void changeFragment(Fragment frag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, frag);
        fragmentTransaction.commit();
    }
}