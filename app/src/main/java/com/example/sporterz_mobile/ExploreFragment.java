package com.example.sporterz_mobile;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sporterz_mobile.adapters.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {

    private EditText searchInput;
    private ImageButton searchButton;
    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private DatabaseReference usersRef;
    private ProgressBar progressBar;
    private TextView noUsersFoundText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        searchInput = view.findViewById(R.id.seach_username_input);
        searchButton = view.findViewById(R.id.search_user_btn);
        userRecyclerView = view.findViewById(R.id.search_user_recycler_view);
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(getActivity(), userList);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userRecyclerView.setAdapter(userAdapter);
        progressBar = view.findViewById(R.id.progress_bar);
        noUsersFoundText = view.findViewById(R.id.no_users_found_text);

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchInput.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    searchUsers(searchText);
                } else {
                    Toast.makeText(getActivity(), "Please enter a username to search",
                            Toast.LENGTH_SHORT).show();
                    userList.clear();
                    userAdapter.notifyDataSetChanged();
                }
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().trim();
                if (searchText.isEmpty()) {
                    userList.clear();
                    userAdapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }

    private void searchUsers(final String searchText) {
        usersRef.orderByChild("username").startAt(searchText).endAt(searchText + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (user != null) {
                                userList.add(user);
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                        if (userList.isEmpty()) {
                            noUsersFoundText.setVisibility(View.VISIBLE);
                        } else {
                            noUsersFoundText.setVisibility(View.GONE);
                        }
                        userAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Error: " + databaseError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}