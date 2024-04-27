package com.example.sporterz_mobile.fragements;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sporterz_mobile.models.HomeItem;
import com.example.sporterz_mobile.R;
import com.example.sporterz_mobile.adapters.PostsAdapter;
import com.example.sporterz_mobile.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference, dbReference;
    private String uid;
    private RecyclerView recyclerView;
    private PostsAdapter postsAdapter;
    private ArrayList<HomeItem> items;
    private HomeItem item;
    private String username;
    private Bitmap imageBitmap;
    private StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setHasFixedSize(true);
        items = new ArrayList<>();
        postsAdapter = new PostsAdapter(getContext(), items);
        recyclerView.setAdapter(postsAdapter);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        dbReference = FirebaseDatabase.getInstance().getReference("Posts");

        uid = auth.getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    username = String.valueOf(dataSnapshot.child("username").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String thinking = binding.homeInput.getText().toString().trim();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                Date currentDate = new Date();
                String formattedDate = dateFormat.format(currentDate);
                try {
                    getProfileImage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                HomeItem homeItem = new HomeItem(imageBitmap, username, thinking, formattedDate);
                String postId = databaseReference.push().getKey();
                if (!thinking.isEmpty() && !postId.isEmpty()) {
                    databaseReference.child(postId).setValue(homeItem)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Posted successfully!", Toast.LENGTH_SHORT).show();
                                    binding.homeInput.setText("");
                                    fetchPosts();
                                } else {
                                    Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getContext(), "Please enter something to post", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fetchPosts();

        return view;
    }

    private void getProfileImage() throws IOException {
        final String userId = auth.getCurrentUser().getUid().toString();
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + userId);
        File localfile = File.createTempFile("tempImage", "jpeg");
        storageReference.getFile(localfile).addOnSuccessListener(taskSnapshot -> {
            imageBitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
        });
    }

    private void fetchPosts() {
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Bitmap postImage = (Bitmap) snapshot.child("imageBitmap").getValue();
                    String username = String.valueOf(snapshot.child("username").getValue());
                    String thinking = String.valueOf(snapshot.child("thinking").getValue());
                    String postDate = String.valueOf(snapshot.child("postDate").getValue());
                    item = new HomeItem(postImage, username, thinking, postDate);
                    items.add(item);
                }
                postsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Toast.makeText(getContext(), "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}