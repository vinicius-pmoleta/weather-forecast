package com.weatherforecast.features.common.data.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {

    @SerializedName("dt_txt")
    private final String date;

    @SerializedName("main")
    private final Temperature temperature;

    @SerializedName("weather")
    private final List<Condition> conditions;

    public Forecast(@NonNull final String date, @NonNull final Temperature temperature,
                    @NonNull final List<Condition> conditions) {
        this.date = date;
        this.temperature = temperature;
        this.conditions = conditions;
    }

    public String date() {
        return date;
    }

    public Temperature temperature() {
        return temperature;
    }

    public List<Condition> conditions() {
        return conditions;
    }

    public static class Temperature {

        @SerializedName("temp")
        private final Double current;

        @SerializedName("temp_min")
        private final Double minimum;

        @SerializedName("temp_max")
        private final Double maximum;

        public Temperature(@NonNull final Double current, @NonNull final Double minimum,
                           @NonNull final Double maximum) {
            this.current = current;
            this.minimum = minimum;
            this.maximum = maximum;
        }

        public Double current() {
            return current;
        }

        public Double minimum() {
            return minimum;
        }

        public Double maximum() {
            return maximum;
        }
    }

    public static class Condition {

        @SerializedName("main")
        private final String name;

        private final String icon;

        public Condition(@NonNull final String name, @NonNull final String icon) {
            this.name = name;
            this.icon = icon;
        }

        public String name() {
            return name;
        }

        public String icon() {
            return icon;
        }
    }

}
