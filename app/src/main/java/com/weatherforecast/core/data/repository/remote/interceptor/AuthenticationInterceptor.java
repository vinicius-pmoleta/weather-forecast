package com.weatherforecast.core.data.repository.remote.interceptor;

import android.support.annotation.NonNull;

import com.weatherforecast.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull final Chain chain) throws IOException {
        Request request = chain.request();
        final HttpUrl url = request.url().newBuilder()
                .addQueryParameter("appid", BuildConfig.WEATHER_FORECAST_API_KEY)
                .build();
        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }

}
