package com.weatherforecast.core.data.repository.remote;

import com.weatherforecast.features.common.data.model.Forecasts;
import com.weatherforecast.features.search.data.Weather;

import dagger.Module;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

@Module
public interface ForecastRepository {

    @GET("forecast")
    Flowable<Forecasts> getForecast(@Query("id") final long id);

    @GET("weather")
    Flowable<Weather> getWeather(@Query("q") final String location);

}
