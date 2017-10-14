package com.weatherforecast.features.search.presentation;

import android.support.annotation.NonNull;

import com.weatherforecast.core.data.live.LiveResult;
import com.weatherforecast.core.structure.BaseDataHolder;
import com.weatherforecast.features.common.data.model.City;
import com.weatherforecast.features.search.data.Weather;

import java.util.List;

public class SearchDataHolder extends BaseDataHolder {

    private LiveResult<Weather> weatherResult;
    private LiveResult<List<City>> locationsResult;

    LiveResult<Weather> weatherResult() {
        return weatherResult;
    }

    void weatherResult(@NonNull final LiveResult<Weather> result) {
        this.weatherResult = result;
    }

    LiveResult<List<City>> locationSearchesResult() {
        return locationsResult;
    }

    void locationSearchesResult(@NonNull final LiveResult<List<City>> result) {
        this.locationsResult = result;
    }

}
