package com.example.sporterz_mobile.managers;

import com.example.sporterz_mobile.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseMessageManager {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public FirebaseMessageManager() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    // Method to send a message
    public void sendMessage(String chatId, Message message) {
        String messageId = mDatabase.child("chats").child(chatId).child("messages").push().getKey();
        mDatabase.child("chats").child(chatId).child("messages").child(messageId).setValue(message);
    }

    public DatabaseReference getMessagesReference(String chatId) {
        return mDatabase.child("chats").child(chatId).child("messages");
    }
}