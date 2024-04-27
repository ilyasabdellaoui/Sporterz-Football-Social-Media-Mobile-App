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

import com.example.sporterz_mobile.R;
import com.example.sporterz_mobile.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private StorageReference storageReference;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private ImageView userImage;
        private TextView userName;
        private TextView userBio;

        private TextView username;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);
            userBio = itemView.findViewById(R.id.user_bio);
        }

        private void getProfileImage() throws IOException {
            final String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid().toString();
            storageReference = FirebaseStorage.getInstance().getReference().child("images/" + userId);
            File localfile = File.createTempFile("tempImage", "jpeg");
            storageReference.getFile(localfile).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                userImage.setImageBitmap(bitmap);
            }).addOnFailureListener(exception -> {
                Toast.makeText(context, "Failed to load user image!", Toast.LENGTH_SHORT).show();
            });
        }

        public void bind(User user) {
            userName.setText(user.getFirstname() + " " + user.getLastname() + " @" + user.getUsername());
            userBio.setText(user.getBio());
            try {
                getProfileImage();
            } catch (IOException e) {
                Toast.makeText(context, "Failed to load user image!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}