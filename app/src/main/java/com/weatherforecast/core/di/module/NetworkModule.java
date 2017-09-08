package com.weatherforecast.core.di.module;

import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.weatherforecast.BuildConfig;
import com.weatherforecast.core.WeatherForecastApplication;
import com.weatherforecast.core.data.repository.local.NetworkCache;
import com.weatherforecast.core.data.repository.remote.adapter.AutoValueGsonFactory;
import com.weatherforecast.core.data.repository.remote.interceptor.AuthenticationInterceptor;
import com.weatherforecast.core.data.repository.remote.interceptor.OnlineCacheInterceptor;
import com.weatherforecast.core.data.repository.remote.interceptor.UnitInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(@NonNull final WeatherForecastApplication application) {
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? Level.BODY : Level.NONE);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new AuthenticationInterceptor())
                .addInterceptor(new UnitInterceptor())
                .addNetworkInterceptor(new OnlineCacheInterceptor())
                .cache(new NetworkCache().initialise(application.getApplicationContext()))
                .build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(@NonNull final OkHttpClient okHttpClient) {
        final GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(
                new GsonBuilder()
                        .registerTypeAdapterFactory(AutoValueGsonFactory.create())
                        .create()
        );

        return new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

}
