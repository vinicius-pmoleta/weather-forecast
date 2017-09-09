package com.weatherforecast.features.search.presentation;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.weatherforecast.R;
import com.weatherforecast.core.WeatherForecastApplication;
import com.weatherforecast.core.structure.BaseActivity;
import com.weatherforecast.core.view.GlideApp;
import com.weatherforecast.features.common.data.model.City;
import com.weatherforecast.features.dailyforecast.presentation.DailyForecastActivity;
import com.weatherforecast.features.search.di.DaggerSearchFeatureComponent;
import com.weatherforecast.features.search.di.SearchPresentationModule;
import com.weatherforecast.features.search.di.SearchUseCaseModule;
import com.weatherforecast.features.search.presentation.adapter.PastQueriesAdapter;
import com.weatherforecast.features.search.presentation.model.WeatherScreenModel;

import java.util.List;

public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {

    private ProgressBar progressView;
    private EditText queryView;
    private AppCompatImageButton searchActionView;
    private ViewGroup weatherView;
    private TextView locationView;
    private ImageView conditionIconView;
    private TextView conditionNameView;
    private TextView temperatureCurrentView;
    private TextView temperatureMinimumView;
    private TextView temperatureMaximumView;
    private AppCompatImageView forecastActionView;
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
        progressView = findViewById(R.id.search_progress);
        queryView = findViewById(R.id.search_query);
        searchActionView = findViewById(R.id.search_action);

        searchActionView.setOnClickListener(
                view -> presenter.loadWeatherForLocation(queryView.getText().toString()));
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
        forecastActionView = findViewById(R.id.current_weather_forecast_action);

        presenter.loadLastWeatherIfAvailable();
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
    public void showWeather(@NonNull final WeatherScreenModel weather) {
        locationView.setText(weather.location());
        conditionNameView.setText(weather.condition());
        temperatureCurrentView.setText(weather.temperatureCurrent());
        temperatureMinimumView.setText(weather.temperatureMinimum());
        temperatureMaximumView.setText(weather.temperatureMaximum());

        forecastActionView.setOnClickListener(
                view -> triggerForecastAction(City.create(weather.id(), weather.location())));
        weatherView.setVisibility(View.VISIBLE);

        GlideApp.with(this)
                .load(weather.conditionIcon())
                .centerCrop()
                .into(conditionIconView);
    }

    @Override
    public void showErrorLoadingWeather() {
        Toast.makeText(this, R.string.search_weather_error_loading, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLocationsSearched(@NonNull final List<City> cities) {
        adapter.updateContent(cities);
    }

    @Override
    public void showProgress() {
        queryView.setVisibility(View.INVISIBLE);
        searchActionView.setVisibility(View.INVISIBLE);
        weatherView.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressView.setVisibility(View.GONE);
        queryView.setVisibility(View.VISIBLE);
        searchActionView.setVisibility(View.VISIBLE);
    }

    @Override
    public void resetInteractions() {
        queryView.setText("");
        queryView.clearFocus();

        final InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(queryView.getWindowToken(), 0);
    }

    private void triggerForecastAction(@NonNull final City city) {
        startActivity(DailyForecastActivity.newIntent(this, city.id(), city.name()));
    }

    private class PastQueriesActionListener implements PastQueriesAdapter.ActionListener {

        @Override
        public void onItemAction(@NonNull final City city) {
            presenter.loadWeatherForLocation(city.name());
        }

        @Override
        public void onForecastAction(@NonNull final City city) {
            triggerForecastAction(city);
        }
    }

}
