package com.weatherforecast.features.dailyforecast.presentation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weatherforecast.R;
import com.weatherforecast.core.view.GlideApp;
import com.weatherforecast.features.dailyforecast.presentation.model.ForecastScreenModel;

import java.util.ArrayList;
import java.util.List;

class ForecastByTimeAdapter extends RecyclerView.Adapter<ForecastByTimeAdapter.ViewHolder> {

    private final List<ForecastScreenModel> forecasts;

    ForecastByTimeAdapter() {
        forecasts = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_forecast_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(forecasts.get(position));
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }

    void updateContent(@NonNull final List<ForecastScreenModel> updates) {
        this.forecasts.clear();
        this.forecasts.addAll(updates);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final TextView timeView;
        private final ImageView conditionView;
        private final TextView temperatureView;

        ViewHolder(View view) {
            super(view);
            rootView = view;
            timeView = view.findViewById(R.id.time_forecast_item_time);
            conditionView = view.findViewById(R.id.time_forecast_item_condition);
            temperatureView = view.findViewById(R.id.time_forecast_item_temperature);
        }

        void bind(@NonNull final ForecastScreenModel forecast) {
            final Context context = rootView.getContext();

            timeView.setText(forecast.time());
            temperatureView.setText(forecast.temperature());

            GlideApp.with(context)
                    .load(forecast.conditionIcon())
                    .centerCrop()
                    .into(conditionView);
        }
    }
}
