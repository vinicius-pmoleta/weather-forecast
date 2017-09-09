package com.weatherforecast.features.search.presentation.adapter;

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

public class PastQueriesAdapter extends RecyclerView.Adapter<PastQueriesAdapter.ViewHolder> {

    private final List<City> searches;
    private final ActionListener actionListener;

    public PastQueriesAdapter(@NonNull final ActionListener actionListener) {
        this.searches = new ArrayList<>();
        this.actionListener = actionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_query_item, parent, false);
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

    public void updateContent(@NonNull final List<City> updates) {
        this.searches.clear();
        this.searches.addAll(updates);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final TextView nameView;
        private final ImageView actionView;

        ViewHolder(View view) {
            super(view);
            rootView = view;
            nameView = view.findViewById(R.id.past_query_item_name);
            actionView = view.findViewById(R.id.past_query_item_action);
        }

        void bind(@NonNull final City city) {
            rootView.setOnClickListener(view -> actionListener.onItemAction(city));
            nameView.setText(city.name());
            actionView.setOnClickListener(view -> actionListener.onForecastAction(city));
        }
    }

    public interface ActionListener {

        void onItemAction(@NonNull final City city);

        void onForecastAction(@NonNull final City city);

    }

}
