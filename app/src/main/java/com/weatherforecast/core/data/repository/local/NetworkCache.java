package com.weatherforecast.core.data.repository.local;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;

import okhttp3.Cache;

public class NetworkCache {

    public Cache initialise(@NonNull final Context context) {
        return new Cache(new File(context.getCacheDir(), "network-cache"), 10 * 1024 * 1024);
    }

}
