package com.weatherforecast.core.data.repository.remote;

import com.weatherforecast.core.data.repository.remote.model.ForecastResponse;

import dagger.Module;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

@Module
public interface ForecastRepository {

    @GET("forecast")
    Flowable<ForecastResponse> getForecast(@Query("q") final String query);

}
