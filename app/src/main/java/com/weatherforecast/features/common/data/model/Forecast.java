package com.weatherforecast.features.common.data.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class Forecast {

    @SerializedName("dt_txt")
    public abstract String date();

    @SerializedName("main")
    public abstract Temperature temperature();

    @SerializedName("weather")
    public abstract List<Condition> conditions();

    public static Forecast create(@NonNull final String date, @NonNull final Temperature temperature,
                                  @NonNull final List<Condition> conditions) {
        return new AutoValue_Forecast(date, temperature, conditions);
    }

    public static TypeAdapter<Forecast> typeAdapter(Gson gson) {
        return new AutoValue_Forecast.GsonTypeAdapter(gson);
    }

    @AutoValue
    public abstract static class Temperature {

        @SerializedName("temp")
        public abstract Double current();

        @SerializedName("temp_min")
        public abstract Double minimum();

        @SerializedName("temp_max")
        public abstract Double maximum();

        public static Temperature create(@NonNull final Double current, @NonNull final Double minimum,
                                         @NonNull final Double maximum) {
            return new AutoValue_Forecast_Temperature(current, minimum, maximum);
        }

        public static TypeAdapter<Temperature> typeAdapter(Gson gson) {
            return new AutoValue_Forecast_Temperature.GsonTypeAdapter(gson);
        }

    }

    @AutoValue
    public abstract static class Condition {

        @SerializedName("main")
        public abstract String name();

        public abstract String icon();

        public static Condition create(@NonNull final String name, @NonNull final String icon) {
            return new AutoValue_Forecast_Condition(name, icon);
        }

        public static TypeAdapter<Condition> typeAdapter(Gson gson) {
            return new AutoValue_Forecast_Condition.GsonTypeAdapter(gson);
        }

    }

}
