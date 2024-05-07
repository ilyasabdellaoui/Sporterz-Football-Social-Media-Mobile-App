package com.example.sporterz_mobile.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sporterz_mobile.R;
import com.example.sporterz_mobile.models.Chat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {
    private List<Chat> chatList = new ArrayList<>();
    private OnChatClickListener listener;

    // Define the interface for click events
    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    public ChatListAdapter(Context context, OnChatClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        if (chat != null) {
            holder.bind(chat);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onChatClick(chat);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    // Method to update the chat list
    public void updateChats(List<Chat> chats) {
        this.chatList.clear();
        this.chatList.addAll(chats);
        notifyDataSetChanged(); // Notify any registered observers that the data set has changed.
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView chatImageView;
        TextView chatNameTextView;
        TextView lastMessageTextView;
        TextView timestampTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatImageView = itemView.findViewById(R.id.chatImage);
            chatNameTextView = itemView.findViewById(R.id.chatNameTextView);
            lastMessageTextView = itemView.findViewById(R.id.lastMessageTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
        }

        // fetch current authenticated user full name



        public void fetchChatName(String chatId) {
            DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference().child("Chats").child(chatId);
            chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Chat chat = snapshot.getValue(Chat.class);
                        chatNameTextView.setText(chat.getName());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Chat List Adapter", "onCancelled: " + error.getMessage());
                }
            });
        }

        public void bind(Chat chat) {
            chatImageView.setImageResource(R.drawable.ic_anon_user_48dp);
            chatNameTextView.setText(chat.getName() != null ? chat.getName() : "Unknown");
            lastMessageTextView.setText(chat.getLastMessage() != null ? chat.getLastMessage() : "No last message");
            timestampTextView.setText(chat.getTimestamp() != null ? chat.getTimestamp() : "Unknown time");
        }
    }
}