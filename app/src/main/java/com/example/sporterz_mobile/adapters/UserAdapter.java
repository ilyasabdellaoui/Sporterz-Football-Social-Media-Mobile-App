    package com.example.sporterz_mobile.adapters;

    import android.content.Context;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageButton;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.sporterz_mobile.MessageActivity;
    import com.example.sporterz_mobile.R;
    import com.example.sporterz_mobile.models.Chat;
    import com.example.sporterz_mobile.models.User;
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
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

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
            private TextView userName, userBio;
            private ImageButton chatButton;

            public UserViewHolder(@NonNull View itemView) {
                super(itemView);
                userImage = itemView.findViewById(R.id.user_image);
                userName = itemView.findViewById(R.id.user_name);
                userBio = itemView.findViewById(R.id.user_bio);
                chatButton = itemView.findViewById(R.id.chat_button);

                chatButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createChat();
                    }
                });
            }

            private void getProfileImage() throws IOException {
                final String userId = userList.get(getAdapterPosition()).getUserId();
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

            private void createChat() {
                final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final User selectedUser = userList.get(getAdapterPosition());
                DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference().child("chats");

                // Query to check if a chat with both users already exists
                chatRef.orderByChild("participants/" + currentUserId).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Chat chat = snapshot.getValue(Chat.class);
                            if (chat != null && chat.getParticipants().containsKey(selectedUser.getUserId())) {
                                // Chat already exists, move to MessageActivity with chat ID
                                Intent intent = new Intent(context, MessageActivity.class);
                                intent.putExtra("chat_id", snapshot.getKey());
                                context.startActivity(intent);
                                return;
                            }
                        }

                        // If no existing chat found, create a new one
                        createNewChat(selectedUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(context, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            private void createNewChat(User selectedUser) {
                final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

                // Retrieve current user's information
                usersRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User currentUser = dataSnapshot.getValue(User.class);
                        if (currentUser != null) {
                            DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference().child("chats");

                            String chatKey = chatRef.push().getKey();
                            if (chatKey == null) return;

                            Map<String, Object> chatInfo = new HashMap<>();
                            // Concatenate both users' names in the chat name
                            String chatName = selectedUser.getFirstname() + " " + selectedUser.getLastname() +
                                    " # " + currentUser.getFirstname() + " " + currentUser.getLastname();
                            chatInfo.put("name", chatName);

                            Map<String, Boolean> participants = new HashMap<>();
                            participants.put(currentUserId, true);
                            participants.put(selectedUser.getUserId(), true);
                            chatInfo.put("participants", participants);

                            chatInfo.put("messages", new HashMap<>());  // Initially empty

                            chatRef.child(chatKey).setValue(chatInfo)
                                    .addOnSuccessListener(aVoid -> {
                                        // Move to MessageActivity
                                        Intent intent = new Intent(context, MessageActivity.class);
                                        intent.putExtra("chat_id", chatKey);
                                        context.startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Failed to create chat", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(context, "Failed to retrieve user information", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }