package com.example.sporterz_mobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment  implements ChatListAdapter.OnChatClickListener {

    private RecyclerView mRecyclerView;
    private ChatListAdapter mAdapter;
    private List<Chat> mChatList;
    private FirebaseChatManager mChatManager;
    private ProgressBar mProgressBar;
    private TextView mNoChatsTextView;

    private static final String TAG = "MessagesFragment";

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        mRecyclerView = view.findViewById(R.id.chat_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mChatList = new ArrayList<>();
        mAdapter = new ChatListAdapter(getContext(), this);
        mRecyclerView.setAdapter(mAdapter);

        mProgressBar = view.findViewById(R.id.progress_bar);
        mNoChatsTextView = view.findViewById(R.id.no_chats_found_text_view);

        mChatManager = new FirebaseChatManager();
        loadChats();

        return view;
    }

    private void loadChats() {
        showLoading(true);
        mChatManager.loadChats(new FirebaseChatManager.ChatLoadListener() {
            @Override
            public void onChatsLoaded(List<Chat> chatList) {
                if (chatList.isEmpty()) {
                    showNoChatsFound(true);
                } else {
                    mChatList.clear();
                    mAdapter.updateChats(chatList);
                    mChatList.addAll(chatList);
                    mAdapter.notifyDataSetChanged();
                    showNoChatsFound(false);
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
        // Handle the click event here
        // For example, start the ChatActivity with the clicked chat
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        intent.putExtra("chat_id", chat.getChatId());
        startActivity(intent);
    }

    private void setupRecyclerView() {
        mAdapter = new ChatListAdapter(getContext(), this);
        mRecyclerView.setAdapter(mAdapter);
    }
}