package com.weatherforecast.features.search.presentation.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.weatherforecast.R;
import com.weatherforecast.features.common.data.model.Forecast;
import com.weatherforecast.features.search.data.Weather;

public class WeatherScreenConverter {

    private final String temperaturePattern;
    private final String iconUrlPattern;

    public static WeatherScreenConverter newInstance(@NonNull final Context context) {
        return new WeatherScreenConverter(context.getString(R.string.temperature_format),
                context.getString(R.string.weather_icon_url));
    }

    WeatherScreenConverter(@NonNull final String temperaturePattern, @NonNull final String iconUrlPattern) {
        this.temperaturePattern = temperaturePattern;
        this.iconUrlPattern = iconUrlPattern;
    }

    public WeatherScreenModel prepareForPresentation(@NonNull final Weather weather) {
        final String temperatureMaximum = String.format(temperaturePattern, weather.temperature().maximum());
        final String temperatureCurrent = String.format(temperaturePattern, weather.temperature().current());
        final String temperatureMinimum = String.format(temperaturePattern, weather.temperature().minimum());

        final Forecast.Condition condition = weather.conditions().get(0);
        final String conditionName = condition.name();
        final String conditionIcon = String.format(iconUrlPattern, condition.icon());

        return WeatherScreenModel.create(weather.id(), weather.name(), conditionName, conditionIcon,
                temperatureMaximum, temperatureCurrent, temperatureMinimum);
    }

}
