package com.weatherforecast.core.data.repository.remote;

import com.weatherforecast.features.common.data.model.Forecasts;

import dagger.Module;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

@Module
public interface ForecastRepository {

    @GET("forecast")
    Flowable<Forecasts> getForecast(@Query("q") final String query);

}
