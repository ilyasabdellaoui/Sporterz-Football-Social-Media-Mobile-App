package com.example.sporterz_mobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sporterz_mobile.MessageActivity;
import com.example.sporterz_mobile.R;
import com.example.sporterz_mobile.adapters.ChatListAdapter;
import com.example.sporterz_mobile.managers.FirebaseChatManager;
import com.example.sporterz_mobile.models.Chat;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment  implements ChatListAdapter.OnChatClickListener {

    private RecyclerView mRecyclerView;
    private ChatListAdapter mAdapter;
    private FirebaseChatManager mChatManager;
    private ProgressBar mProgressBar;
    private TextView mNoChatsTextView;
    private EditText mSearchEditText;
    private List<Chat> mOriginalChatList;
    private static final String TAG = "MessagesFragment";

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        mRecyclerView = view.findViewById(R.id.chat_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ChatListAdapter(getContext(), this);
        mRecyclerView.setAdapter(mAdapter);

        mProgressBar = view.findViewById(R.id.progress_bar);
        mNoChatsTextView = view.findViewById(R.id.no_chats_found_text_view);

        mChatManager = new FirebaseChatManager();
        // Initialize views
        mRecyclerView = view.findViewById(R.id.chat_recycler_view);
        mSearchEditText = view.findViewById(R.id.search_chat_input);
        mOriginalChatList = new ArrayList<>();

        // Set up search functionality
        setupSearchEditText();
        loadChats();

        return view;
    }

    private void setupSearchEditText() {
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterChats(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterChats(String query) {
        List<Chat> filteredList = new ArrayList<>();
        for (Chat chat : mOriginalChatList) {
            if (chat.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(chat);
            }
        }
        mAdapter.updateChats(filteredList);
    }

    private void loadChats() {
        showLoading(true);
        mChatManager.loadChats(new FirebaseChatManager.ChatLoadListener() {
            @Override
            public void onChatsLoaded(List<Chat> chatList) {
                if (chatList.isEmpty()) {
                    showNoChatsFound(true);
                } else {
                    Log.d(TAG, "onChatsLoaded: " + chatList.size() + " chats loaded");

                    mAdapter = new ChatListAdapter(getContext(), MessagesFragment.this);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.updateChats(chatList);
                    mOriginalChatList.clear();
                    mOriginalChatList.addAll(chatList);
                    showNoChatsFound(chatList.isEmpty());
                }
                showLoading(false);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                showLoading(false);
                showNoChatsFound(true);
            }
        });
    }

    private void showLoading(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showNoChatsFound(boolean show) {
        mNoChatsTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onChatClick(Chat chat) {
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        intent.putExtra("chat_id", chat.getChatId());
        startActivity(intent);
    }

    private void setupRecyclerView() {
        mAdapter = new ChatListAdapter(getContext(), this);
        mRecyclerView.setAdapter(mAdapter);
    }
}