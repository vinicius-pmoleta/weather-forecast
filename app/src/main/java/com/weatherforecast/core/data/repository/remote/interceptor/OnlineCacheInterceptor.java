package com.weatherforecast.core.data.repository.remote.interceptor;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Response;

public class OnlineCacheInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull final Chain chain) throws IOException {
        final Response response = chain.proceed(chain.request());

        final CacheControl cacheControl = new CacheControl.Builder()
                .maxAge(2, TimeUnit.HOURS)
                .build();

        return response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .build();
    }

}
