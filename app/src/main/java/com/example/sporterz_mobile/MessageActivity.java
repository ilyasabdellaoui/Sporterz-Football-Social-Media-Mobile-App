package com.example.sporterz_mobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sporterz_mobile.adapters.MessageAdapter;
import com.example.sporterz_mobile.managers.FirebaseMessageManager;
import com.example.sporterz_mobile.models.Message;
import com.example.sporterz_mobile.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TextView emptyRecyclerView;
    private MessageAdapter mAdapter;
    private List<Message> mMessageList;
    private FirebaseMessageManager mMessageManager;
    private FirebaseAuth mAuth;
    private EditText mChatEditText;
    private ImageButton mSendButton;

    private String mChatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mRecyclerView = findViewById(R.id.chatRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        emptyRecyclerView = findViewById(R.id.emptyRecyclerView);

        mMessageList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId = Objects.requireNonNull(currentUser).getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mAdapter = new MessageAdapter(mMessageList, currentUserId, usersRef);
        mRecyclerView.setAdapter(mAdapter);

        mMessageManager = new FirebaseMessageManager();

        // Hardcoded chat ID for testing
        mChatId = "test_chat_id";

        // Initialize mSendButton
        mSendButton = findViewById(R.id.sendButton);

        // Initialize mChatEditText
        mChatEditText = findViewById(R.id.chatEditText);

        // Load chat messages
        if (mChatId != null) {
            loadChatMessages();
            setChannelName(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        }

        mSendButton.setOnClickListener(v -> sendMessage());

        // Back button: when clicked finish the activity
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    private void setChannelName(String currentUserId) {
        HashSet<String> userIds = new HashSet<>();
        for (Message message : mMessageList) {
            if (!message.getSenderId().equals(currentUserId)) {
                userIds.add(message.getSenderId());
            }
        }

        if (!userIds.isEmpty()) {
            String otherUserId = userIds.iterator().next();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
            TextView channelName = findViewById(R.id.channelName);
            channelName.setText("Loading...");

            usersRef.child(otherUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        channelName.setText(user.getFirstname() + " " + user.getLastname());
                    } else {
                        channelName.setText("Chat");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    channelName.setText("Chat");
                }
            });
        } else {
            TextView channelName = findViewById(R.id.channelName);
            channelName.setText("Chat");
        }
    }

    private void loadChatMessages() {
        Query messageQuery = mMessageManager.getMessagesReference(mChatId).orderByChild("timestamp");
        messageQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMessageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    mMessageList.add(message);
                }
                mAdapter.notifyDataSetChanged();

                setChannelName(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

                // Check if messageList is empty
                if (mMessageList.isEmpty()) {
                    mRecyclerView.setVisibility(View.GONE);
                    emptyRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    emptyRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(MessageActivity.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String messageText = mChatEditText.getText().toString().trim();
        if (!messageText.isEmpty()) {
            long timestamp = System.currentTimeMillis();
            Message message = new Message(messageText, "text", timestamp, Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
            mMessageManager.sendMessage(mChatId, message);
            mChatEditText.setText("");
        }
    }
}