package com.example.sporterz_mobile.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.sporterz_mobile.R;
import com.example.sporterz_mobile.adapters.MyMatchesAdapter;
import com.example.sporterz_mobile.models.MatchesDataClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MatchesFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyMatchesAdapter adapter;
    private List<MatchesDataClass> matchesList;

    public MatchesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize your matches list
        matchesList = new ArrayList<>();

        // Populate the matches list
        String url = "https://match-service-1hpr.onrender.com/match-service/getAll";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                populateMatchesList(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Error", volleyError.toString());
            }
        });
        Volley.newRequestQueue(getContext().getApplicationContext()).add(jsonArrayRequest);

        // Initialize adapter with the matches list
        adapter = new MyMatchesAdapter(getContext(), matchesList);

        // Set adapter to RecyclerView
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void populateMatchesList(JSONArray response) {
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonObject = response.getJSONObject(i);

                String idAwayTeam = jsonObject.getString("idAwayTeam");
                String idHomeTeam = jsonObject.getString("idHomeTeam");
                String dateTime = jsonObject.getString("datetimeMatch");
                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
                Date date = null;
                try {
                    date = originalFormat.parse(dateTime);
                } catch (Exception e) {
                    Log.e("Date Parse Exception", e.toString());
                }

                // Format Date object into desired format
                SimpleDateFormat desiredFormat = new SimpleDateFormat("E dd/MM, HH:mm", Locale.getDefault());
                String formattedDatetime = desiredFormat.format(date);

                matchesList.add(new MatchesDataClass(
                        formattedDatetime,
                        jsonObject.getString("nameStadium"),
                        jsonObject.getString("cityStadium"),
                        jsonObject.getString("nameHomeTeam"),
                        String.valueOf(jsonObject.getInt("goalsHomeTeam")),
                        "https://media.api-sports.io/football/teams/" + idHomeTeam +".png",
                        jsonObject.getString("nameAwayTeam"),
                        String.valueOf(jsonObject.getInt("goalsAwayTeam")),
                        "https://media.api-sports.io/football/teams/" + idAwayTeam +".png" )
                );
            }
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
        adapter.notifyDataSetChanged();
    }
}