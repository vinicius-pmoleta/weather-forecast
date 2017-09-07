package com.weatherforecast.features.search.presentation;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weatherforecast.R;
import com.weatherforecast.features.common.data.model.City;

import java.util.ArrayList;
import java.util.List;

class PastQueriesAdapter extends RecyclerView.Adapter<PastQueriesAdapter.ViewHolder> {

    private final List<City> searches;
    private final ActionListener actionListener;

    public PastQueriesAdapter(@NonNull final ActionListener actionListener) {
        this.searches = new ArrayList<>();
        this.actionListener = actionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_past_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(searches.get(position));
    }

    @Override
    public int getItemCount() {
        return searches.size();
    }

    void updateContent(@NonNull final List<City> updates) {
        this.searches.clear();
        this.searches.addAll(updates);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;
        private ImageView actionView;

        ViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.search_past_location_name);
            actionView = view.findViewById(R.id.search_past_location_action);
        }

        void bind(@NonNull final City city) {
            nameView.setText(city.name());
            actionView.setOnClickListener(view -> actionListener.onDetailedForecastAction(city));
        }
    }

    interface ActionListener {

        void onDetailedForecastAction(@NonNull final City city);

    }

}
