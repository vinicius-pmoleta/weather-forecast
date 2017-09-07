package com.weatherforecast.features.dailyforecast.presentation;

import android.app.SearchManager;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.weatherforecast.R;
import com.weatherforecast.core.WeatherForecastApplication;
import com.weatherforecast.core.structure.BaseActivity;
import com.weatherforecast.features.common.data.model.Forecast;
import com.weatherforecast.features.dailyforecast.data.model.DailyForecast;
import com.weatherforecast.features.dailyforecast.di.DaggerDailyForecastFeatureComponent;
import com.weatherforecast.features.dailyforecast.di.DailyForecastPresentationModule;
import com.weatherforecast.features.dailyforecast.di.DailyForecastUseCaseModule;

import java.util.List;

public class DailyForecastActivity extends BaseActivity<DailyForecastPresenter> implements DailyForecastContract.View {

    private static final String EXTRA_ID = "extra_id";
    private static final String EXTRA_LOCATION = "extra_location";

    public static Intent newIntent(@NonNull final Context context, final long id, @NonNull final String location) {
        return new Intent(context, DailyForecastActivity.class)
                .putExtra(EXTRA_ID, id)
                .putExtra(EXTRA_LOCATION, location);
    }

    @Override
    public void initialiseDependencyInjector() {
        DaggerDailyForecastFeatureComponent.builder()
                .applicationComponent(((WeatherForecastApplication) getApplication()).applicationComponent())
                .dailyForecastPresentationModule(new DailyForecastPresentationModule(this))
                .dailyForecastUseCaseModule(new DailyForecastUseCaseModule())
                .build()
                .inject(this);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_forecast_activity);
        extractExtrasAndLoadDailyForecast(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        extractExtrasAndLoadDailyForecast(intent);
    }

    private void extractExtrasAndLoadDailyForecast(@NonNull final Intent intent) {
        final long id = intent.getLongExtra(EXTRA_ID, -1L);
        final String location = extractLocation(intent);
        presenter.loadLocationForecast(id, location);
    }

    private String extractLocation(@NonNull final Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY);
            if (!TextUtils.isEmpty(query)) {
                return query;
            }
        }
        return intent.getStringExtra(EXTRA_LOCATION);
    }

    @Override
    public LifecycleOwner provideLifecycleOwner() {
        return this;
    }

    @Override
    public DailyForecastDataHolder provideForecastsDataHolder() {
        return ViewModelProviders.of(this).get(DailyForecastDataHolder.class);
    }

    @Override
    public void showErrorLoadingDailyForecast() {
        Toast.makeText(this, R.string.daily_forecast_error_loading, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDailyForecasts(@NonNull final List<DailyForecast> dailyForecasts) {
        Log.d("TEST", String.valueOf(dailyForecasts.size()));
        for (final DailyForecast dailyForecast : dailyForecasts) {
            for (final Forecast forecast : dailyForecast.forecasts()) {
                Log.d("TEST", "KEY " + dailyForecast.date() + " / VALUE " + String.valueOf(forecast.date()));
            }
        }
    }
}
