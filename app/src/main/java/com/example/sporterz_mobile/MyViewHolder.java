package com.example.sporterz_mobile;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView postContent;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        postContent = itemView.findViewById(R.id.postContent);
    }
}
