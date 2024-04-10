package com.example.sporterz_mobile;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView postProfile;
        TextView postContent, postUsername, postTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            postProfile = itemView.findViewById(R.id.postProfile);
            postUsername = itemView.findViewById(R.id.postUsername);
            postContent = itemView.findViewById(R.id.postContent);
            postTime = itemView.findViewById(R.id.postTime);
        }
    }

    private Context context;
    private List<HomeItem> items;

    public MyAdapter(Context context, ArrayList<HomeItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.home_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.postProfile.setImageBitmap(items.get(position).getImageBitmap());
        holder.postUsername.setText(items.get(position).getUsername());
        holder.postContent.setText(items.get(position).getThinking());
        holder.postTime.setText(items.get(position).getPostDate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}