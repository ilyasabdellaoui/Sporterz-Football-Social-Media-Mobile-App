package com.example.sporterz_mobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sporterz_mobile.R;
import com.example.sporterz_mobile.models.MatchesDataClass;

public class MyMatchesAdapter extends RecyclerView.Adapter<MyMatchesAdapter.MyMatchesViewHolder> {

    private Context context;
    private List<MatchesDataClass> dataList;

    public void setSearchList(List<MatchesDataClass> dataSearchList) {
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    public MyMatchesAdapter(Context context, List<MatchesDataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyMatchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_item, parent, false);
        return new MyMatchesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMatchesViewHolder holder, int position) {
        MatchesDataClass currentItem = dataList.get(position);

        // Load home team logo from URL using Glide
        Glide.with(context)
                .load(currentItem.getHomeTeamLogo())
                .into(holder.team1_logo);

        // Load away team logo from URL using Glide
        Glide.with(context)
                .load(currentItem.getAwayTeamLogo())
                .into(holder.team2_logo);

        // Set other data as before
        holder.datetime.setText(currentItem.getDateTime());
        holder.stadium.setText(currentItem.getStadium() + "\n" + currentItem.getStadiumCity());
        holder.team1_name.setText(currentItem.getHomeTeam());
        holder.team2_name.setText(currentItem.getAwayTeam());
        holder.score_info.setText(currentItem.getHomeTeamScore() + "\t\t-\t\t" + currentItem.getAwayTeamScore());


        // holder.recCard.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View view) {
        //         Intent intent = new Intent(context, DetailActivity.class);
        //         intent.putExtra("DateTime", currentItem.getDateTime());
        //         intent.putExtra("Stadium", currentItem.getStadium());
        //         intent.putExtra("StadiumCity", currentItem.getStadiumCity());
        //         intent.putExtra("HomeTeam", currentItem.getHomeTeam());
        //         intent.putExtra("HomeTeamScore", currentItem.getHomeTeamScore());
        //         intent.putExtra("HomeTeamLogo", currentItem.getHomeTeamLogo());
        //         intent.putExtra("AwayTeam", currentItem.getAwayTeam());
        //         intent.putExtra("AwayTeamScore", currentItem.getAwayTeamScore());
        //         intent.putExtra("AwayTeamLogo", currentItem.getAwayTeamLogo());
        //         context.startActivity(intent);
        //     }
        // });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class MyMatchesViewHolder extends RecyclerView.ViewHolder {

        ImageView team1_logo, team2_logo;
        TextView datetime, stadium, team1_name, team2_name, score_info;
        CardView match_card;

        public MyMatchesViewHolder(@NonNull View itemView) {
            super(itemView);

            team1_logo = itemView.findViewById(R.id.team1_logo);
            team2_logo = itemView.findViewById(R.id.team2_logo);
            datetime = itemView.findViewById(R.id.datetime);
            stadium = itemView.findViewById(R.id.venue_info);
            team1_name = itemView.findViewById(R.id.team1_name);
            team2_name = itemView.findViewById(R.id.team2_name);
            score_info = itemView.findViewById(R.id.score_info);
            match_card = itemView.findViewById(R.id.matchcard);
        }
    }
}