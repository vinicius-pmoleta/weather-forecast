package com.weatherforecast.features.dailyforecast.presentation.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.weatherforecast.R;
import com.weatherforecast.features.common.data.model.Forecast;
import com.weatherforecast.features.dailyforecast.data.model.DailyForecast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.support.annotation.VisibleForTesting.PRIVATE;

public class DailyForecastScreenConverter {

    private static final String TAG = DailyForecastScreenConverter.class.getSimpleName();

    private final String temperaturePattern;
    private final String dateParserPattern;
    private final String dateFormatterPattern;
    private final String timeParserPattern;
    private final String timeFormatterPattern;
    private final String iconUrlPattern;

    public static DailyForecastScreenConverter newInstance(@NonNull final Context context) {
        return new DailyForecastScreenConverter(
                context.getString(R.string.temperature_format),
                context.getString(R.string.daily_forecast_date_parser_pattern),
                context.getString(R.string.daily_forecast_date_formatter_pattern),
                context.getString(R.string.daily_forecast_time_parser_pattern),
                context.getString(R.string.daily_forecast_time_formatter_pattern),
                context.getString(R.string.weather_icon_url));
    }

    DailyForecastScreenConverter(@NonNull final String temperaturePattern, @NonNull final String dateParserPattern,
                                 @NonNull final String dateFormatterPattern, @NonNull final String timeParserPattern,
                                 @NonNull final String timeFormatterPattern, @NonNull final String iconUrlPattern) {
        this.temperaturePattern = temperaturePattern;
        this.dateParserPattern = dateParserPattern;
        this.dateFormatterPattern = dateFormatterPattern;
        this.timeParserPattern = timeParserPattern;
        this.timeFormatterPattern = timeFormatterPattern;
        this.iconUrlPattern = iconUrlPattern;
    }

    public List<DailyForecastScreenModel> prepareForPresentation(@NonNull final List<DailyForecast> forecasts) {
        final List<DailyForecastScreenModel> models = new ArrayList<>();
        for (final DailyForecast forecast : forecasts) {
            models.add(prepareForPresentation(forecast));
        }
        return models;
    }

    @VisibleForTesting(otherwise = PRIVATE)
    DailyForecastScreenModel prepareForPresentation(@NonNull final DailyForecast dailyForecast) {
        final String date = formatDate(dailyForecast.date());

        final List<ForecastScreenModel> forecasts = new ArrayList<>();
        for (final Forecast forecast : dailyForecast.forecasts()) {
            forecasts.add(prepareForPresentation(forecast));
        }

        return DailyForecastScreenModel.create(date, forecasts);
    }

    @VisibleForTesting(otherwise = PRIVATE)
    ForecastScreenModel prepareForPresentation(@NonNull final Forecast forecast) {
        final String time = formatTime(forecast.date());
        final String icon = String.format(iconUrlPattern, forecast.conditions().get(0).icon());
        final String temperature = String.format(temperaturePattern, forecast.temperature().current());
        return ForecastScreenModel.create(time, icon, temperature);
    }

    @VisibleForTesting(otherwise = PRIVATE)
    String formatDate(@NonNull final String date) {
        return formatDate(date, dateParserPattern, dateFormatterPattern);
    }

    @VisibleForTesting(otherwise = PRIVATE)
    String formatTime(@NonNull final String date) {
        return formatDate(date, timeParserPattern, timeFormatterPattern);
    }

    private String formatDate(@NonNull final String value, @NonNull final String parserPattern,
                              @NonNull final String formatterPattern) {
        try {
            final SimpleDateFormat parser = new SimpleDateFormat(parserPattern, Locale.getDefault());
            final Date parsed = parser.parse(value);

            final SimpleDateFormat formatter = new SimpleDateFormat(formatterPattern, Locale.getDefault());
            return formatter.format(parsed);
        } catch (ParseException e) {
            Log.e(TAG, "Error formatting date", e);
            return "";
        }
    }

}
