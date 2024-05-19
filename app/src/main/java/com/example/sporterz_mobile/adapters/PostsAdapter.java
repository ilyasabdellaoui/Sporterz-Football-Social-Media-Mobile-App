package com.example.sporterz_mobile.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sporterz_mobile.models.HomeItem;
import com.example.sporterz_mobile.R;
import com.example.sporterz_mobile.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private StorageReference storageReference;
    private Context context;
    private List<HomeItem> items;

    public PostsAdapter(Context context, ArrayList<HomeItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.home_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HomeItem homeItem = items.get(position);
        holder.bind(homeItem);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView postProfile;
        TextView postContent, postUsername, postTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            postProfile = itemView.findViewById(R.id.postProfile);
            postUsername = itemView.findViewById(R.id.postUsername);
            postContent = itemView.findViewById(R.id.postContent);
            postTime = itemView.findViewById(R.id.postTime);
        }

        private void getProfileImage() throws IOException {
            final String userId = items.get(getAdapterPosition()).getUserId();
            storageReference = FirebaseStorage.getInstance().getReference().child("images/" + userId);
            File localfile = File.createTempFile("tempImage", "jpeg");
            storageReference.getFile(localfile).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                postProfile.setImageBitmap(bitmap);
            }).addOnFailureListener(exception -> {
                Toast.makeText(context, "An error has occurred!", Toast.LENGTH_SHORT).show();
            });
        }

        public void bind(HomeItem homeItem) {
            postUsername.setText(homeItem.getUsername());
            postTime.setText(homeItem.getPostDate());
            postContent.setText(homeItem.getThinking());
            try {
                getProfileImage();
            } catch (IOException e) {
                Toast.makeText(context, "An error has occurred!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}