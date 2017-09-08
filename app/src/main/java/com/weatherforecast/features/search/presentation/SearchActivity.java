package com.weatherforecast.features.search.presentation;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.weatherforecast.R;
import com.weatherforecast.core.WeatherForecastApplication;
import com.weatherforecast.core.structure.BaseActivity;
import com.weatherforecast.features.common.data.model.City;
import com.weatherforecast.features.dailyforecast.presentation.DailyForecastActivity;
import com.weatherforecast.features.search.data.Weather;
import com.weatherforecast.features.search.di.DaggerSearchFeatureComponent;
import com.weatherforecast.features.search.di.SearchPresentationModule;
import com.weatherforecast.features.search.di.SearchUseCaseModule;

import java.util.List;

public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {

    private ViewGroup weatherView;
    private TextView locationView;
    private ImageView conditionIconView;
    private TextView conditionNameView;
    private TextView temperatureCurrentView;
    private TextView temperatureMinimumView;
    private TextView temperatureMaximumView;
    private PastQueriesAdapter adapter;

    @Override
    public void initialiseDependencyInjector() {
        DaggerSearchFeatureComponent.builder()
                .applicationComponent(((WeatherForecastApplication) getApplication()).applicationComponent())
                .searchPresentationModule(new SearchPresentationModule(this))
                .searchUseCaseModule(new SearchUseCaseModule())
                .build()
                .inject(this);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        initialiseSearchInteraction();
        initialiseSuggestionsView();
        initialiseWeatherView();
    }

    private void initialiseSearchInteraction() {
        final EditText queryField = findViewById(R.id.search_query);
        findViewById(R.id.search_action).setOnClickListener(view -> presenter.loadWeatherForLocation(queryField.getText().toString()));
    }

    private void initialiseSuggestionsView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new PastQueriesAdapter(new PastQueriesActionListener());

        final RecyclerView pastQueriesView = findViewById(R.id.search_past_queries);
        pastQueriesView.setLayoutManager(layoutManager);
        pastQueriesView.setAdapter(adapter);

        presenter.loadLocationsSearched();
    }

    private void initialiseWeatherView() {
        weatherView = findViewById(R.id.search_weather_result);
        locationView = findViewById(R.id.current_weather_location);
        conditionIconView = findViewById(R.id.current_weather_condition_icon);
        conditionNameView = findViewById(R.id.current_weather_condition_name);
        temperatureCurrentView = findViewById(R.id.current_weather_temperature_now);
        temperatureMinimumView = findViewById(R.id.current_weather_temperature_minimum);
        temperatureMaximumView = findViewById(R.id.current_weather_temperature_maximum);
    }

    @Override
    public LifecycleOwner provideLifecycleOwner() {
        return this;
    }


    @Override
    public SearchDataHolder provideSearchDataHolder() {
        return ViewModelProviders.of(this).get(SearchDataHolder.class);
    }

    @Override
    public void showWeather(@NonNull final Weather weather) {
        locationView.setText(weather.name());
        conditionNameView.setText(weather.conditions().get(0).name());
        temperatureCurrentView.setText(getString(R.string.temperature_format, weather.temperature().current()));
        temperatureMinimumView.setText(getString(R.string.temperature_format, weather.temperature().minimum()));
        temperatureMaximumView.setText(getString(R.string.temperature_format, weather.temperature().maximum()));

        Glide.with(this)
                .load(getString(R.string.weather_api_url, weather.conditions().get(0).icon()))
                .into(conditionIconView);

        weatherView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorLoadingWeather() {
        Toast.makeText(this, R.string.search_weather_error_loading, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLocationsSearched(@NonNull final List<City> cities) {
        adapter.updateContent(cities);
    }

    private class PastQueriesActionListener implements PastQueriesAdapter.ActionListener {

        @Override
        public void onDetailedForecastAction(@NonNull final City city) {
            startActivity(DailyForecastActivity.newIntent(SearchActivity.this, city.id(), city.name()));
        }
    }

}
