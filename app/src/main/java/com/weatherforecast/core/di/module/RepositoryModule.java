package com.weatherforecast.core.di.module;

import android.support.annotation.NonNull;

import com.weatherforecast.core.data.repository.remote.ForecastRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public ForecastRepository provideForecastRepository(@NonNull final Retrofit retrofit) {
        return retrofit.create(ForecastRepository.class);
    }

}
