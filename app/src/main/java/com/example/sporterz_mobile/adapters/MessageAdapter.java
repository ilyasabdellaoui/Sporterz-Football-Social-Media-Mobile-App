package com.example.sporterz_mobile.adapters;

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

import com.example.sporterz_mobile.R;
import com.example.sporterz_mobile.models.Message;
import com.example.sporterz_mobile.models.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ChatViewHolder> {

    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHERS = 2;

    private List<Message> messageList;
    private static String currentUserId;
    private static DatabaseReference usersRef;
    private static StorageReference storageReference;

    public MessageAdapter(List<Message> messageList, String currentUserId, DatabaseReference usersRef) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
        this.usersRef = usersRef;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == VIEW_TYPE_ME) {
            view = inflater.inflate(R.layout.item_message_me, parent, false);
        } else {
            view = inflater.inflate(R.layout.item_message_others, parent, false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = messageList.get(position);
        try {
            holder.bind(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        return message.getSenderId().equals(String.valueOf(currentUserId)) ? VIEW_TYPE_ME : VIEW_TYPE_OTHERS;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView senderTextView;
        TextView messageTextView;
        TextView dateTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }

        public void bind(Message message) throws IOException {
            User user;
            // Bind profile image
            getProfileImage(message);
            // Bind sender name
            if (!message.getSenderId().equals(String.valueOf(currentUserId))) {
                fetchSenderName(message.getSenderId());
            }
            // Bind message content
            if (message.getMessageType().equals("text")) {
                messageTextView.setText(message.getMessageContent());
            } else if (message.getMessageType().equals("image")) {
                // Set image message placeholder text
                messageTextView.setText("Image message");
            } else if (message.getMessageType().equals("voice")) {
                // Set voice message placeholder text
                messageTextView.setText("Voice message");
            }

            // Bind message date
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
            String formattedDate = sdf.format(message.getTimestamp());
            dateTextView.setText(formattedDate);
        }

        private void getProfileImage(Message message) throws IOException {
            final String userId = message.getSenderId();
            storageReference = FirebaseStorage.getInstance().getReference().child("images/" + userId);
            File localfile = File.createTempFile("tempImage", "jpeg");
            storageReference.getFile(localfile).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                profileImage.setImageBitmap(bitmap);
            });
        }

        private void fetchSenderName(String senderId) {
            usersRef.child(senderId).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String senderName = dataSnapshot.getValue(String.class);
                    senderTextView.setText(senderName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                    senderTextView.setText("Unknown");
                }
            });
        }
    }
}