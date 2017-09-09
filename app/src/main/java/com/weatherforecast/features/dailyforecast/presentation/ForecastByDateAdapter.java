package com.weatherforecast.features.dailyforecast.presentation;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weatherforecast.R;
import com.weatherforecast.features.dailyforecast.data.model.DailyForecast;

import java.util.ArrayList;
import java.util.List;

class ForecastByDateAdapter extends RecyclerView.Adapter<ForecastByDateAdapter.ViewHolder> {

    private final List<DailyForecast> forecasts;

    ForecastByDateAdapter() {
        forecasts = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_forecast_item, parent, false);
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

    void updateContent(@NonNull final List<DailyForecast> updates) {
        this.forecasts.clear();
        this.forecasts.addAll(updates);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView dateView;
        private final RecyclerView timeForecastsView;

        ViewHolder(View view) {
            super(view);
            dateView = view.findViewById(R.id.date_forecast_item_date);

            final ForecastByTimeAdapter adapter = new ForecastByTimeAdapter();
            timeForecastsView = view.findViewById(R.id.date_forecast_item_time_forecasts);
            timeForecastsView.setLayoutManager(
                    new LinearLayoutManager(timeForecastsView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            timeForecastsView.setAdapter(adapter);
        }


        void bind(@NonNull final DailyForecast forecast) {
            dateView.setText(forecast.date());

            final ForecastByTimeAdapter adapter = (ForecastByTimeAdapter) timeForecastsView.getAdapter();
            adapter.updateContent(forecast.forecasts());
        }
    }

}
