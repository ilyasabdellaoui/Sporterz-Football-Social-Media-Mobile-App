package com.example.sporterz_mobile;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostsViewHolder extends RecyclerView.ViewHolder {

    TextView postContent;


    public PostsViewHolder(@NonNull View itemView) {
        super(itemView);

        postContent = itemView.findViewById(R.id.postContent);
    }
}
